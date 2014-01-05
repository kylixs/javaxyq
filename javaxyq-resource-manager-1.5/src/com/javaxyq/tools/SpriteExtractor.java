/**
 * 
 */
package com.javaxyq.tools;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.javaxyq.util.Utils;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Frame;
import com.javaxyq.widget.Sprite;

/**
 * 精灵提取器
 * @author gongdewei
 * @date 2011-8-13 create
 */
public class SpriteExtractor implements Extractor {

	private static final Logger log = Logger.getLogger(SpriteExtractor.class.getName());
	
	@Override
	public void extract(FileObject fileObject, File dir, Map<String, ?> options) {
		String type = fileObject.getContentType();
		if (!FileObject.TCP_FILE.equals(type)) {
			log.warning("导出精灵终止，文件类型不是："+FileObject.TCP_FILE);
			return;
		}
		if(!dir.exists()) {
			dir.mkdirs();
		}
		log.info("导出精灵："+fileObject.getPath()+" ...");
		//判断是否修整图片
		boolean  isTrim = "true".equalsIgnoreCase((String) options.get("trim_image"));
		
		Sprite sprite = XYQTools.createSprite(fileObject);
		int animCount = sprite.getAnimationCount();
		int frameCount = sprite.getAnimation(0).getFrameCount();
		SpriteDescriptor descriptor = new SpriteDescriptorImpl(animCount, frameCount);
		for (int animIndex = 0; animIndex < animCount; animIndex++) {
			Animation anim = sprite.getAnimation(animIndex);
			Vector<Frame> frames = anim.getFrames();
			for (int frameIndex = 0; frameIndex < frames.size(); frameIndex++) {
				Frame frame = frames.get(frameIndex);
				descriptor.setFrameWidth(animIndex,frameIndex, anim.getWidth());
				descriptor.setFrameHeight(animIndex,frameIndex, anim.getHeight());
				descriptor.setFrameRefPixel(animIndex,frameIndex, frame.getRefPixelX(), frame.getRefPixelY());
				saveImage(frame, animIndex, frameIndex, dir, descriptor, isTrim);
				log.fine("已导出帧："+animIndex+"-"+frameIndex);
			}
			log.info("已导出动画："+animIndex);
		}
		//保存精灵描述
		saveDescriptor(dir, descriptor);
		log.info("成功导出精灵到目录："+dir.getAbsolutePath());
	}

	private void saveDescriptor(File dir, SpriteDescriptor descriptor) {
		try {
			String encodeDescriptor = descriptor.encode();
			File file = new File(dir, "des.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(encodeDescriptor.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveImage(Frame frame, int animIndex, int frameIndex, File dir, SpriteDescriptor descriptor, boolean isTrim) {
		//BufferedImage tmpImage = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
		//Graphics2D g2d = tmpImage.createGraphics();
		//g2d.setComposite(AlphaComposite.SrcOver);
		//g2d.drawImage(frame.getImage(), 0, 0, null);
		String filename = animIndex+"-"+frameIndex+".png";
		BufferedImage image = (BufferedImage) frame.getImage();
		if(isTrim) {
			image = trimImage(image, descriptor, animIndex, frameIndex);
		}
		Utils.writeImage(image, new File(dir, filename), "png");
		//tmpImage.flush();
		//g2d.dispose();
	}

	private BufferedImage trimImage(BufferedImage source, SpriteDescriptor descriptor, int animIndex, int frameIndex) {
		int minX = 0, minY = 0;
		int maxX = source.getWidth()-1;
		int maxY = source.getHeight()-1;
		while( minX < maxX && isTransparentX(source, minX)) {
			minX ++;
		}
		while( maxX > minX && isTransparentX(source, maxX)) {
			maxX --;
		}
		while( minY < maxY && isTransparentY(source, minY)) {
			minY ++;
		}
		while( maxY > minY && isTransparentY(source, maxY)) {
			maxY --;
		}
		if(minX!=0 || minY!=0 || maxX!=source.getWidth()-1 || maxY!=source.getHeight()-1) {
			log.fine("trim image to: ("+minX+","+minY+","+maxX+","+maxY+")");
			Point refPixel = descriptor.getFrameRefPixel(animIndex, frameIndex);
			refPixel.x -= minX;
			refPixel.y -= minY;
			descriptor.setFrameRefPixel(animIndex, frameIndex, refPixel.x, refPixel.y);
			descriptor.setFrameWidth(animIndex, frameIndex, maxX-minX);
			descriptor.setFrameHeight(animIndex, frameIndex, maxY-minY);
		}
		return source.getSubimage(minX, minY, maxX-minX, maxY-minY);
	}

	private boolean isTransparentX(BufferedImage source, int x) {
		int h = source.getHeight();
		for (int i = 0; i < h; i++) {
			if(source.getRGB(x, i)!=0) {
				return false;
			}
		}
		return true;
	}

	private boolean isTransparentY(BufferedImage source, int y) {
		int w = source.getWidth();
		for (int i = 0; i < w; i++) {
			if(source.getRGB(i,y)!=0) {
				return false;
			}
		}
		return true;
	}
	
}
