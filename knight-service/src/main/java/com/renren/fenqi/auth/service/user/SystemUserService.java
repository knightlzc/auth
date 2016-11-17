/**
 * 
 */
package com.renren.fenqi.auth.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.auth.tools.redis.JedisUtils;
import com.renren.fenqi.auth.model.user.SystemUser;
import com.renren.fenqi.auth.redisbean.UserCache;
import com.renren.fenqi.auth.repository.role.RoleMenuRepository;
import com.renren.fenqi.auth.repository.role.UserRoleRepository;
import com.renren.fenqi.auth.repository.user.SystemUserRepository;
import com.renren.fenqi.dbtool.common.SortPage;
import com.renren.fenqi.dbtool.page.Page;

/**
 * @author knight
 *
 */
@Service
public class SystemUserService {
	
	@Autowired
	private SystemUserRepository systemUserRepository;
	
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
	public Page<SystemUser> page(int pageNum,int pageSize,Map<String,Object> andParam){
		Page<SystemUser> page = systemUserRepository.page(SystemUser.class,andParam,new SortPage(pageNum,pageSize,"register_time",SortPage.DESC));
		return page;
	}
	public SystemUser getByName(String name){
		Map<String, Object> param = new HashMap<>();
		param.put("name", name);
		return systemUserRepository.single(SystemUser.class, param);
	}

	public int saveUser(SystemUser user) {
		return systemUserRepository.insert(user);
	}
	
	public UserCache getCacheUser(String name){
		UserCache userCache = (UserCache) JedisUtils.getObject(UserCache.CACHE_PREFIX+name);
		if(null == userCache){
			userCache = cacheUser(name,null);
		}
		return userCache;
	}
	/**
	 * <br/>Description:缓存用户
	 * <p>Author:zcliu/刘子萃</p>
	 * @param userId(如果user不为null，则该参数不起作用)
	 * @param user
	 * @return
	 */
	public UserCache cacheUser(String name,SystemUser user){
		Map<String, Object> param = new HashMap<>();
		param.put("name", name);
		if(null == user){
			user = systemUserRepository.single(SystemUser.class, param);
		}else{
			name = user.getName();
		}
		UserCache userCache = new UserCache();
		userCache.setId(user.getId());
		userCache.setName(user.getName());
		userCache.setNickName(user.getNickName());
		userCache.setPwdUpdateTime(user.getPwdUpdateTime());
		userCache.setRegisterTime(user.getRegisterTime());
		userCache.setStatus(user.getStatus());
		
		List<Integer> roleIds = userRoleRepository.getRoleIdsByUserId(user.getId());
		userCache.setRoleIds(roleIds);
		
		List<Integer> menuIds = roleMenuRepository.getMenuIdsByRoleIds(roleIds);
		
		userCache.setMenuIds(menuIds);
		JedisUtils.addObject(UserCache.CACHE_PREFIX+userCache.getId(),userCache,3600);
		return userCache;
		
	}
}
