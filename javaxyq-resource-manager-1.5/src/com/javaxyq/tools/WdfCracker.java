/**
 * 
 */
package com.javaxyq.tools;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.javaxyq.util.HashUtil;

/**
 * @author dewitt
 * 
 */
public class WdfCracker {
	
	public static void crack(String[]formats,Object[] values) {
		
	}

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		//crackSound();
		//crackWzife();
		crackScene();
	}
	
	
	private static void crackScene() throws FileNotFoundException {
		WdfFile wdf = new WdfFile("E:/Game/梦幻西游/Scene.wdf");
		PrintWriter pw = new PrintWriter("resources/names/scene1.lst");
		String[] formats = new String[] { "%04d.nav", "%04d.spr","%04d.cell"};
		int max = 9999,iCount = 0;
		for(int i=0;i<max;i++) {
			for (int f = 0; f < formats.length; f++) {
				String res = String.format(formats[f], i);
				long id = HashUtil.stringToId(res);
				if (wdf.findNode(id) != null) {
					System.out.println(res);
					pw.println(res);
					iCount ++;
				}
			}
		}
		pw.close();
		System.out.println();
		System.out.printf("共%d个，匹配%d个",wdf.getFileNodeCount(),iCount);
	}
	
	private static void crackWzife() throws FileNotFoundException {
		WdfFile wdf = new WdfFile("E:/Game/梦幻西游/wzife.wd1");
		PrintWriter pw = new PrintWriter("resources/names/test1.lst");
		String[] formats = new String[] { "magic\\normal\\%04d.tcp","magic\\small\\%04d.tcp" };
		int max = 9999,iCount = 0;
		for(int i=0;i<max;i++) {
			String res = String.format(formats[0], i);
			long id = HashUtil.stringToId(res);
			if (wdf.findNode(id) != null) {
				System.out.println(res);
				pw.println(res);
				iCount ++;
			}
			res = String.format(formats[1], i);
			id = HashUtil.stringToId(res);
			if (wdf.findNode(id) != null) {
				System.out.println(res);
				pw.println(res);
				iCount ++;
			}
		}
		pw.close();
		System.out.println();
		System.out.printf("共%d个，匹配%d个",wdf.getFileNodeCount(),iCount);
	}

	private static void crackSound() throws FileNotFoundException {
		String[] list1 = new String[] { "attack", "magic", "hit", "defend", "die", "rusha", "rushb", "guard", "悲伤",
				"发怒", "亲近", "舞蹈", "休息", "招呼" };
		WdfFile wdf = new WdfFile("E:/Game/梦幻西游/sound1.wdf");
		PrintWriter pw = new PrintWriter("resources/names/test.lst");
		String[] formats = new String[] { "char\\%04d\\%s.wav", "magic\\%04d.wav","scene\\eff%04d.wav" };
		System.out.println("--------- start crack -----------");
		int iCount = 0;
		for (int i = 1; i < 6000; i++) {
			for (int n = 0; n < list1.length; n++) {
				String action = list1[n];
				//匹配1
				String res = String.format(formats[0], i, action);
				long id = HashUtil.stringToId(res);
				if (wdf.findNode(id) != null) {
					System.out.println(res);
					pw.println(res);
					iCount ++;
				}
			}
			//匹配2
			String res = String.format(formats[1], i);
			long id = HashUtil.stringToId(res);
			if (wdf.findNode(id) != null) {
				System.out.println(res);
				pw.println(res);
				iCount ++;
			}
			//匹配3
			res = String.format(formats[2], i);
			id = HashUtil.stringToId(res);
			if (wdf.findNode(id) != null) {
				System.out.println(res);
				pw.println(res);
				iCount ++;
			}
		}
		pw.close();
		System.out.println();
		System.out.printf("共%d个，匹配%d个",wdf.getFileNodeCount(),iCount);
	}

}
