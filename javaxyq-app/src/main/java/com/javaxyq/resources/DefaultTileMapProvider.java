/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.resources;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.javaxyq.config.MapConfig;
import com.javaxyq.core.DataManager;
import com.javaxyq.data.Scene;
import com.javaxyq.io.CacheManager;
import com.javaxyq.widget.TileMap;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 龚德伟
 * @history 2008-5-29 龚德伟 新建
 *          2013-12-26 wpaul modify
 */
@Slf4j
public class DefaultTileMapProvider implements MapProvider {

    public class ImageLoadThread extends Thread {

        protected Component component = new Component() {
        };

        private Image image;

        private boolean isCompleted;

        private boolean isFinished;

        private int mediaTrackerID;

        protected MediaTracker tracker = new MediaTracker(component);

        public ImageLoadThread() {
            setDaemon(true);
            setName("ImageLoadThread");
        }

        /**
         * Returns an ID to use with the MediaTracker in loading an image.
         */
        private int getNextID() {
            return ++mediaTrackerID;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public void run() {
            while (true) {
            	//log.info(this.getId()+" "+this.getName());
                synchronized (this) {
                    if (image != null) {
                        // load image
                        isFinished = false;
                        isCompleted = false;
                        int id = getNextID();
                        tracker.addImage(image, id);
                        try {
                            tracker.waitForID(id, 0);
                            isCompleted = true;
                        } catch (InterruptedException e) {
                            log.info("INTERRUPTED while loading Image");
                        }
                        tracker.removeImage(image, id);
                        isFinished = true;
                        image = null;
                        notifyAll();
                    }
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void loadImage(Image image) {
            this.image = image;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    class MyRandomAccessFile extends RandomAccessFile {

        public MyRandomAccessFile(File file, String mode) throws FileNotFoundException {
            super(file, mode);
        }

        public MyRandomAccessFile(String name, String mode) throws FileNotFoundException {
            super(name, mode);
        }

        public int readInt2() throws IOException {
            int ch1 = this.read();
            int ch2 = this.read();
            int ch3 = this.read();
            int ch4 = this.read();
            if ((ch1 | ch2 | ch3 | ch4) < 0)
                throw new EOFException();
            return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
        }
    }

    private MyRandomAccessFile mapFile;

    /** 地图块入口地址偏移表 */
    private int[][] blockOffsetTable;

    /** 地图宽度 */
    private int width;

    /** 地图高度 */
    private int height;

    /** 地图X方向块数 */
    private int xBlockCount;

    /** 地图Y方向块数 */
    private int yBlockCount;
   
    /** mask数量 */
    private int maskCount;
    
    /** mask偏移量 */
    private int []maskOffsets;
    
	//mask 各类参数；
    private 	byte m_mask[];
    private	int o_pos,m_pos,i_pos;
    private int t; // what?
		

    private ImageLoadThread imageLoader;
    private DataManager dataManager;

    public DefaultTileMapProvider(DataManager dataManager) {
        imageLoader = new ImageLoadThread();
        imageLoader.start();
        this.dataManager = dataManager;
    }

    public Image getBlock(int x, int y) {
        byte[] data = this.getJpegData(x, y);
        Image image = Toolkit.getDefaultToolkit().createImage(data);
        imageLoader.loadImage(image);
        return image;
    }

    public String getDescription() {
        return "default tile map provider for XYQ";
    }

    public int getHeight() {
        return height;
    }

    /**
     * 获取指定的JPEG数据块
     * 
     * @param x X轴索引
     * @param y Y轴索引
     * @return
     */
    public synchronized byte[] getJpegData(int x, int y) {
        byte jpegBuf[] = null;
        try {
            // read jpeg data
            int len = 0;
            mapFile.seek(blockOffsetTable[x][y]);// XXX offset
            if (isJPEGData()) {
                len = mapFile.readInt2();
                jpegBuf = new byte[len];
                mapFile.readFully(jpegBuf);
            }else {
//            	jpegBuf = new byte[0];
//            	return jpegBuf;
            	throw new RuntimeException("read jpeg data failed");
            }

            // modify jpeg data
            ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
            boolean isFilled = false;// 是否0xFF->0xFF 0x00
            bos.reset();
            bos.write(jpegBuf, 0, 2);
            // skip 2 bytes: FF A0
            int p, start;
            isFilled = false;
            for (p = 4, start = 4; p < jpegBuf.length - 2; p++) {
                if (!isFilled && jpegBuf[p] == (byte) 0xFF && jpegBuf[++p] == (byte) 0xDA) {
                    isFilled = true;
                    // 0xFF 0xDA ; SOS: Start Of Scan
                    // ch=jpegBuf[p+3];
                    // suppose always like this: FF DA 00 09 03...
                    jpegBuf[p + 2] = 12;
                    bos.write(jpegBuf, start, p + 10 - start);
                    // filled 00 3F 00
                    bos.write(0);
                    bos.write(0x3F);
                    bos.write(0);
                    start = p + 10;
                    p += 9;
                }
                if (isFilled && jpegBuf[p] == (byte) 0xFF) {
                    bos.write(jpegBuf, start, p + 1 - start);
                    bos.write(0);
                    start = p + 1;
                }
            }
            bos.write(jpegBuf, start, jpegBuf.length - start);
            jpegBuf = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            System.err.println("获取JPEG 数据块失败：" + e.getMessage());
        }
        return jpegBuf;

    }

    public TileMap getResource(String resId) {
        return loadMap(resId);
    }

    public String getVersion() {
        return "1.0";
    }

    public int getWidth() {
        return width;
    }

    public int getXBlockCount() {
        return xBlockCount;
    }

    public int getYBlockCount() {
        return yBlockCount;
    }

    private boolean isJPEGData() {
        byte[] buf = new byte[4];
        try {
            int len = mapFile.readInt2();
            mapFile.skipBytes(len * 4); //mask index?
            mapFile.read(buf);// 47 45 50 4A; GEPJ
            String str = new String(buf);
            return str.equals("GEPJ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean isValidMapFile() {
        byte[] buf = new byte[4];
        try {
            mapFile.read(buf);
            String str = new String(buf);
            return str.equals("0.1M");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 从流加载MAP
     * 
     * @param is
     */
    private void loadHeader() {
        if (!isValidMapFile()) {
            throw new IllegalArgumentException("非梦幻地图格式文件!");
        }
        try {
            // start decoding
            width = mapFile.readInt2();
            height = mapFile.readInt2();
            xBlockCount = (int) Math.ceil(width / 320.0);
            yBlockCount = (int) Math.ceil(height / 240.0);

            blockOffsetTable = new int[xBlockCount][yBlockCount];
            for (int y = 0; y < yBlockCount; y++) {
                for (int x = 0; x < xBlockCount; x++) {
                    blockOffsetTable[x][y] = mapFile.readInt2();
                    //log.info("blockoffsettable is:"+blockOffsetTable[x][y]);
                }
            }
            ReadHead();
            //读取ReadUnit
            /*for (int y = 0; y < yBlockCount; y++) {
                for (int x = 0; x < xBlockCount; x++) {
                	ReadUnit(x,y);
                }
            } */  
            //读取ReadMask
            /*for(int x=0;x<offsetlsize;x++){
            	ReadMask(x);
            }*/
            
            // int headerSize = sis.readInt2();// where need it?
        } catch (Exception e) {
            throw new IllegalArgumentException("地图解码失败:" + e.getMessage());
            // e.printStackTrace();
        }
       
    }
    
  //读取地图的head数据
    /**
     * 从流加载mask偏移量
     * 
     * @param is
     * @throws IOException 
     */
  	public boolean ReadHead() throws IOException {
  		
  		 /*if (!isValidMapFile()) {
             throw new IllegalArgumentException("非梦幻地图格式文件!");
         }*/
  		 
          int headsize = mapFile.readInt2();
          
          //读取mask数量；
          maskCount=mapFile.readInt2();
          
         //log.info("headsize is:"+offsetlsize);
          maskOffsets=new int[maskCount];
          int tmpdata = 0;
          for(int i=0;i<maskCount;i++){
          	maskOffsets[i]=mapFile.readInt2();
          	int masksize = maskOffsets[i]-tmpdata;
          	tmpdata=maskOffsets[i];
          //log.info("m_MaskList is:"+i+" data is:"+m_MaskList[i]+"  Masksize is:"+masksize);

          } 
          return true;
  	}
  	
  //读取地图的单元数据
  	/**
     * 从流加载单元图mask标示
     * 
     * @param is
     * @throws IOException 
     */
  	public MapUnit ReadUnit(int x,int y){
  		
  		try {
  			int[] masklist = new int[0];
  			byte[] m_cell = new byte[0];
  			long seek;
  			boolean Result;
  			boolean loop=true;
  			seek=blockOffsetTable[x][y];
  			
  			
  			mapFile.seek(seek);
  			//log.info("seek is:"+seek);
  			
  			byte masknum[]=new byte[4];
  			mapFile.read(masknum, 0, 4);
  			int m_MaskNum = constructInt(masknum,0); //读取mask数量
  			
  			
  			//log.info("masknum is:"+m_MaskNum);
  			if(m_MaskNum>0){
  			byte MaskList[]=new byte[4*m_MaskNum];
  			masklist = new int[m_MaskNum];
  			mapFile.read(MaskList, 0, 4*m_MaskNum);
  			    for(int i=0;i<m_MaskNum;i++){
  				    masklist[i]=constructInt(MaskList,4*i);
  			        //log.info("masklist "+i+" is:"+masklist[i]+" is:"+m_MaskList[masklist[i]]);
  			    }
  			}
  			
  			while(loop){
  				byte unithead[]=new byte[4*2];
  				mapFile.read(unithead,0,4*2);
  				int m_unithead=constructInt(unithead,0); //读取单元的头数据
  				int m_headSize=constructInt(unithead,4);
  				
  				//log.info(0x494D4147+","+0x4A504547+","+0x4D41534B+","+
  						//0x424C4F4B+","+0x43454C4C+","+0x42524947);
  				
  				//log.info("head.flag is:"+m_unithead+"  head.size is:"+
  					//	m_headSize);
  				
  				switch (m_unithead){
  				case 0x494D4147: log.info("imag"); break;//GAMI
  				case 0x4A504547: //log.info("jpeg");
  				byte []m_jpeg=new byte[m_headSize];
  				
  				
  				
  				//log.info("$%$^$%#%$#%#%#%"+size);
  				/*try {
  					
  					mapFile.read(m_jpeg,0,m_headSize);
  					
  					
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}*/
  				mapFile.skipBytes(m_headSize);
  				
  				break;//GEPJ
  				
  				case 0x4D41534B: log.info("mask");break;//KSAM
  				case 0x424C4F4B: log.info("blok");break;//KOLB
  				case 0x43454C4C: //log.info("cell");
  				m_cell = new byte[m_headSize];
  				try {
  					mapFile.read(m_cell,0,m_headSize);
  					
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  				break;//LLEC
  				case 0x42524947: //log.info("brig");
  				byte m_brig[]=new byte[m_headSize];
  				
  				try {
  					mapFile.read(m_brig,0,m_headSize);
  				
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  				loop=false;
  				break;//GIRB
  				}
  			}

  			return new MapUnit(masklist, m_cell);
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		
  		return null;
  		
  	}
  	
  //读取地图的单元数据
  	/**
     * 从流加载mask数据
     * 
     * @param is
     * @throws IOException 
     */
  	public MaskUnit ReadMask(int UnitNum){
		
		try {
	
			long seek;
			//log.info("UnitNum is:"+UnitNum+",maskoffestlist is:"+m_MaskList[UnitNum]);
			seek = maskOffsets[UnitNum];
			mapFile.seek(seek);
			byte maskhead[] = new byte[20];
			mapFile.read(maskhead, 0, 20);
			int maskX = constructInt(maskhead,0);
			int maskY = constructInt(maskhead,4);
			int maskWidth = constructInt(maskhead,8);
			int maskHeight = constructInt(maskhead,12);
			
			int masksize = constructInt(maskhead,16);
			
			//读mask数据
			//log.info("mask x,y,width,height is:"+maskkeyx+","+maskkeyy+","+maskwidth+","+maskheight);
			//log.info("masksize is:"+masksize);
			m_mask = new byte[masksize];
			mapFile.read(m_mask,0,masksize);
			
			/*for(int i=0;i<masksize;i++){
				log.info(constructMask(m_mask,i));
			}*/
			
			// 解密mask数据
			int bol;
			if(maskWidth%4==0){
				bol=0;
			}else{
				bol=1;
			}
			int align_width = (maskWidth / 4 + bol) * 4;	// 以4对齐的宽度
			byte[] pMaskDataDec = new byte[align_width * maskHeight / 4];		// 1个字节4个像素，故要除以4
			int dec_mask_size = DecompressMask(m_mask, pMaskDataDec);
			//log.info("alignwidth is:"+align_width);
			//log.info("pmaskdatasize is:"+pMaskDataDec.length);
			
			//log.info("dec_mask_size is:"+dec_mask_size);
			
			/*for(int i=0;i<dec_mask_size;i++){
				log.info(constructMask(pMaskDataDec,i));
			}*/
			
			//mask数据还原
			int[] maskData = new int[maskWidth*maskHeight];
			int ow=align_width-maskWidth;
			int md=0;
			//log.info("align width is:"+align_width);
			//log.info("ow is:"+ow);
			
			
			for(int i=0;i<dec_mask_size;i++){
				
				//log.info("i is:"+i+",dec_mask is:"+(int)constructMask(pMaskDataDec,i));
				if(((i+1)*4)%align_width==0 ){
					
					for(int j=0;j<4-ow;j++){
						maskData[md++]=constructMaskData(pMaskDataDec[i],j);
						//log.info("num is:"+(md-1)+","+maskData[md-1]);
					}
	
			    }else if(maskWidth<4){
			    	for(int j=0;j<maskWidth;j++){
						maskData[md++]=constructMaskData(pMaskDataDec[i],j);
						//log.info("num is:"+(md-1)+","+maskData[md-1]);
					}
			    }
				else{
					 for(int j=0;j<4;j++){
						maskData[md++]=constructMaskData(pMaskDataDec[i],j);
						//log.info("num is:"+(md-1)+","+maskData[md-1]);
					 }
			    }
	
				//masktemp=constructMask(pMaskDataDec,i);
			   // log.info(masktemp);	
			}
			/*for(int i=0;i<maskwidth*maskheight;i++){
				log.info(maskData[i]);
			}*/
		
			return new MaskUnit(maskX, maskY, maskWidth, maskHeight, maskData);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

    public int getMaskCount() {
		return maskCount;
	}

	private TileMap loadMap(String sceneId) {
        if (mapFile != null) {
            try {
                mapFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.blockOffsetTable = null;
        }
        
        Scene scene = dataManager.findScene(Integer.valueOf(sceneId));
        if(scene!=null) {
            try {
            	String path = String.format("scene/%s.map", sceneId);
				String music = String.format("music/%s.mp3", sceneId);;
				MapConfig cfg = new MapConfig(sceneId, scene.getName(), path, music);
                File file = CacheManager.getInstance().getFile(cfg.getPath());
                mapFile = new MyRandomAccessFile(file, "r");
                loadHeader();
                return new TileMap(this, cfg);
            } catch (Exception e) {
                System.err.println("create map decoder failed!");
                e.printStackTrace();
            }
        }

        /*MapConfig cfg = (MapConfig) ResourceStore.getInstance().findConfig(id);
        if (cfg != null) {
            try {
                File file = new File(cfg.getPath());
                mapFile = new MyRandomAccessFile(file, "r");
                loadHeader();
                return new TileMap(this, cfg);
            } catch (Exception e) {
                System.err.println("create map decoder failed!");
                e.printStackTrace();
            }
        }*/
        return null;
    }

    public void dispose() {
        try {
            mapFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        blockOffsetTable = null;
        imageLoader.stop();
    }
    
    /**
     * 从流加载mask数据并解码
     * 
     * @param is
     * @throws IOException 
     */    
    public int DecompressMask(byte[] m_mask,byte [] pMaskDataDec){
		
		o_pos=0;
		m_pos=0;
		i_pos=0 ;
		byte [] op = pMaskDataDec;
		
		byte [] ip= m_mask;
		 
		
		boolean first_literal_run;
		
		int masktemp;
  	
		
		cont:
		while(true){
			
			first_literal_run=true;
			
			if(i_pos==0){
				if(constructMask(ip,i_pos)>17){
					t = constructMask(ip,i_pos++)-17;
					if(t<4){
						do{
							op[o_pos++]=ip[i_pos++];
						}while(--t>0);
					t=constructMask(ip,i_pos++);
					if(match(op,ip)==1){   //goto match
						break; 
					}else{
						//log.info("i_pos is:"+i_pos);
						continue cont;
					}
					
					}else{
						do{
							op[o_pos++]=ip[i_pos++];
						}while(--t>0);
						first_literal_run=false;
					}
				}
			}

			if(first_literal_run){
				//log.info("ip is:"+ip[i_pos]+",i_pos is:"+i_pos);
			t=constructMask(ip,i_pos++);
			
			//log.info("t is:"+t);
			
			if(t>=16) {
				if(match(op,ip)==1){
					break;
				}else{
					//log.info("i_pos is:"+i_pos);
					continue cont;
				}
			}
			if(t==0){
				while(constructMask(ip,i_pos)==0){
					t+=255;
					i_pos++;
				}
				t+=15+constructMask(ip,i_pos++);
			}
			
			//op[o_pos]=ip[i_pos];
            for(int i=0;i<4;i++){
            	op[o_pos++]=ip[i_pos++];    // 获取sizeof(unsigned)个新字符
            	//log.info("op is:"+op[o_pos-1]);
            	
            }
            
            //log.info("t is:"+t);
			if(--t>0){
				
				if(t>=4){
					do{
						for(int i=0;i<4;i++){
			            	op[o_pos++]=ip[i_pos++];   // 获取sizeof(unsigned)个新字符
			            	
			            	//log.info("--t op is:"+op[o_pos-1]);
			            }
						t-=4;
					}while(t>=4);
					if(t>0){
						do{
					       op[o_pos++]=ip[i_pos++];	
					    }while(--t>0);
					}
				}else {
					do{
						op[o_pos++]=ip[i_pos++];
						//log.info("op is:"+op[o_pos-1]);
					}while(--t>0);
				}
			}
			}
			
			
			//first_literal_run
			
				t=constructMask(ip,i_pos++);
				
				//log.info("t is:"+t+",ipos is:"+i_pos);
			if(t>=16){                             // 是重复字符编码
				if(match(op,ip)==1){
					break;
				}else{
					//log.info("i_pos is:"+i_pos);
					continue cont;
				}
				
			}else{
				log.info("0x0801");
				m_pos = o_pos - 0x0801;
				m_pos -= t>>2;
	            m_pos -= constructMask(ip,i_pos++)<<2;
	            
	            op[o_pos++] = ip[m_pos++];
	            op[o_pos++] = ip[m_pos++];
	            op[o_pos++] = ip[m_pos++];
	            
	            //goto match_done
	            t=(ip[i_pos]>>6)&3;
				if(t==0){
					
				}else{
					do{
						op[o_pos++]=ip[i_pos++];
					}while(--t>0);
				t=constructMask(ip,i_pos++);
				if(match(op,ip)==1){
					break;
				}else{
					//log.info("i_pos is:"+i_pos);
					continue cont;
				}
				}
			}
			

			
		}
		return o_pos;
	}

/**
 * mask解码
 * 
 * @param is
 * @throws IOException 
 */    
	
	public int match(byte[]op,byte[]ip){
		
		while(true){
			
			
			boolean copy_match=false;
			boolean match_done=true;
			byte []ms=new byte[o_pos];
			int num=0;
			
			//log.info("t is:"+t);
			if(t>=64){

				for(int i=0;i<o_pos;i++){
					ms[i]=op[i];
				}
				
				
				
				num=1;
				
				int po = (t>>2)&7;
				num+=po;				
				
				po = constructMask(ip,i_pos++)<<3;
				num+=po;
				
				
			       
			        int mtemp=0;
			      
			        
					/*for(int i=o_pos-1;i>=num;i--){
				      ms[i]=ms[i-num];

					}*/
					
					for(int j=o_pos-num;j<=o_pos-1;j++){
						ms[mtemp++]=op[j];
					}
				
				 /*for(int i=0;i<o_pos;i++){
				   log.info("op is:"+op[i]);
			    }*/
				
				t = (t>>5)-1;
				
				copy_match=true;
			}else if(t>=32){

				t&=31;
			
				if(t==0){
					while(ip[i_pos]==0){
						t+=255;
						i_pos++;
						
					}
					
					t+=31+constructMask(ip,i_pos++);
					
				}
				
				//m_pos = o_pos -1;
				//ms=op;
				
				for(int i=0;i<o_pos;i++){
					ms[i]=op[i];
				}

				num=1;

				int po = constructWord(ip,i_pos)>>2;
				num+=po;
				
				int mtemp=0;
				for(int j=o_pos-num;j<=o_pos-1;j++){
					ms[mtemp++]=op[j];
				}

				/*for(int i=0;i<o_pos;i++){
				log.info("op2 is:"+op[i]);
			    }
				*/
				i_pos +=2;
				
			}else if(t>=16){
				
				byte temp;
				
				//m_pos = o_pos;
				for(int i=0;i<o_pos;i++){
					ms[i]=op[i];
				}
				
				//m_pos -= (t&8)<<11;
				int po = (t&8)<<11;
				num=po;

				t&=7;
				if(t==0){
					while(ip[i_pos]==0){
						t+=255;
						i_pos++;
					}
					
					t+=7+constructMask(ip,i_pos++);
				}
				
				//m_pos -= constructMask(ip,i_pos)>>2;
				po = constructWord(ip,i_pos)>>2;
				num+=po;
				
				
				i_pos +=2;
				if(ip.length == i_pos){
					//log.info("o_pos is:"+o_pos);
					return 1;
				}else{
					//m_pos -= 0x4000;
					po = 0x4000;
					//log.info("0x4000 is:"+po);
				num+=po;
				//log.info("num is:"+num);
				int mtemp=0;
				for(int j=o_pos-num;j<=o_pos-1;j++){
					ms[mtemp++]=op[j];
				}
					
					
					
				}
				
			}else{
				
				log.info("t0-15 is:"+t);
				
				byte temp;
				//m_pos = o_pos-1;
				for(int i=0;i<o_pos;i++){
					ms[i]=op[i];
				}
				
				temp=ms[o_pos-1];
				num=1;

				for(int i=o_pos-1;i>=1;i--){
			      ms[i]=ms[i-1];
			      
			      
				}
				ms[0]=temp;
				
				
				
				//m_pos -= t>>2;
				int po = (t>>2);
				num+=po;
				for(int j=0;j<po;j++){
					temp=ms[o_pos-1];
					
					for(int i=o_pos-1;i>=1;i--){
				      ms[i]=ms[i-1];

					}
					ms[0]=temp;
				}
				
				
				
				//m_pos -= constructMask(ip,i_pos++)<<2;
				po = constructWord(ip,i_pos++)<<2;
				num+=po;
				for(int j=0;j<po;j++){
					temp=ms[o_pos-1];
					
					for(int i=o_pos-1;i>=1;i--){
				      ms[i]=ms[i-1];

					}
					ms[0]=temp;
				}
				
				
				op[o_pos++] = ms[0];
				op[o_pos++] = ms[1];
				match_done=false;
			}
			
			if(match_done){

			if((t>=6 && num>=4)&&copy_match==false){
				//op[o_pos] = ip[i_pos];
				m_pos=0;
				for(int i=0;i<4;i++){
	            	op[o_pos++]=ms[m_pos++];    // 获取sizeof(unsigned)个新字符
	            }
				t-=2;
				do{
					
						for(int i=0;i<4;i++){
							if(m_pos<num){
								op[o_pos++]=ms[m_pos++];    // 获取sizeof(unsigned)个新字符
							}else{
								m_pos=0;
								op[o_pos++]=ms[m_pos++];    // 获取sizeof(unsigned)个新字符
							}
			            	
			            }
					
					
					t-=4;
				}while (t>=4);
				if(t>0){
					do{
						if(m_pos<num){
							op[o_pos++]=ms[m_pos++];
						}else{
							m_pos=0;
							op[o_pos++]=ms[m_pos++];
						}
						
					}while(--t>0);
				}
			}else{
//copy_match: 
				m_pos=0;
	
				    for(int i=0;i<2;i++){
				    	if(m_pos<num){
				    		op[o_pos++]=ms[m_pos++];
							//log.info("ms is:"+ms[m_pos-1]);
				    	}else{
				    		m_pos=0;
				    		op[o_pos++]=ms[m_pos++];
					        //log.info("ms is:"+ms[m_pos-1]);
				    	}
				    	
				    }

			        do{
			        	if(m_pos<num){
			        	op[o_pos++]=ms[m_pos++];
			        	//log.info("ms is:"+ms[m_pos-1]+" m_pos is:"+m_pos);
			        	}else{
			        		m_pos=0;
			        	op[o_pos++]=ms[m_pos++];
			        	//log.info("ms is:"+ms[m_pos-1]+" m_pos is:"+m_pos);
			        	}
			        	
			        }while(--t>0);
			}
			}
	
				t=(ip[i_pos-2])&3;

			if(t==0){
				return 0;
			}
			
			//match_next:
				do{
					op[o_pos++]=ip[i_pos++];
					//log.info("op is:"+op[o_pos]);
				}while(--t>0);
			t=constructMask(ip,i_pos++);
			
		}
	}
    
    /**
     * 将BYTE格式转化
     * 
     * @param is
     */
    public static int constructInt(byte[] in, int offset) {

        int ret = ((int) in[offset + 3] & 0xff);

        ret = (ret << 8) | ((int) in[offset + 2] & 0xff);

        ret = (ret << 8) | ((int) in[offset + 1] & 0xff);

        ret = (ret << 8) | ((int) in[offset + 0] & 0xff);

        return ret;

    }
    /**
     * 将BYTE格式转化
     * 
     * @param is
     */
     public static int constructWord(byte[] mask,int offset){
	    int ret = ((int) mask[offset + 1] & 0xff);
	    ret = (ret << 8) | ((int) mask[offset + 0] & 0xff);
	    return ret;
     }
 
     /**
      * 将BYTE格式转化
      * 
      * @param is
      */
     public static int constructMaskData(byte mask,int offset){
	    int ret=0;
	    switch(offset){
	    case 0:ret = (int)mask & 0x03;break;
	    case 1:ret = ((int)mask & 0x0c)>>2;break;
	    case 2:ret = ((int)mask & 0x30)>>4;break;
	    case 3:ret = ((int)mask & 0xc0)>>6;break;
	    }
	    return ret;
     }

     /**
      * 将BYTE格式转化
      * 
      * @param is
      */
     public static int constructMask(byte[] mask,int offset){
	    int ret = (int)mask[offset] & 0xff;
	    return ret;
     }
     /**
      * 将BYTE格式转化
      * 
      * @param is
      */
     public static short constructShort(byte[] in, int offset) {

        short ret =0x00;

        ret = (short) ((ret << 8) | (short) ((short) in[offset] & 0xff));

        return (ret);

     }
    
    
}
