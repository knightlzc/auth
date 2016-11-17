/**
 * 
 */
package com.renren.fenqi.auth.model.menu;

import java.util.Date;

import com.renren.fenqi.dbtool.annotation.Colum;
import com.renren.fenqi.dbtool.annotation.TableName;

/**
 * @author jinshi
 */
@TableName(tableName="system_menu")
public class SystemMenu {
	
	@Colum(columName = "id")
    private int id;
	
	@Colum(columName = "menu_title")
	private String menuTitle;
	
	@Colum(columName = "parent_id")
    private int parentId;
	
	@Colum(columName = "realm")
	private String realm;
	
	@Colum(columName = "menu_desc")
	private String menuDesc;
	
	@Colum(columName = "menu_url")
	private String menuUrl="";
	
	@Colum(columName = "sort")
	private int sort;
	
	@Colum(columName = "update_time")
	private Date updateTime;
	
	@Colum(columName = "create_time")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
