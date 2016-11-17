package com.renren.fenqi.auth.controller.role;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.renren.fenqi.auth.Utils.UserUtils;
import com.renren.fenqi.auth.model.role.SystemRole;
import com.renren.fenqi.auth.model.user.SystemUser;
import com.renren.fenqi.auth.response.ResultResponse;
import com.renren.fenqi.auth.service.menu.SystemMenuService;
import com.renren.fenqi.auth.service.role.SystemRoleService;
import com.renren.fenqi.dbtool.page.Page;
import com.renren.fenqi.dbtool.page.PagingUtil;

/**
 * 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Controller
@RequestMapping("/role")
public class RoleController {

	private int pageSize = 10;
	
	@Autowired
	private SystemRoleService systemRoleService;
	
	@RequestMapping("/roleListPage")
	public String gotoRolePage(){
		return "role/role_list_page";
	}
	
	@RequestMapping(value="/loadRoleList",method=RequestMethod.GET)
	public String listRoles(Model model,@RequestParam(value="pageNum",required=false) Integer pageNum){
		if(null == pageNum || pageNum<=0){
			pageNum = 1;
		}
		Page<SystemRole> responsePage = systemRoleService.page(pageNum,pageSize,null);
		
		if (null != responsePage) {
	        List<Integer> paginationList = PagingUtil.getPaginationList(responsePage, 5);
	        model.addAttribute("paginationList", paginationList);
	        model.addAttribute("contents", responsePage.getContent());
	        model.addAttribute("pageNum", responsePage.getPageNum());
	        model.addAttribute("pageSize", responsePage.getPageSize());
	        model.addAttribute("totalNum", responsePage.getTotalNum());
	        model.addAttribute("totalPage", responsePage.getTotalPages());
		}
		return "role/table/role_table";
	}
	
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse saveRole(HttpServletRequest request,
			@RequestParam("roleId") String roleId,
			@RequestParam("roleName") String roleName,
			@RequestParam("realm") String realm){
		Map<String,Object> jb = new HashMap<>();
		SystemUser curUser = UserUtils.getCurrentUser(request);
		if(null == curUser){
			return new ResultResponse(false,"没检测到登录用户");
		}
		SystemRole role = new SystemRole();
		role.setRoleId(StringUtils.trim(roleId));
		role.setRoleName(StringUtils.trim(roleName));
		role.setStatus(SystemRole.STATUS_USER);
		role.setRealm(realm);
		role.setCreator(curUser.getName());
		String checkMsg = this.checkRole(role);
		if(StringUtils.isNotBlank(checkMsg)){
			jb.put("suc", false);
			jb.put("msg", checkMsg);
			return new ResultResponse(false,checkMsg);
		}
		int id = systemRoleService.saveRole(role);
		if(id>0){
			jb.put("suc", true);
			jb.put("msg", "保存成功");
			return new ResultResponse(true,"保存成功");
			
		}else{
			return new ResultResponse(false,"保存失败");
		}
	}
	
	@RequestMapping(value="/saveRoleMenu",method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse saveRoleMenu(
			@RequestParam("roleId") int roleId,
			@RequestParam("menuIds") String menuIds){
			System.out.println(menuIds);
//		Type type = new TypeToken<List<Integer>>(){}.getType();	
		List<Integer> ids = JSONObject.parseArray(menuIds,Integer.class);
		int i = systemRoleService.saveRoleMenu(roleId,ids);
		if(i>0){
			return new ResultResponse(ResultResponse.SUCCESS,"保存成功");
			
		}else{
			return new ResultResponse(ResultResponse.FAILD,"保存成功");
		}
	}
	
	private String checkRole(SystemRole role){
		if(StringUtils.isBlank(role.getRoleId())){
			return "角色id为空";
		}
		if(StringUtils.isBlank(role.getRoleName())){
			return "角色名为空";
		}
		if(StringUtils.isBlank(role.getRealm())){
			return "作用域为空";
		}
		return "";
	}
	
	public static void main(String[] args) {
		Date date = new Date();
		Map<String,Object> map = new HashMap<>();
		map.put("date", date);
		System.out.println(map.get("date") instanceof Date);
	}
}
