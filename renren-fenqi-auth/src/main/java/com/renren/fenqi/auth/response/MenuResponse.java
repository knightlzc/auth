/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: MemuResponse.java
 * @Package com.renren.fenqi.auth.response
 * @date 2016-11-15 上午11:00:55 
 */
package com.renren.fenqi.auth.response;

import java.util.Date;
import java.util.List;

import com.renren.fenqi.dbtool.annotation.Colum;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description: 菜单响应类 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class MenuResponse{

    private int id;
	
	private String menuTitle;
	
    private int parentId;
	
	private String menuDesc;
	
	private String menuUrl="";
	
	private int sort;
	
	private List<MenuResponse> children;

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public String getMenuTitle(){
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle){
		this.menuTitle = menuTitle;
	}

	public int getParentId(){
		return parentId;
	}

	public void setParentId(int parentId){
		this.parentId = parentId;
	}

	public String getMenuDesc(){
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc){
		this.menuDesc = menuDesc;
	}

	public String getMenuUrl(){
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl){
		this.menuUrl = menuUrl;
	}

	public int getSort(){
		return sort;
	}

	public void setSort(int sort){
		this.sort = sort;
	}

	public List<MenuResponse> getChildren(){
		return children;
	}

	public void setChildren(List<MenuResponse> children){
		this.children = children;
	}
	
}
