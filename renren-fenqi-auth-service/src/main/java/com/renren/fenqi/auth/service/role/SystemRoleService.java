/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: SystemRoleService.java
 * @Package com.renren.fenqi.auth.service.role
 * @date 2016-11-9 下午7:03:29 
 */
package com.renren.fenqi.auth.service.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renren.fenqi.auth.model.role.SystemRole;
import com.renren.fenqi.auth.model.role.SystemRoleMenu;
import com.renren.fenqi.auth.model.role.SystemUserRole;
import com.renren.fenqi.auth.repository.role.RoleMenuRepository;
import com.renren.fenqi.auth.repository.role.SystemRoleRepository;
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
public class SystemRoleService{
	
	@Autowired
	private SystemRoleRepository systemRoleRepository;
	
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
	public Page<SystemRole> page(int pageNum,int pageSize,Map<String,Object> andParam){
		Page<SystemRole> page = systemRoleRepository.page(SystemRole.class,andParam,new SortPage(pageNum,pageSize,"create_time",SortPage.DESC));
		return page;
	}
	public List<SystemRole> listAll(){
		return systemRoleRepository.list(SystemRole.class, null, new SortPage("create_time", SortPage.DESC));
	}
	
	public List<Integer> getRoleIdsByUserId(int userId){
		Map<String, Object> param = new HashMap<>();
		param.put("user_id", userId);
		return userRoleRepository.getRoleIdsByUserId(userId);
	}
	public SystemRole getById(String roleId){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("role_id",roleId);
		SystemRole role = systemRoleRepository.single(SystemRole.class,param);
		return role;
	}
	
	/**
	 * <br/>Description:保存
	 * <p>Author:zcliu/刘子萃</p>
	 * @param role
	 * @return
	 */
	public int saveRole(SystemRole role){
		return systemRoleRepository.insert(role);
	}
	
	@Transactional("authTxManager")
	public int saveRoleMenu(int roleId,List<Integer> menuIds){
		try{
			roleMenuRepository.deleteByRoleId(roleId);
			for(int menuId:menuIds){
				SystemRoleMenu rm = new SystemRoleMenu();
				rm.setId(UUID.randomUUID().toString().toLowerCase());
				rm.setRoleId(roleId);
				rm.setMenuId(menuId);
				roleMenuRepository.insert(rm);
			}
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	@Transactional("authTxManager")
	public int saveUserRole(int userId,List<Integer> roleIds){
		try{
			userRoleRepository.deleteByUserId(userId);
			for(int roleId:roleIds){
				SystemUserRole ur = new SystemUserRole();
				ur.setId(UUID.randomUUID().toString().toLowerCase());
				ur.setRoleId(roleId);
				ur.setUserId(userId);
				userRoleRepository.insert(ur);
			}
			return 1;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void main(String[] args){
		System.out.println(UUID.randomUUID().toString().toLowerCase());
	}
}
