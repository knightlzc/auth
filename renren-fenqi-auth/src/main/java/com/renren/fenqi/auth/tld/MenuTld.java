package com.renren.fenqi.auth.tld;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import xce.util.channel.UserChannel;

import com.renren.auth.tools.redis.JedisUtils;
import com.renren.fenqi.auth.Utils.UserUtils;
import com.renren.fenqi.auth.model.user.SystemUser;
import com.renren.fenqi.auth.redisbean.UserCache;
import com.renren.fenqi.auth.service.menu.SystemMenuService;
import com.renren.fenqi.auth.service.user.SystemUserService;

/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description: 控制菜单是否显示 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
public class MenuTld extends BodyTagSupport{
	private static final long serialVersionUID = 2065726141086206736L;

	private String menuId;

	@Override
	public int doAfterBody() throws JspException{
		try{
			HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
			HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
			AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
			String username = principal.getName();
			List<Integer> menuIds = null;
			
			//缓存获取
			UserCache user = (UserCache) JedisUtils.getObject(username);
			//缓存没有，初始化缓存
			if(null == user){
				WebApplicationContext wac = ContextLoader 
						.getCurrentWebApplicationContext(); 
				SystemUserService userService = wac.getBean(SystemUserService.class);
				user = userService.getCacheUser(username);
			}
			//缓存如果挂了，在数据库获取
			if(null == user){
				WebApplicationContext wac = ContextLoader 
						.getCurrentWebApplicationContext(); 
				SystemMenuService menuService = wac.getBean(SystemMenuService.class);
				SystemUserService userService = wac.getBean(SystemUserService.class);
				SystemUser systemUser = userService.getByName(username);
				if(null == systemUser){
					response.sendRedirect("");
					return Tag.SKIP_BODY;
				}
				menuIds = menuService.getMenuIdsByUserId(systemUser.getId());
			}else{
				menuIds = user.getMenuIds();
				
			}
			// 资源menuIds列表(暂时屏蔽，发布时开启)
			
			JspWriter out = this.getBodyContent().getEnclosingWriter();
			if(null != menuIds && menuIds.contains(Integer.parseInt(menuId))){
				out.write(this.getBodyContent().getString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return super.doAfterBody();
	}

	@Override
	public int doEndTag() throws JspException{
		return EVAL_PAGE;
	}

	public String getMenuId(){
		return menuId;
	}

	public void setMenuId(String menuId){
		this.menuId = menuId;
	}

}
