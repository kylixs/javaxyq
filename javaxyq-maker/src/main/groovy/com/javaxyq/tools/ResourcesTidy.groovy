/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-30
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools;

import com.javaxyq.util.HashUtil;

/**
 * @author dewitt
 * @date 2009-11-30 create
 */
class ResourcesTidy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		def files = new File('resources/names').listFiles();
		files.each{file ->
			if(!file.isFile())return;
			println file;
			def output = new File(file.getPath().replaceAll('.lst','.cmt'));
			file.eachLine{
				output << HashUtil.stringToIdAsHex(it.trim())<<'='<<it << '\n'
			}
		}

	}

}
