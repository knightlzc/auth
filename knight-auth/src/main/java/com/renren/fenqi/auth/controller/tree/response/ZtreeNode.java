/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: ZtreeNode.java
 * @Package com.renren.fenqi.auth.response
 * @date 2016-11-11 下午3:28:39 
 */
package com.renren.fenqi.auth.controller.tree.response;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class ZtreeNode{

	private String id;
	
	private String pId;
	
	private String name;
	
	private boolean isParent;
	
	private boolean open;
	
	private boolean checked;
	
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public boolean isParent(){
		return isParent;
	}

	public void setParent(boolean isParent){
		this.isParent = isParent;
	}

	public boolean isOpen(){
		return open;
	}

	public void setOpen(boolean open){
		this.open = open;
	}

	public boolean isChecked(){
		return checked;
	}

	public void setChecked(boolean checked){
		this.checked = checked;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
