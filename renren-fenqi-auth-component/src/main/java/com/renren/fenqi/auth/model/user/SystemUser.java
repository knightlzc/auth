/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: PfUser.java
 * @Package com.renren.fenqi.auth.model.user
 * @date 2016-11-10 下午5:43:04 
 */
package com.renren.fenqi.auth.model.user;

import java.io.Serializable;
import java.util.Date;

import com.renren.fenqi.dbtool.annotation.Colum;
import com.renren.fenqi.dbtool.annotation.TableName;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@TableName(tableName="system_user")
public class SystemUser implements Serializable{

	/**
	 * <p>Description: </p>
	 * <p>Author:zcliu/刘子萃</p>
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 7877929319611082788L;

	@Colum(columName="id")
    private int id;
	
	@Colum(columName="name")
    private String name;
	
	@Colum(columName="password")
    private String password;
	
	@Colum(columName="nick_name")
    private String nickName;
	
	@Colum(columName="register_time")
    private Date registerTime;
   
    /**
     * 密码更新时间
     */
	@Colum(columName="pwd_update_time")
    private Date pwdUpdateTime;
    
	@Colum(columName="status")
    private int status=USEFUL;
	
    public final static int USEFUL = 0;//有效的
    
    public final static int DELETE = 1;//无效的

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
	
}
