/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: ResultResponse.java
 * @Package com.renren.fenqi.auth.response
 * @date 2016-11-11 下午4:51:08 
 */
package com.renren.fenqi.auth.response;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class ResultResponse{
	
	public static boolean SUCCESS = true;
	public static boolean FAILD = false;

	private boolean suc;
	
	private String msg;
	
	private Object data;
	
	public ResultResponse(){
		
	}
	public ResultResponse(boolean suc,String msg){
		this.suc = suc;
		this.msg = msg;
	}
	public ResultResponse(boolean suc,String msg,Object data){
		this.suc = suc;
		this.msg = msg;
		this.data = data;
	}

	public boolean isSuc(){
		return suc;
	}

	public void setSuc(boolean suc){
		this.suc = suc;
	}

	public String getMsg(){
		return msg;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public Object getData(){
		return data;
	}

	public void setData(Object data){
		this.data = data;
	}
	
}
