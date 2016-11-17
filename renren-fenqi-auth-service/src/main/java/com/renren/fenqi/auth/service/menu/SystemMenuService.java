/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: SystemRoleService.java
 * @Package com.renren.fenqi.auth.service.role
 * @date 2016-11-9 下午7:03:29 
 */
package com.renren.fenqi.auth.service.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.fenqi.auth.model.menu.SystemMenu;
import com.renren.fenqi.auth.repository.menu.SystemMenuRepository;
import com.renren.fenqi.auth.repository.role.RoleMenuRepository;
import com.renren.fenqi.auth.repository.role.UserRoleRepository;
import com.renren.fenqi.dbtool.common.SortPage;
import com.renren.fenqi.dbtool.page.Page;


/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Service
public class SystemMenuService{
	
	@Autowired
	private SystemMenuRepository systemMenuRepository;
	
	@Autowired
	private RoleMenuRepository roleMenuRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	/**
	 * <br/>Description:分页查询
	 * <p>Author:zcliu/刘子萃</p>
	 * @param pageNum
	 * @param pageSize
	 * @param andParam
	 * @return
	 */
	public Page<SystemMenu> page(int pageNum,int pageSize,Map<String,Object> andParam){
		Page<SystemMenu> page = systemMenuRepository.page(SystemMenu.class,andParam,new SortPage(pageNum,pageSize,"sort",SortPage.ASC));
		return page;
	}
	
	

	public int saveMenu(SystemMenu menu) {
		
		return systemMenuRepository.insert(menu);
	}
	
    public int updateMenu(SystemMenu menu) {
    	
		return systemMenuRepository.update(menu);
	}



	public SystemMenu queryMenu(int id) {
		
		SystemMenu  menu = systemMenuRepository.queryByid(id);
		return menu;
	}
	
	/**
	 * <br/>Description:加载全部
	 * <p>Author:zcliu/刘子萃</p>
	 * @return
	 */
	public List<SystemMenu> listAll() {
		List<SystemMenu> list = roleMenuRepository.list(SystemMenu.class,null,new SortPage("sort",SortPage.ASC));
		return list;
	}
	
	public List<SystemMenu> listAllByRealm(String realm) {
		Map<String,Object> param = new HashMap<>();
		param.put("realm",realm);
		List<SystemMenu> list = roleMenuRepository.list(SystemMenu.class,param,new SortPage("sort",SortPage.ASC));
		return list;
	}
	
	public List<Integer> getMenuIdsByRoleId(int roleId){
		try {
			
			List<Integer> menuIds = roleMenuRepository.getMenuIdsByRoleId(roleId);
			return menuIds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Integer> getMenuIdsByUserId(int userId){
		try {
			List<Integer> roleIds = userRoleRepository.getRoleIdsByUserId(userId);
			List<Integer> menuIds = roleMenuRepository.getMenuIdsByRoleIds(roleIds);
			return menuIds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
