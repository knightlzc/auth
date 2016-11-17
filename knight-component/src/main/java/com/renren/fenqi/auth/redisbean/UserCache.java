/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: SystemUserInfo.java
 * @Package com.renren.fenqi.auth.response
 * @date 2016-11-16 上午11:12:01 
 */
package com.renren.fenqi.auth.redisbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description: 用户信息缓存类 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class UserCache implements Serializable{

	public static final String CACHE_PREFIX = "SYSTEM_USER_CACHE_";
	private static final long serialVersionUID = - 1214958606951128637L;
	
    private int id;
	
    private String name;
	
    private String password;
	
    private String nickName;
	
    private Date registerTime;
   
    /**
     * 密码更新时间
     */
    private Date pwdUpdateTime;
    
    private int status;
    
    private List<Integer> menuIds;
    
    private List<Integer> roleIds;

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getNickName(){
		return nickName;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public Date getRegisterTime(){
		return registerTime;
	}

	public void setRegisterTime(Date registerTime){
		this.registerTime = registerTime;
	}

	public Date getPwdUpdateTime(){
		return pwdUpdateTime;
	}

	public void setPwdUpdateTime(Date pwdUpdateTime){
		this.pwdUpdateTime = pwdUpdateTime;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public List<Integer> getMenuIds(){
		return menuIds;
	}

	public void setMenuIds(List<Integer> menuIds){
		this.menuIds = menuIds;
	}

	public List<Integer> getRoleIds(){
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds){
		this.roleIds = roleIds;
	}

}
