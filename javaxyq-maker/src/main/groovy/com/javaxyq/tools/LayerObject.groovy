/*
 * JavaXYQ Source Code 
 * LayerObject LayerObject.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.tools;

import com.javaxyq.util.StringUtils;

//图层列表对象
public class LayerObject {
	public Node node;
	public LayerObject(Node node){
		this.node = node;
	}
	public String toString() {
		StringBuilder sb = new  StringBuilder();
		if(node.name() != 'Dialog') sb <<'　';
		sb <<'['<<node.name() << ']'
		if(node.@name) {
			sb << node.@name 
		}else if(node.@id) {
			//println "${node.@id}  => ${node.@id.afterLast('com.javaxyq.action.dialog.') ?: node.@id}"
			sb << ( StringUtils.afterLast(node.@id,'com.javaxyq.action.dialog.')?:node.@id.afterLast('com.javaxyq.action.') ?: node.@id)
		}
		sb << ' - '
		if(node.@path) {
			sb << node.@path.afterLast("/")
		}else if(node.@was) {
			sb << node.@was.afterLast("/")
		}else if(node.@text) {
			sb << node.@text
		}
		return sb.toString()
	}
}
