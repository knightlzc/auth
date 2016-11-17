/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: RedisTest.java
 * @Package com.renren.redis
 * @date 2016-11-15 下午7:08:55 
 */
package com.renren.redis;

import com.alibaba.fastjson.JSONObject;
import com.renren.auth.tools.redis.JedisUtils;
import com.renren.fenqi.auth.model.user.SystemUser;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class RedisTest{

	public static void main(String[] args){
		SystemUser user = new SystemUser();
		user.setId(2);
		user.setName("范总");
		user.setNickName("范范");
//		System.out.println(JedisUtils.serialize("fff",user));
		System.out.println(JSONObject.toJSON(JedisUtils.getObject("ffsf")));
//		System.out.println(JedisUtils.add("f","fffff"));
	}
}
