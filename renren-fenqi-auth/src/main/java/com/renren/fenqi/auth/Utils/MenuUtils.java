/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: MenuUtils.java
 * @Package com.renren.fenqi.auth.Utils
 * @date 2016-11-15 上午11:56:08 
 */
package com.renren.fenqi.auth.Utils;

import java.util.ArrayList;
import java.util.List;

import com.renren.fenqi.auth.model.menu.SystemMenu;
import com.renren.fenqi.auth.response.MenuResponse;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description: 菜单工具类 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class MenuUtils{

	public static List<MenuResponse> menuTree(List<SystemMenu> menus){
		List<MenuResponse> responses = new ArrayList<MenuResponse>();
		for(SystemMenu systemMenu:menus){
			if(systemMenu.getParentId()==0){
				MenuResponse response = beanToResponse(systemMenu);
				response.setChildren(setChildMenu(systemMenu.getId(),menus));
				responses.add(response);
			}
		}
		return responses;
	}
	
	public static List<MenuResponse> setChildMenu(int id,List<SystemMenu> menus){
		List<MenuResponse> children = new ArrayList<MenuResponse>();
		for(SystemMenu systemMenu:menus){
			if(id==systemMenu.getParentId()){
				MenuResponse response = beanToResponse(systemMenu);
				response.setChildren(setChildMenu(systemMenu.getId(),menus));
				children.add(response);
			}
		}
		return children;
	}
	
	public static MenuResponse beanToResponse(SystemMenu systemMenu){
		MenuResponse response = new MenuResponse();
		response.setId(systemMenu.getId());
		response.setMenuDesc(systemMenu.getMenuDesc());
		response.setMenuTitle(systemMenu.getMenuTitle());
		response.setMenuUrl(systemMenu.getMenuUrl());
		response.setSort(systemMenu.getSort());
		return response;
	}
}
