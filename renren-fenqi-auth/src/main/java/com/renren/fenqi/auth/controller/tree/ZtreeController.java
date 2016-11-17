/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: ZtreeController.java
 * @Package com.renren.fenqi.auth.controller.tree
 * @date 2016-11-11 下午3:42:36 
 */
package com.renren.fenqi.auth.controller.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.renren.fenqi.auth.controller.tree.response.ZtreeNode;
import com.renren.fenqi.auth.model.menu.SystemMenu;
import com.renren.fenqi.auth.model.realm.Realm;
import com.renren.fenqi.auth.model.role.SystemRole;
import com.renren.fenqi.auth.model.role.SystemUserRole;
import com.renren.fenqi.auth.response.ResultResponse;
import com.renren.fenqi.auth.service.menu.SystemMenuService;
import com.renren.fenqi.auth.service.realm.RealmService;
import com.renren.fenqi.auth.service.role.SystemRoleService;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description: ztree树控制器 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Controller
@RequestMapping("/ztree")
public class ZtreeController{
	
	@Autowired
	private SystemMenuService systemMenuService; 
	
	@Autowired
	private SystemRoleService systemRoleService;
	
	@Autowired
	private RealmService realmService;
	
	@RequestMapping(value="/loadMenus",method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse loadMenus(int roleId){
		try{
			
			List<SystemMenu> list = systemMenuService.listAll();
			List<ZtreeNode> nodes = this.buildMenuTree(roleId,list);
			if(null == nodes){
				return new ResultResponse(ResultResponse.FAILD,"没有菜单数据",nodes);
			}
			return new ResultResponse(ResultResponse.SUCCESS,"查询成功",nodes);
		}catch(Exception e){
			
			return new ResultResponse(ResultResponse.FAILD,"系统异常");
		}
		
	}
	
	@RequestMapping(value="/loadRoles",method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse loadRoles(int userId){
		try{
			
			List<SystemRole> list = systemRoleService.listAll();
			List<ZtreeNode> nodes = this.buildRoleTree(userId, list);
			if(null == nodes){
				return new ResultResponse(ResultResponse.FAILD,"没有角色数据",nodes);
			}
			return new ResultResponse(ResultResponse.SUCCESS,"查询成功",nodes);
		}catch(Exception e){
			
			return new ResultResponse(ResultResponse.FAILD,"系统异常");
		}
		
	}
	
	private List<ZtreeNode> buildMenuTree(int roleId,List<SystemMenu> menus){
		if(null == menus || menus.size()<=0){
			return null;
		}
		List<ZtreeNode> nodes = new ArrayList<>();
		List<Integer> menuIds = systemMenuService.getMenuIdsByRoleId(roleId);
		for(SystemMenu menu:menus){
			ZtreeNode node = new ZtreeNode();
			node.setId(menu.getId()+"");
			node.setName(menu.getMenuTitle());
			node.setpId(menu.getParentId()+"");
			if(null != menuIds && menuIds.size()>0 && menuIds.contains(menu.getId())){
				node.setOpen(true);
				node.setChecked(true);
//				menuIds.remove(menu.getId()+"");
			}
			nodes.add(node);
		}
		return nodes;
	}
	private List<ZtreeNode> buildRoleTree(int userId,List<SystemRole> roles){
		if(null == roles || roles.size()<=0){
			return null;
		}
		List<ZtreeNode> nodes = new ArrayList<>();
		List<Integer> roleIds = systemRoleService.getRoleIdsByUserId(userId);
		List<Realm> realms = realmService.getAllRealm();
		Map<String,ZtreeNode> realmMap = new HashMap<>();
		for (Realm realm : realms) {
			ZtreeNode node = new ZtreeNode();
			node.setId(realm.getRealmId());
			node.setName(realm.getName());
			node.setpId("");
			nodes.add(node);
			realmMap.put(realm.getRealmId(), node);
		}		
		for(SystemRole role:roles){
			ZtreeNode node = new ZtreeNode();
			node.setId(role.getId()+"");
			node.setName(role.getRoleName());
			node.setpId(role.getRealm());
			if(null != roleIds && roleIds.size()>0 && roleIds.contains(role.getId())){
				node.setOpen(true);
				node.setChecked(true);
				realmMap.get(role.getRealm()).setChecked(true);
//				menuIds.remove(menu.getId());
			}
			nodes.add(node);
		}
		return nodes;
	}
}
