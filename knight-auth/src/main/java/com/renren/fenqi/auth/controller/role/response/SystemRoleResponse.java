/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: SystemRoleResponse.java
 * @Package com.renren.fenqi.auth.controller.role.response
 * @date 2016-11-10 下午5:31:31 
 */
package com.renren.fenqi.auth.controller.role.response;

import java.util.Date;


/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class SystemRoleResponse{

    private int id;
	
    private String roleId;
	
    private String roleName;
    
    private String creator;
    
    private String creatorName;
	
	private String realm;
	
	private String realmName;
	
    private int status;
	
    private Date createTime;
	
    private Date updateTime;

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public String getRoleId(){
		return roleId;
	}

	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	public String getRoleName(){
		return roleName;
	}

	public void setRoleName(String roleName){
		this.roleName = roleName;
	}

	public String getCreator(){
		return creator;
	}

	public void setCreator(String creator){
		this.creator = creator;
	}

	public String getCreatorName(){
		return creatorName;
	}

	public void setCreatorName(String creatorName){
		this.creatorName = creatorName;
	}

	public String getRealm(){
		return realm;
	}

	public void setRealm(String realm){
		this.realm = realm;
	}

	public String getRealmName(){
		return realmName;
	}

	public void setRealmName(String realmName){
		this.realmName = realmName;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
    
}
