/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: UserUtils.java
 * @Package com.renren.fenqi.auth.Utils
 * @date 2016-11-14 下午6:07:21 
 */
package com.renren.fenqi.auth.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.renren.fenqi.auth.model.user.SystemUser;
import com.renren.fenqi.auth.repository.user.SystemUserRepository;
import com.renren.fenqi.dbtool.common.RepositoryFactory;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
public class UserUtils{
	

	/**
	 * <br/>Description:获取当前登录用户
	 * <p>Author:zcliu/刘子萃</p>
	 * @param request
	 * @return
	 */
	public static SystemUser getCurrentUser(HttpServletRequest request){
		AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
		String username = principal.getName();
		WebApplicationContext wac = ContextLoader 
                .getCurrentWebApplicationContext(); 
		SystemUserRepository bean = wac.getBean(SystemUserRepository.class); 
		Map<String,Object> param = new HashMap<>();
		if(username==null || "".equals(username)){
			return null;
		}
		param.put("name",username);
		return bean.single(SystemUser.class,param);
	}
}
