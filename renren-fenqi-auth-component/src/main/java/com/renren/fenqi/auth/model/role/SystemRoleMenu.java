/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: SystemRoleMenu.java
 * @Package com.renren.fenqi.auth.model.role
 * @date 2016-11-11 下午6:37:28 
 */
package com.renren.fenqi.auth.model.role;

import com.renren.fenqi.dbtool.annotation.Colum;
import com.renren.fenqi.dbtool.annotation.TableName;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description: 角色菜单映射关系 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@TableName(tableName="system_role_menu")
public class SystemRoleMenu{

	@Colum(columName="id")
	private String id;
	
	@Colum(columName="role_id")
	private int roleId;
	
	@Colum(columName="menu_id")
	private int menuId;

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public int getRoleId(){
		return roleId;
	}

	public void setRoleId(int roleId){
		this.roleId = roleId;
	}

	public int getMenuId(){
		return menuId;
	}

	public void setMenuId(int menuId){
		this.menuId = menuId;
	}
}
