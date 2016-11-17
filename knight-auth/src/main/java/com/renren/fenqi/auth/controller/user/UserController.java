/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description: 用户管理控制器 </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: UserController.java
 * @Package com.renren.fenqi.auth.controller.user
 * @date 2016-11-10 下午5:32:52 
 */
package com.renren.fenqi.auth.controller.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.renren.fenqi.auth.Utils.MD5;
import com.renren.fenqi.auth.model.user.SystemUser;
import com.renren.fenqi.auth.response.ResultResponse;
import com.renren.fenqi.auth.service.realm.RealmService;
import com.renren.fenqi.auth.service.role.SystemRoleService;
import com.renren.fenqi.auth.service.user.SystemUserService;
import com.renren.fenqi.dbtool.page.Page;
import com.renren.fenqi.dbtool.page.PagingUtil;

@Controller
@RequestMapping("user")
public class UserController{
	
	private int pageSize = 10;
	
	@Autowired
	private SystemUserService systemUserService;
	
	@Autowired
	private SystemRoleService systemRoleService;
	
	@Autowired
	private RealmService realmService;

	@RequestMapping(value="userListPage")
	public String gotoUserPage(){
		return "user/user_list_page";
	}
	
	@RequestMapping(value="/loadUserList",method=RequestMethod.GET)
	public String listUsers(Model model,@RequestParam(value="pageNum",required=false) Integer pageNum){
		if(null == pageNum || pageNum<=0){
			pageNum = 1;
		}
		Page<SystemUser> responsePage = systemUserService.page(pageNum,pageSize,null);
		
		if (null != responsePage) {
	        List<Integer> paginationList = PagingUtil.getPaginationList(responsePage, 5);
	        model.addAttribute("paginationList", paginationList);
	        model.addAttribute("contents", responsePage.getContent());
	        model.addAttribute("pageNum", responsePage.getPageNum());
	        model.addAttribute("pageSize", responsePage.getPageSize());
	        model.addAttribute("totalNum", responsePage.getTotalNum());
	        model.addAttribute("totalPage", responsePage.getTotalPages());
		}
		return "user/table/user_table";
	}
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse saveRole(
			@RequestParam("name") String name,
			@RequestParam("password") String password,
			@RequestParam("nickName") String nickName){
		SystemUser user = new SystemUser();
		user.setName(name);
		user.setNickName(nickName);
		user.setPassword(password);
		user.setRegisterTime(new Date());
		String checkMsg = this.checkUser(user);
		if(StringUtils.isNotBlank("")){
			return new ResultResponse(ResultResponse.FAILD, checkMsg);
		}
		int id = systemUserService.saveUser(user);
		if(id>0){
			return new ResultResponse(ResultResponse.SUCCESS, "保存成功");
			
		}else{
			return new ResultResponse(ResultResponse.FAILD, "保存失败");
		}
	}
	
	@RequestMapping(value="/saveUserRole",method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse saveUserRole(
			@RequestParam("userId") int userId,
			@RequestParam("roleIds") String roleIds){
//		Type type = new TypeToken<List<Integer>>(){}.getType();	
		try {
			
			List<String> strIds = JSONObject.parseArray(roleIds,String.class);
			List<Integer> ids = this.convertRoleIds(strIds);
			int i = systemRoleService.saveUserRole(userId, ids);
			if(i>0){
				return new ResultResponse(ResultResponse.SUCCESS,"保存成功");
				
			}else{
				return new ResultResponse(ResultResponse.FAILD,"保存失败");
			}
		} catch (Exception e) {
			return new ResultResponse(ResultResponse.FAILD,"系统异常");
		}	
	}

	private List<Integer> convertRoleIds(List<String> roleIds) throws Exception{
		if(null == roleIds || roleIds.size()<=0){
			return null;
		}
		List<String> realmIds = realmService.getAllRealmIds();
		List<Integer> ids = new ArrayList<>();
		for (String roleId : roleIds) {
			if(realmIds.contains(roleId))
				continue;
			try {
				
				Integer id = Integer.parseInt(roleId);
				ids.add(id);
			} catch (Exception e) {
				throw new Exception("解析角色id错误："+roleId);
			}
		}
		return ids;
	}
	private String checkUser(SystemUser user) {
		if(StringUtils.isBlank(user.getName())){
			return "登录名为空";
		}
		SystemUser checkUser = systemUserService.getByName(user.getName());
		if(null != checkUser){
			return "该登录名已被占用";
		}
		if(StringUtils.isBlank(user.getNickName())){
			return "昵称为空";
		}
		if(StringUtils.isBlank(user.getPassword())){
			return "密码需要至少8位数字于字母组合";
		}
		user.setPassword(MD5.digest(user.getPassword()));
		return "";
	}
	
	
}
