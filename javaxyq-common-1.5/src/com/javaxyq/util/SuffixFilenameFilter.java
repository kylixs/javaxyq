/**
 * 
 */
package com.javaxyq.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 后缀名文件过滤器
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class SuffixFilenameFilter implements FilenameFilter {

	private String suffix;
	
	public SuffixFilenameFilter(String suffix) {
		super();
		this.suffix = suffix;
	}

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith(suffix);
	}

}
