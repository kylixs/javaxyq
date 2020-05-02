/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.*;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import com.javaxyq.config.MapConfig;
import com.javaxyq.core.GameMain;
import com.javaxyq.resources.DefaultTileMapProvider;
import com.javaxyq.resources.MapUnit;
import com.javaxyq.resources.MaskUnit;
import com.javaxyq.widget.Player;



/**
 * @author 龚德伟
 * @history 2008-5-22 龚德伟 新建
 *          2013-12-25 wpaul modify
 */
public class TileMap extends AbstractWidget {

    /** 地图块像素宽度 */
    public static final int MAP_BLOCK_WIDTH = 320;

    /** 地图块像素高度 */
    public static final int MAP_BLOCK_HEIGHT = 240;
    
    public static final int STEP_DISTANCE = 20;

    private DefaultTileMapProvider provider;

    private SoftReference<Image>[][] blockTable;

    /** 地图X方向块数 */
    private int xBlockCount;

    /** 地图Y方向块数 */
    private int yBlockCount;
    
//    private int offsetX;
//    private int offsetY;
//    private int firstTileX;
//    private int lastTileX;
//    private int firstTileY;
//    private int lastTileY;

    private int width;

    private int height;

    private MapConfig config;

    private int lastCount;
    
 //子地图Cell参数
    private byte [][] cellData;
    
 // 子地图及mask图 参数
    private MaskUnit[] maskUnits;
    private BufferedImage[] tileImageDes;
    private ColorModel cm;
    private Object data;
    private int[] des;

    /**
     * 构造TileMap
     * 生成子地图tileImage
     * 生成MASK遮掩图tileImageDes
     */
    public TileMap(DefaultTileMapProvider provider, MapConfig cfg) {
    	
        
    	
        //水平方向w块，垂直方向h块
        this.config = cfg;
        this.xBlockCount = provider.getXBlockCount();
        this.yBlockCount = provider.getYBlockCount();
        this.width = provider.getWidth();
        this.height = provider.getHeight();
        blockTable = new SoftReference[this.xBlockCount][this.yBlockCount];
        this.provider = provider;
        
        //totalcell初始化
        int wbol,hbol;
        if(width%320==0){
			wbol=0;
		}else{
			wbol=1;
		}
        if(height%240==0){
			hbol=0;
		}else{
			hbol=1;
		}
        int swidth  = (width / 320 + wbol) * 320/STEP_DISTANCE;
        int sheight = (height / 240 + hbol) * 240/STEP_DISTANCE;
        cellData = new byte[sheight][swidth];
        
        //mask像素；
        tileImageDes = new BufferedImage[provider.getMaskCount()];
        maskUnits = new MaskUnit[provider.getMaskCount()];
//        rasterDes = new WritableRaster[provider.offsetlsize];
//        tiles = new ArrayList<Object>();
        //cell= new boolean[(int) rMap.m_SubMapRowNum][(int) rMap.m_SubMapColNum][12][16];
//        maskkey= new ArrayList<int[]>();
//        mask = new ArrayList<int[]>();
//        mask_data = new ArrayList<int[]>();
//        m_anteroposterior = new int[provider.getWidth()][provider.getHeight()];
       // mapwidth=width;
        //mapheight=height;
        
        //确定行数列数
        int b=yBlockCount;
        int a=xBlockCount;
        //System.out.println("a,b is:"+a+","+b);
        
        
        //读取子地图
        for(int y=0;y<yBlockCount;y++){
          for(int x=0;x<xBlockCount;x++){
        	MapUnit mapUnit = provider.ReadUnit(x,y);
        	
        	//子地图合成cell
        	byte[] cellData = mapUnit.getCellData();
        	for(int ch=0;ch<12;ch++){
               for(int cw=0;cw<16;cw++){
            	//System.out.println(provider.m_cell[c]);
            	//totalcell[((12*y+ch)*16*xBlockCount)+(16*x+cw)] = provider.m_cell[ch*16+cw];
				this.cellData[y*12+ch][x*16+cw]=cellData[ch*16+cw];
                }
        	}
            
        	//tiles.add(tileImage);
          }
        }
        
        
    }
    
    
    /**
     * 加载totalRaster中KEYX,KEYY处像素
     * 至rasterDes处 并判断是否drawmask
     */
  //mask图规则标签
    public void loadMask(int viewX,int viewY){
    	//初始化MASK遮掩图并输出
       int firstTilex = pixelsToTilesw(viewX);
       int lastTilex = pixelsToTilesw(viewX + MAP_BLOCK_WIDTH*2);
       int firstTiley = pixelsToTilesh(viewY);
       int lastTiley = pixelsToTilesh(viewY + MAP_BLOCK_HEIGHT*3);
        
       lastTilex=Math.min(lastTilex, (int)(provider.getXBlockCount()-1));
       lastTiley=Math.min(lastTiley, (int)provider.getYBlockCount());
        //System.out.println("fistX,Y is:"+firstTilex+","+firstTiley);
        //System.out.println("lastX,Y is:"+lastTilex+","+lastTiley);
        
       //读取地图mask信息;
        for (int y = firstTiley; y <lastTiley; y++) {
            for (int x = firstTilex; x <= lastTilex; x++) {
            	
            	int unitnum = (y*xBlockCount+x);
            	//System.out.println("unitnum"+ "is:"+unitnum);
            	MapUnit mapUnit = provider.ReadUnit(x, y);
            	int []masknum = mapUnit.getMaskIndexs();
            	for(int i=0;i<masknum.length;i++){
            		int maskIndex = masknum[i];
            		if(tileImageDes[maskIndex] != null) {
        			continue;
        		    }
        			MaskUnit maskUnit = getMaskUnit(maskIndex);
                	createTileMaskImg(masknum[i],maskUnit.getX(),maskUnit.getY(),maskUnit.getWidth(),
                			maskUnit.getHeight(), maskUnit.getData());           		
            	}
            }	 
           	 
        }
        
        //TODO 释放没显示的mask图像
        
    }

	private MaskUnit getMaskUnit(int maskIndex) {
		MaskUnit maskUnit = maskUnits[maskIndex];
		if(maskUnit == null) {
			maskUnit = provider.ReadMask(maskIndex);
			maskUnits[maskIndex] = maskUnit;
		}
		return maskUnit;
	}
    
    private void createTileMaskImg(int unitnum,int keyx,int keyy,int width,int height,int []mask){
		if(tileImageDes[unitnum] != null) {
			return;
		}
//		tileImageDes[unitnum] = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    	
    	int firstTilex = pixelsToTilesw(keyx);
    	int lastTilex = pixelsToTilesw(keyx + width - 1);
        int firstTiley = pixelsToTilesh(keyy);
        int lastTiley = pixelsToTilesh(keyy + height - 1);
       // lastTileX=Math.min(lastTileX, (int)(rMap.m_SubMapRowNum-1));
       // lastTileY=Math.min(lastTileY, (int)rMap.m_SubMapColNum);
        //System.out.println("fistX,Y is:"+firstTilex+","+firstTiley);
        //System.out.println("lastX,Y is:"+lastTilex+","+lastTiley);
        
      //合并子地图并输出像素值；
        BufferedImage tempMaskImage = new BufferedImage((lastTilex+1-firstTilex)*320,
        	    (lastTiley+1-firstTiley)*240,BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster tempMaskRaster = tempMaskImage.getRaster();
        for (int y = firstTiley; y < lastTiley+1; y++) {
         	
            for (int x = firstTilex; x < lastTilex+1; x++) {
            	
            	try {
            		//读取IMAGE数据
            		InputStream buffin = new ByteArrayInputStream(provider.getJpegData(x, y));
            		BufferedImage Image= ImageIO.read(buffin);
            		Raster raster = Image.getRaster();
            		
            		//合并raster像素矩阵
                	//System.out.println("x,y is:"+320*(x-firstTilex)+","+240*(y-firstTiley));
                	//System.out.println("num is:"+(y*xBlockCount+x));
                	tempMaskRaster.setRect(320*(x-firstTilex), 240*(y-firstTiley), 
                			raster);
                	
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            
            }
        }
        writerRaster(tempMaskImage,tempMaskRaster,unitnum,keyx,keyy,width,height,mask);
      //输出图像
//    	String name ="mask/"+unitnum+".png";
//    	try {
//    		File jpegdata=new File(name);
//    		jpegdata.getParentFile().mkdirs();
//    		FileOutputStream fout =new FileOutputStream(jpegdata);
//			ImageIO.write(tileImageDes[unitnum], "png", fout);
//			fout.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
    }

    //mask图规则标签
    private void writerRaster(BufferedImage tempMaskImage,WritableRaster tempMaskRaster,
    		int num,int keyx,int keyy,int width,int height,int []mask){
		if(tileImageDes[num] != null) {
			return;
		}
		tileImageDes[num] = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		
    	cm = tempMaskImage.getColorModel();
    	
       // data = null;
        //int m=0;
        des = new int [4];
       //System.out.println("keyx,keyy is:"+keyx+","+keyy);
       //System.out.println("width,height is:"+width+","+height);
    	for(int h=0;h<height;h++){
    		for(int w=0;w<width;w++){
    			//System.out.println("rgb is:");
    			//data = totalRaster.getDataElements(keyx+w, keyy+h, null);
    			  //int rgb=cm.getRGB(data);
    			//System.out.println("x,y is:"+(keyx+w)+","+(keyy+h));
    			  data = tempMaskRaster.getDataElements(keyx%320+w,keyy%240+h, null);
    			  int rgb = cm.getRGB(data);
    			  int sr,sg,sb;
    			  sr = (rgb & 0xFF0000)>>16;
    	        sg = (rgb & 0xFF00)>>8;
    	        sb = rgb & 0xFF;
    	        des[0]=sr;
    	        des[1]=sg;
    	        des[2]=sb;
    	        if(mask[h*width+w]==3){
    	        	des[3]=110;
    	        }else if(mask[h*width+w] == 1){
    	        	des[0]=0;
    	        	des[1]=0;
    	        	des[2]=0;
    	        	des[3]=0;
    	        }
    	        else{
    	        	des[3]=0;
    	        }
    	        
    	        //rasterDes[num].setPixel(w, h, des);
    	        WritableRaster raster = tileImageDes[num].getRaster();
    	        raster.setPixel(w, h, des);
    		}
    	}
   	 
   }
   

    @Override
    protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
        // 1.计算Rect落在的图块 
        Point pFirstBlock = viewToBlock(x, y);
        // 2.计算第一块地图相对ViewRect的偏移量,并将Graphics偏移
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        //System.out.println("x,y is:"+dx+","+dy);
        //System.out.println("x,y is:"+pFirstBlock.x+","+pFirstBlock.y);
        g2.translate(dx, dy);
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.计算X轴,Y轴方向需要的地图块数量
         int offsetX = x;
         int offsetY = y;
         int firstTileX = pixelsToTilesw(offsetX);
         int lastTileX = pixelsToTilesw(offsetX + MAP_BLOCK_WIDTH*2);
         int firstTileY = pixelsToTilesh(offsetY);
         int lastTileY = pixelsToTilesh(offsetY + MAP_BLOCK_HEIGHT*3);
         
         lastTileX=Math.min(lastTileX, (int)(provider.getXBlockCount()-1));
         lastTileY=Math.min(lastTileY, (int)provider.getYBlockCount());
         
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.从缓存获取地图块,画到Graphics上
        for (int i = firstTileX; i <= lastTileX; i++) {
            for (int j = firstTileY; j < lastTileY; j++) {
            	//System.out.println("i , j is:"+i+","+j);
                Image b = getBlock(i , j );
            	//Image b = tileImage[j*xBlockCount+i];
                //System.out.println("b width is:"+b.getWidth(null));
                g2.drawImage(b, (i-firstTileX)*MAP_BLOCK_WIDTH, 
                		(j-firstTileY)*MAP_BLOCK_HEIGHT, null);
                
            }
        }
        
		loadMask(x, y);
    }
    
    /**
     * 根据MASK前后数据关系
     * 判断是否drawmask
     */
    public void drawMask(Player player,int pcoordx,int pcoordy, Graphics g,int viewx, int viewy, int offsetX, int offsetY) {
        // 1.计算Rect落在的图块 
        /* Point pFirstBlock = viewToBlock(x, y);
        // 2.计算第一块地图相对ViewRect的偏移量,并将Graphics偏移
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;*/
        //System.out.println("x,y is:"+pFirstBlock.x+","+pFirstBlock.y);
        //g2.translate(dx, dy);
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.计算X轴,Y轴方向需要的地图块数量
        // doDraw已计算
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.从缓存获取地图块,画到Graphics上
      
  
    	//读取player信息及参数；
        int pkeyx   = player.getRefPixelX(); //person RefPixelX
        int pOffsetX = pcoordx- pkeyx;
        int pkeyy   = player.getRefPixelY();//person RefPixelY
        int pOffsetY = pcoordy- pkeyy;
        int pwidth  = player.getWidth();//person width
        int pheight = player.getHeight(); //person height
    	

		int offsetx = viewx;
		int offsety = viewy;
		int firstTileX = pixelsToTilesw(offsetx);
		int lastTileX = pixelsToTilesw(offsetx + MAP_BLOCK_WIDTH*2);
		int firstTileY = pixelsToTilesh(offsety);
		int lastTileY = pixelsToTilesh(offsety + MAP_BLOCK_HEIGHT*3);
		 
		lastTileX=Math.min(lastTileX, (int)(provider.getXBlockCount()-1));
		lastTileY=Math.min(lastTileY, (int)provider.getYBlockCount());
		//System.out.println("firstx,y is:"+firstTileX+","+firstTileY);
		//System.out.println("lastx,y is:"+lastTileX+","+lastTileY);
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
		
		Set<Integer> paintedMaskSet = new HashSet<Integer>();
        // 4.从缓存获取地图块,画到Graphics上
        for (int y = firstTileY; y < lastTileY; y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {
//               int unitnum = (y*xBlockCount+x);
               //System.out.println("unitnum"+ "is:"+unitnum);

        	   MapUnit mapUnit = provider.ReadUnit(x, y);
        	   int[] masknum = mapUnit.getMaskIndexs();
        	   //System.out.println("mapUnit: ("+x+","+y+")"+", maskIndex: "+Arrays.toString(masknum));
        	   for(int i=0;i<masknum.length;i++){
        		   int maskIndex = masknum[i];
        		   //防止重复绘制同一个mask
        		   if(paintedMaskSet.contains(maskIndex)) {
        			   continue;
        		   }
        		   paintedMaskSet.add(maskIndex);
        		   
        		   MaskUnit maskUnit = getMaskUnit(maskIndex);
        		   int []maskdata = maskUnit.getData();
        		   //this.createTileMaskImg(maskIndex, maskUnit.getX(), maskUnit.getY(), maskUnit.getWidth(), 
        			//	   maskUnit.getHeight(), maskdata);
        		   //检测mask是否盖住人物或者NPC
       	           boolean bDrawMask = true;//false;
        		   if(pcoordx<=maskUnit.getX()+maskUnit.getWidth()-1 && pcoordx>=maskUnit.getX()){
        			   bDrawMask = true;
        			   int length = maskUnit.getWidth()*maskUnit.getHeight();
        			   if(maskUnit.getY()+maskUnit.getHeight() < pcoordy ){
        				   for(int l=length-maskUnit.getWidth()-1;l<length;l++){
            				   if(maskdata[l]==1){
            					   bDrawMask=false;
            				   }
            			   }
        			   }else if(maskUnit.getY()+maskUnit.getHeight() > pcoordy){
        				   int row = pcoordy-maskUnit.getY();
        				   for(int l=row*(maskUnit.getWidth()-1);l<row*maskUnit.getWidth();l++){
        					   if(maskdata[l]==1){
            					   bDrawMask=false;
            				   }
        				   }
        			   }
        		   }else if(pcoordx<maskUnit.getX() && pOffsetX+pwidth>maskUnit.getX()){
        			   bDrawMask = true;
        			   int length = maskUnit.getWidth()*maskUnit.getHeight();
        			   if(maskUnit.getY()+maskUnit.getHeight() < pcoordy ){
        				   int col = maskUnit.getX()+maskUnit.getWidth()-(pOffsetX+pwidth);
        				   for(int l=length-maskUnit.getWidth()-1;l<Math.min(length-col, length);l++){
            				   if(maskdata[l]==1){
            					   bDrawMask=false;
            				   }
            			   }
        			   }else if(maskUnit.getY()+maskUnit.getHeight() > pcoordy && pcoordy>maskUnit.getY()){
        				   int col = maskUnit.getX()+maskUnit.getWidth()-(pOffsetX+pwidth);
        				   int row = pcoordy-maskUnit.getY();
        				   for(int l=row*(maskUnit.getWidth()-1);
        						   l<Math.min(row*maskUnit.getWidth()-col,row*maskUnit.getWidth());l++){
        	
        					   if(maskdata[l]==1){
            					   bDrawMask=false;
            				   }
        				   }
        			   }
        			   
        		   }else if(pcoordx>maskUnit.getX()+maskUnit.getWidth()-1 &&
        				   pOffsetX<maskUnit.getX()+maskUnit.getWidth()-1){
        			   bDrawMask = true;
        			   int length = maskUnit.getWidth()*maskUnit.getHeight();
        			   if(maskUnit.getY()+maskUnit.getHeight() < pcoordy ){
        				   int col = maskUnit.getX()+maskUnit.getWidth()-pOffsetX;
        				   for(int l=length-Math.min(col,maskUnit.getWidth());l<length;l++){
            				   if(maskdata[l]==1){
            					   bDrawMask=false;
            				   }
            			   }
        			   }else if(maskUnit.getY()+maskUnit.getHeight() > pcoordy && pcoordy>maskUnit.getY()){
        				   int col = maskUnit.getX()+maskUnit.getWidth()-pOffsetX;
        				   int row = pcoordy-maskUnit.getY();
        				   for(int l=Math.max(row*maskUnit.getWidth()-col, maskUnit.getX());
        						   l<row*maskUnit.getWidth();l++){
        					   if(maskdata[l]==1){
            					   bDrawMask=false;
            				   }
        				   }
        			   }
        			   
        		   }
        		   if(bDrawMask){
            		   g.drawImage(this.tileImageDes[maskIndex], maskUnit.getX()-viewx, 
            				   maskUnit.getY()-viewy,null);
        		   }
               }
            }
        }    
        
    }

    /**
     * 预加载此区域的地图块
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void prepare(int x, int y, int width, int height) {
        // 1.计算Rect落在的图块 
        Point pFirstBlock = viewToBlock(x, y);
        // 2.计算第一块地图相对ViewRect的偏移量,并将Graphics偏移
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.计算X轴,Y轴方向需要的地图块数量
        int xCount = 1 + (width - dx - 1) / MAP_BLOCK_WIDTH;
        int yCount = 1 + (height - dy - 1) / MAP_BLOCK_HEIGHT;
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.缓存此区域的地图块
        Image[][] images = new Image[xCount][yCount];
        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                Image img = getBlock(i + pFirstBlock.x, j + pFirstBlock.y);
                images[i][j] = img;
            }
        }
    }

    private int checkTable() {
        int count = 0;
        int width = this.blockTable.length;
        int height = this.blockTable[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                SoftReference<Image> reference = this.blockTable[i][j];
                if (reference != null && reference.get() != null) {
                    count++;
                }
            }
        }
//        if (count != lastCount) {
//            System.out.printf("map loaded block count: %s \n", count);
//        }
        lastCount = count;
        return count;
    }

    private Image getBlock(int x, int y) {
        SoftReference<Image> reference = this.blockTable[x][y];
        //如果此地图块还没加载,则取地图块数据并生成图像
        //如果GC由于低内存,已释放image,需要重新装载
        if (reference == null || reference.get() == null) {
            reference = new SoftReference<Image>(provider.getBlock(x, y));
            this.blockTable[x][y] = reference;
        }
        this.checkTable();
        return reference.get();
    }

    public int getXBlockCount() {
        return xBlockCount;
    }

    public void setXBlockCount(int blockCount) {
        xBlockCount = blockCount;
    }

    public int getYBlockCount() {
        return yBlockCount;
    }

    public void setYBlockCount(int blockCount) {
        yBlockCount = blockCount;
    }
    
    public int tileswToPixels(int numTiles) {
        int pixelSize = numTiles * MAP_BLOCK_WIDTH ;
        return pixelSize;
    }

    public int pixelsToTilesw(int pixelCoord) {    	
        int numTiles = pixelCoord / MAP_BLOCK_WIDTH ;
        	return numTiles;
        
    }
    
    public int tileshToPixels(int numTiles){
    	int pixelSize = numTiles * MAP_BLOCK_HEIGHT ;
        return pixelSize;
    }
    
    public int pixelsToTilesh(int pixelCoord) {
    	int numTiles = pixelCoord / MAP_BLOCK_HEIGHT ;
    	
    		return numTiles;
    	
        
    }

    /**
     * 计算view坐标vp点对应的地图数据块位置 （即vp点落在哪个地图块上）
     * 
     * @param vp view's top left position
     * @return the map block index of the vp
     */
    private Point viewToBlock(int x, int y) {
        Point p = new Point();
        p.x = x / MAP_BLOCK_WIDTH;
        p.y = y / MAP_BLOCK_HEIGHT;
        if (p.x < 0)
            p.x = 0;
        if (p.y < 0)
            p.y = 0;
        return p;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void dispose() {
        this.provider.dispose();
        this.provider = null;
        for (SoftReference<Image>[] refs : this.blockTable) {
            for (SoftReference<Image> ref : refs) {
                if (ref != null) {
                    ref.clear();
                }
            }
        }
        this.blockTable = null;
    }

    public MapConfig getConfig() {
        return config;
    }

    public void setConfig(MapConfig config) {
        this.config = config;
    }

    public boolean contains(int x, int y) {
        return true;
    }

	public byte[][] getCellData() {
		return cellData;
	}



}
