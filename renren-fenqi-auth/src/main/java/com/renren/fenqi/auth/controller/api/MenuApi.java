/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: MenuApi.java
 * @Package com.renren.fenqi.auth.controller.api
 * @date 2016-11-15 上午10:56:35 
 */
package com.renren.fenqi.auth.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.renren.fenqi.auth.Utils.MenuUtils;
import com.renren.fenqi.auth.model.menu.SystemMenu;
import com.renren.fenqi.auth.model.user.SystemUser;
import com.renren.fenqi.auth.repository.menu.SystemMenuRepository;
import com.renren.fenqi.auth.response.MenuResponse;
import com.renren.fenqi.auth.service.menu.SystemMenuService;
import com.renren.fenqi.auth.service.role.SystemRoleService;
import com.renren.fenqi.auth.service.user.SystemUserService;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Controller
@RequestMapping(value="/api/menu")
public class MenuApi{
    
	@Autowired
	private SystemMenuService systemMenuService;
	
	@Autowired
	private SystemUserService sysUerService;
	
	@Autowired
	private SystemRoleService sysRoleService;
	
	@Autowired
	private SystemMenuRepository sysMenuRepository;
	
	/**
	 * 通过登录名和作用域查找菜单集合
	 * @param name 登录名
	 * @param realm 作用域
	 * @return
	 */
	@RequestMapping(value="/getMenuList",method=RequestMethod.GET)
	@ResponseBody
	private List<MenuResponse> buildMenuTree(@RequestParam(value="name") String name,
			@RequestParam(value="realm") String realm){
		
		if(StringUtils.isBlank(name)){
			return null;
		}
		
		if(StringUtils.isBlank(realm)){
			return null;
		}
		
		SystemUser user = sysUerService.getByName(name);
		if(user==null){
			return null;
		}
		
		List<Integer> roles = sysRoleService.getRoleIdsByUserId(user.getId());
		
		List<MenuResponse> menusRes= buildMenuByRoles(roles);
		
		return menusRes;
	}


	private List<MenuResponse> buildMenuByRoles(List<Integer> roles) {
		
		if(roles==null||roles.size()==0){
			return null;
		}
		//通过角色查询菜单Id，并且归类到顶层菜单和子菜单
		List<Integer> menuIdList = new ArrayList<Integer>();
		for(Integer roleId:roles){
			List<Integer> menuIds = systemMenuService.getMenuIdsByRoleId(roleId);
			
			if(menuIdList.size()==0){
				menuIdList.addAll(menuIds);
			}else{
				for(Integer menuId:menuIds){
					if(!menuIdList.contains(menuId)){
						menuIdList.add(menuId);
					}
				}
			}
		}
		//通过筛选出来的MenuId去查找Menu菜单,这边是以parent排序,所以直接把顶层Menu给寻找出来
		List<SystemMenu> menuList =  sysMenuRepository.queryListByid(menuIdList);
		if(menuList==null||menuList.size()==0)
			return null;
		return MenuUtils.menuTree(menuList);
		
	}
}
