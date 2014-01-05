/**
 * 
 */
package com.javaxyq.core;

import com.javaxyq.ui.Panel;

/**
 * @author dewitt
 *
 */
public interface DialogBuilder {

	/**
	 * 创建对话框实例
	 * @param id 对话框id
	 * @param res ui资源描述文件
	 * @return
	 */
	Panel createDialog(String id, String res);
}
