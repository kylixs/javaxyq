/**
 * 
 */
package com.javaxyq.tools;

import java.io.File;
import java.util.Map;

/**
 * 资源提取器
 * @author gongdewei
 * @date 2011-8-1 create
 */
public interface Extractor {

	/**
	 * 将FileObject 提取到指定目录
	 * @param fileObject 导出对象
	 * @param dir 存放的目录
	 * @param options 参数
	 */
	void extract(FileObject fileObject, File dir, Map<String, ?> options);
}
