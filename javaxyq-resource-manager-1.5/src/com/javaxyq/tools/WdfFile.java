package com.javaxyq.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.javaxyq.core.Toolkit;
import com.javaxyq.util.Utils;

/**
 * .WDF文件类
 * 
 * @author 龚德伟
 * @history 2008-6-10 龚德伟 新建
 */
public class WdfFile implements FileSystem {

    /** 文件句柄 */
    private RandomAccessFile fileHandler;

    /** 大话西游2/梦幻西游/富甲西游 WDF文件标记 */
    private static final String WDFP_FILE = "PFDW";

    /** 大话3/大话外传 */
    private static final String WDFX_FILE = "XFDW";

    /** 大话3/大话外传 */
    private static final String WDFH_FILE = "HFDW";

    /** 包内结点个数 */
    private int fileNodeCount;

    /** 文件全名 */
    private String filename;

    /** 结点映射表 */
    private Map<Long, WdfFileNode> fileNodeMap = new HashMap<Long, WdfFileNode>();

    private FileObject rootNode;

    private String fileTag;

    private long[] dh3Keys;

    /**
     * 判断打开的文件是否为WDF文件
     * 
     * @param raf
     * @return
     * @throws IOException
     */
    private boolean isWdfFile(RandomAccessFile raf) throws IOException {
        byte[] buf = new byte[4];
        raf.seek(0);
        raf.read(buf);
        fileTag = new String(buf);
        return fileTag.equals(WDFP_FILE) || fileTag.equals(WDFX_FILE) || fileTag.equals(WDFH_FILE);
    }

    public WdfFile(String filename) {
        try {
            filename = StringUtils.replaceChars(filename, '\\', '/');
            this.filename = filename;
            fileHandler = new RandomAccessFile(filename, "r");
            if (!isWdfFile(fileHandler)) {
                throw new IllegalArgumentException("这个不是WDF格式的文件！path=" + filename + ",tag="
                        + fileTag);
            }
            //init key
            if (WDFX_FILE.equals(fileTag) || WDFH_FILE.equals(fileTag)) {
                loadDH3Key("dh3.dat");
            }
            //read
            fileNodeCount = readInt(fileHandler);
            if(fileNodeCount==0) {//可能是【精灵牧场】格式(四字节00) 
            	fileNodeCount = readInt(fileHandler);
            }
            int headerSize = readInt(fileHandler);
            fileHandler.seek(headerSize);
            fileNodeMap.clear();
            //是否需要异或运算，还原id值(大话3资源)
            boolean xor = WDFX_FILE.equals(fileTag) || WDFH_FILE.equals(fileTag);
            int dh3i = 0;
            for (int i = 0; i < fileNodeCount; i++) {
                WdfFileNode node = new WdfFileNode();
                long id = readUnsignInt(fileHandler);
                node.setId(xor ? id ^ dh3Keys[dh3i] : id);
                int offset = readInt(fileHandler);
                node.setOffset((int) (xor ? offset ^ dh3Keys[dh3i + 1] : offset));
                int size = readInt(fileHandler);
                node.setSize((int) (xor ? size ^ dh3Keys[dh3i + 2] : size));
                int space = readInt(fileHandler);
                node.setSpace((int) (xor ? space ^ dh3Keys[dh3i + 3] : space));

                dh3i += 4;
                dh3i %= 0x40;
                node.setFileSystem(this);
                fileNodeMap.put(node.getId(), node);
            }
            rootNode = new WdfDirectoryObject(this);
            //load description
            load(null);
            //restore path
            String name = StringUtils.substringAfterLast(filename, "/");
            restorePaths(name);

        } catch (Exception e) {
            System.err.println("打开WDF文件出错：" + filename);
            e.printStackTrace();
        }
        System.out.printf("nodeCount=%s, total find:%s\n", fileNodeCount, fileNodes().size());
    }
    
    private Map<Long, String> buildPaths(String filename) {
    	String cmtfile ="resources/names/"+ filename.replaceAll("\\.wd.*", ".cmt");
    	Map<Long, String> map = new HashMap<Long, String>();
        InputStream is = Utils.getResourceAsStream(cmtfile);
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String strPath = null;
            try {
                while ((strPath = br.readLine()) != null) {
                	try {
						strPath = strPath.trim();
						if(strPath.length()>0) {
							String[] strs = strPath.split("=");
							if(strs!=null && strs.length>1) {
								map.put(Long.parseLong(strs[0],16), strs[1]);
							}
						}
					} catch (Exception e) {
						System.out.println("解析资源映射失败："+strPath);
						//e.printStackTrace();
					}
                }
            } catch (Throwable e) {
            	System.err.println("还原文件名列表失败: " + cmtfile);
            	e.printStackTrace();
            }
        } else {
            System.err.println("读取资源失败: " + cmtfile);
        }
    	
		return map;
    }

    private void restorePaths(String name) {
    	Map<Long, String> id2PathMap = buildPaths(name);
//        Map<Long, String> id2PathMap = HashUtil.createId2PathMap("resources/names/"
//                + name.replaceAll("\\.wd.*", ".lst"));
        Set<Entry<Long, WdfFileNode>> entryset = fileNodeMap.entrySet();
        int iCount =0;
        for (Entry<Long, WdfFileNode> entry : entryset) {
            WdfFileNode node = entry.getValue();
            String path = id2PathMap.get(entry.getKey());
            path = StringUtils.replaceChars(path, '\\', '/');
            if (path != null) {
                //System.out.printf("匹配: %s=%s\n", strId, path);
                //start with '/'
                if (path.charAt(0) != '/') {
                    path = "/" + path;
                }
                node.setPath(path);
                node.setName(StringUtils.substringAfterLast(path, "/"));
                iCount ++;
            } else {//找不到path
            	String strId = Long.toHexString(entry.getKey());
                node.setPath("/" + strId);
                //System.err.printf("不匹配结点: id=%s\n", strId);
            }
        }
        System.out.printf("共有文件%d个，匹配文件%d个，无匹配文件%d个\n",fileNodeCount,iCount,(fileNodeCount-iCount));
    }

    private void loadDH3Key(String file) {
        InputStream is = Toolkit.getInputStream("/com/javaxyq/tools/" + file);
        DataInputStream dis = new DataInputStream(is);
        try {
            System.out.println("size=" + dis.available());
            dh3Keys = new long[0x40];
            for (int i = 0; i < dh3Keys.length; i++) {
                dh3Keys[i] = readUnsignInt(dis);
            }
        } catch (IOException e) {
            System.err.println("loadDH3Key error!");
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        if (this.fileHandler != null) {
            this.fileHandler.close();
        }
        this.fileNodeMap.clear();
    }

    private int readInt(DataInput di) throws IOException {
        int ch1, ch2, ch3, ch4;
        ch1 = di.readUnsignedByte();
        ch2 = di.readUnsignedByte();
        ch3 = di.readUnsignedByte();
        ch4 = di.readUnsignedByte();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }

    private long readUnsignInt(DataInput di) throws IOException {
        long ch1, ch2, ch3, ch4;
        ch1 = di.readUnsignedByte();
        ch2 = di.readUnsignedByte();
        ch3 = di.readUnsignedByte();
        ch4 = di.readUnsignedByte();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }

    /**
     * 获取WdfFile包含的文件结点集合
     * 
     * @return
     */
    public Collection<WdfFileNode> fileNodes() {
        return fileNodeMap.values();
    }

    public int getFileNodeCount() {
        return fileNodeCount;
    }

    /**
     * 根据id获取对应的数据
     * 
     * @param nid 结点id
     * @return 返回一个输入流
     * @throws IOException
     */
    public InputStream getNodeAsStream(long nodeId) throws IOException {
        return new ByteArrayInputStream(this.getNodeData(nodeId));
    }

    /**
     * 根据id获取对应的数据
     * 
     * @param nid
     * @return
     * @throws IOException
     */
    public byte[] getNodeData(long nodeId) throws IOException {
        //检索结点对象
        WdfFileNode fnode = fileNodeMap.get(nodeId);
        byte[] data = null;
        if (fnode != null) {
            data = new byte[(int) fnode.getSize()];
            //偏移到结点数据段位置
            fileHandler.seek(fnode.getOffset());
            //读取结点数据
            fileHandler.readFully(data);
        }
        return data;
    }

    @Override
    public String toString() {
        return "[wdf name=" + filename + "]";
    }

    public String getName() {
        return filename;
    }

    public WdfFileNode findNode(long nodeId) {
        return fileNodeMap.get(nodeId);
    }

    public WdfFileNode findNode(String nodeId) {
        return fileNodeMap.get(Long.parseLong(nodeId, 16));
    }

    /**
     * 加载描述文件
     * 
     * @param filename
     * @throws Exception
     */
    public void loadDescription(InputStream is) {
        if (is != null) {
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("(\r\n)|(\n\r)|[\n\r=]");
            String tag = scanner.next();
            long uid;
            String str=null, alias = null;
            int iCount = 0;
            if (tag.startsWith("[Resource]")) {
                while (scanner.hasNext()) {
                    WdfFileNode node = null;
					try {
						scanner.skip("(\r\n)|(\n\r)|[\n\r=]");
						str = scanner.next();
						uid = Long.parseLong(str, 16);
						scanner.skip("(\r\n)|(\n\r)|[\n\r=]");
						alias = scanner.next().trim();
						node = fileNodeMap.get(uid);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
                    if (node != null) {
                        node.setDescription(alias.replace('\\', '/'));
                        //System.out.println("资源:" + Long.toHexString(uid) + "=" + alias);
                    } else {
                        System.out.println("找不到对于的资源:" + str + "=" + alias);
                    }
                    iCount++;
                }
            }
            System.out.println("total : " + iCount);
            scanner.close();
        }
    }

    /**
     * 取path下面的结点列表
     * 
     * @param path 路径
     * @param subpath 是否包含子目录
     * @return
     */
    public List<WdfFileNode> getNodesUnderPath(String path, boolean subpath) {
        return null;
    }

    /**
     * 保存资源描述文件
     * 
     * @param filename
     */
    public void saveDescription(FileOutputStream os) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write("[Resource]\r\n");
            List<WdfFileNode> nodeList = getNodesUnderPath("/", false);
            for (WdfFileNode node : nodeList) {
                if (node.getName() != null && node.getName().length() > 0) {
                    writer.write(Long.toHexString(node.getId()).toUpperCase());
                    writer.write('=');
                    writer.write(node.getName());
                    writer.write("\r\n");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 清除资源描述信息
     */
    public void clearDescription() {
        Collection<WdfFileNode> nodes = fileNodes();
        for (WdfFileNode node : nodes) {
            node.setDescription(null);
        }
    }

    public FileObject getRoot() {
        return rootNode;
    }

    public String getType() {
        return "wdf";
    }

    public void load(String descfile) {
    	if(descfile==null) {
    		descfile = "resources/desc/" + new File(this.filename).getName() + ".ini";
    	}
        this.loadDescription(Utils.getResourceAsStream(descfile));
    }

    public void save(String filename) {
        try {
            this.saveDescription(new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            System.out.println("save description failed! filename=" + filename);
            e.printStackTrace();
        }
    }

    /**
     * 从offset开始读取长度为len的数据块
     * 
     * @param data
     * @param offset
     * @param len
     * @throws IOException
     */
    public void read(byte[] data, int offset, int len) throws IOException {
        fileHandler.seek(offset);
        fileHandler.readFully(data, 0, len);
    }

}
