/*
 * JavaXYQ Source Code 
 * MetaClassUtil MetaClassUtil.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.util;

/**
 * @author dewitt
 *
 */
public class ClassUtil {
	
	public static void init() {
		String.metaClass.afterLast = {str->
			int p = delegate.lastIndexOf(str)
			return (p>=0&&p<delegate.length()-1)?delegate.substring(p+str.length()):null;
		}
	}
	
	public static void copyFields(Object src,Object dest) {
		if(src && dest) {
			def props = src.getProperties();
			props.remove('class');
			props.remove('metaClass');
			props.each{key,value ->
				dest[key] = value;
			}
		}
	}
	
	
	
	//public static Object clone(Object src){}
	
}
