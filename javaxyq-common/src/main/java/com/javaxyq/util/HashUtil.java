/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author 龚德伟
 * @history 2008-7-14 龚德伟 新建
 */
@Slf4j
public class HashUtil {
    static {
        System.loadLibrary("lib/JStringId");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String str = "1123斩龙决.tcp";
        log.info("stringToId:{}={}", str, Long.toHexString(stringToId(str)));
        log.info("stringToIdAsHex:{}={}", str, stringToIdAsHex(str));
        Map<Long, String> map = createId2PathMap("resources/names/shape.lst");
        Set<Entry<Long, String>> entryset = map.entrySet();
        for (Iterator iterator = entryset.iterator(); iterator.hasNext();) {
            Entry<Long, String> entry = (Entry<Long, String>) iterator.next();
            System.out.printf("%s = %s\n", Long.toHexString(entry.getKey()), entry.getValue());
        }

    }

    public static Map<Long, String> createId2PathMap(String listfile) {
        log.info("load filelist: " + listfile);
        Map<Long, String> map = new HashMap<Long, String>();
        InputStream is = Utils.getResourceAsStream(listfile);
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String strPath = null;
            try {
                while ((strPath = br.readLine()) != null) {
                	strPath = strPath.trim();
                	if(strPath.length()>0) {
                		//log.info("stringToId: "+strPath);
                		map.put(stringToId(strPath), strPath);
                	}
                }
            } catch (Throwable e) {
            	System.err.println("还原文件名列表失败: " + listfile);
            	e.printStackTrace();
            }
        } else {
            System.err.println("读取资源失败: " + listfile);
        }
        return map;
    }

    /**
     * 根据资源名字生成索引id
     * 
     * @param str
     * @return
     */
    public native static long stringToId(String str);

    /**
     * 根据资源名字生成索引id,返回值是hex字符串. 如stringToIdAsHex("1123斩龙决.tcp")="1ee9406c"
     * 
     * @param str
     * @return
     */
    public native static String stringToIdAsHex(String str);

}