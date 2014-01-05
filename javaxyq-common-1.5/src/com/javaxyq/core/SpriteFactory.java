package com.javaxyq.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import com.javaxyq.io.CacheManager;
import com.javaxyq.util.WASDecoder;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Sprite;

/**
 * Sprite 工厂类<br>
 * 
 * @author Langlauf
 * @date
 */
public class SpriteFactory {

	/**
	 * 动画播放每帧的间隔(ms)
	 */
	public static final int ANIMATION_INTERVAL = 100;
	/** <sprite id, sprite instance> */
	// private static Map<String, Sprite> sprites = new WeakHashMap<String,
	// Sprite>();

	public static Sprite loadCursor(String filename) {
		return loadSprite("/resources/cursor/" + filename);
	}

	/**
	 * 获取一个角色资源
	 * @param id 角色Id
	 * @param action 角色的动作
	 * @return
	 */
	public static Sprite getSprite(String id, String action) {
		Sprite sprite = loadSprite("shape/char/"+id+"/"+action+".tcp", null);
		sprite.setResId(id+"-"+action);
		return sprite;
	}
	
	public static Sprite loadSprite(String filename) {
		return loadSprite(filename, null);
	}

	public static Sprite createSprite(InputStream is) throws Exception {
		WASDecoder decoder = new WASDecoder();
		decoder.load(is);
		return createSprite(decoder);
	}

	private static Sprite createSprite(WASDecoder decoder) {
		int centerX, centerY;
		centerX = decoder.getRefPixelX();
		centerY = decoder.getRefPixelY();
		Sprite sprite = new Sprite(decoder.getWidth(), decoder.getHeight(), centerX, centerY);
		int spriteCount = decoder.getAnimCount();
		int frameCount = decoder.getFrameCount();
		for (int i = 0; i < spriteCount; i++) {
			Animation anim = new Animation();
			anim.setWidth(decoder.getWidth());
			anim.setHeight(decoder.getHeight());
			for (int j = 0; j < frameCount;) {
				try {
					int index = i * frameCount + j;
					BufferedImage frame = decoder.getFrame(index);
					int delay = decoder.getDelay(index);
					int duration = delay * ANIMATION_INTERVAL;
					anim.addFrame(frame, duration, centerX, centerY);
					j += delay;
				} catch (Exception e) {
					if (e instanceof IndexOutOfBoundsException) {
						System.err.println("解析精灵出错：frameCount大于实际值  " + frameCount + " > " + anim.getFrames().size());
						break;
					}
					System.err.println("解析精灵资源文件失败！");
					e.printStackTrace();
					j++;
				}
			}
			sprite.addAnimation(anim);
		}
		sprite.setDirection(0);
		return sprite;
	}

	public static Animation loadAnimation(String filename, int index) {
		Sprite s = loadSprite(filename);
		return (s == null) ? null : s.getAnimation(index);
	}

	public static Animation loadAnimation(String filename) {
		return loadAnimation(filename, 0);
	}

	public static Image loadImage(String filename) {
		if (filename.endsWith(".was")||filename.endsWith(".tcp")) {
			Sprite s = loadSprite(filename);
			return (s == null) ? null : s.getImage();
		}
		return Toolkit.createImageFromResource(filename);
	}

	/**
	 * 加载阴影的精灵
	 * 
	 * @return
	 */
	public static Sprite loadShadow() {
		return loadSprite("/shape/char/shadow.tcp");
	}

	public static Sprite loadSprite(String filename, int[] colorations) {
		if (filename == null || filename.trim().length()==0)
			return null;
		try {
			WASDecoder decoder = new WASDecoder();
			File file = CacheManager.getInstance().getFile(filename);
			if(file == null || !file.exists()) {
				System.err.println("Warning: 找不到精灵的资源文件!"+filename);
				return null;
			}
			decoder.load(file);
			if(colorations!=null) {
				String pp = filename.replaceFirst("(\\w)*.tcp", "00.pp");
				System.out.println("pp: "+pp);//XXX
				decoder.loadColorationProfile(pp);
				decoder.coloration(colorations);
			}
			Sprite s = createSprite(decoder);
			if(colorations!=null) {
				s.setColorations(colorations);
			}
			return s;
		} catch (Exception e) {
			System.err.println("加载精灵失败:" + filename);
			e.printStackTrace();
		}
		return null;
	}

}
