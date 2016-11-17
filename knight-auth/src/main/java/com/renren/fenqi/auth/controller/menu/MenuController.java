package com.renren.fenqi.auth.controller.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.renren.fenqi.auth.model.menu.SystemMenu;
import com.renren.fenqi.auth.service.menu.SystemMenuService;
import com.renren.fenqi.dbtool.page.Page;
import com.renren.fenqi.dbtool.page.PagingUtil;

/**
 * @author jinshi
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
	private int pageSize = 10;
	
	
	@Autowired
	private SystemMenuService systemMenuService;
	
	@RequestMapping("/menuListPage")
	public String gotoMenuPage(Model model,@RequestParam(value="pageNum",required=false) Integer pageNum){
		model.addAttribute("pageNum", pageNum);
		return "menu/menu_list_page";
	}
	
	@RequestMapping(value="/loadMenuList",method=RequestMethod.GET)
	public String listMenu(Model model,@RequestParam(value="parentId",required=false) Integer parentId,@RequestParam(value="pageNum",required=false) Integer pageNum){
		if(null == pageNum || pageNum<=0){
			pageNum = 1;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("parent_id", parentId==null?0:parentId);
		Page<SystemMenu> responsePage = systemMenuService.page(pageNum,pageSize,param);
		
		if (null != responsePage) {
	        List<Integer> paginationList = PagingUtil.getPaginationList(responsePage, 5);
	        model.addAttribute("paginationList", paginationList);
	        model.addAttribute("contents", responsePage.getContent());
	        model.addAttribute("pageNum", responsePage.getPageNum());
	        model.addAttribute("pageSize", responsePage.getPageSize());
	        model.addAttribute("totalNum", responsePage.getTotalNum());
	        model.addAttribute("totalPage", responsePage.getTotalPages());
		}
		if(parentId==0||parentId==null)
		 return "menu/menu_list";
		return "menu/submenu_list";
	}
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveMenu(
			@RequestParam("menuTitle") String menuTitle,
			@RequestParam("realm") String realm,
			@RequestParam("menuDesc") String menuDesc,
			@RequestParam("menuUrl") String menuUrl,
			@RequestParam("parentId") int parentId,
			@RequestParam("sort") int sort
			){
		Map<String,Object> jb = new HashMap<>();
		SystemMenu menu = new SystemMenu();
		menu.setParentId(parentId);
		menu.setMenuTitle(menuTitle);
		menu.setRealm(realm);
		menu.setMenuDesc(menuDesc);
		menu.setMenuUrl(menuUrl);
		menu.setSort(sort);
		String checkMsg = this.checkMenu(menu);
		if(StringUtils.isNotBlank(checkMsg)){
			jb.put("suc", false);
			jb.put("msg", checkMsg);
			return jb;
		}
		int id = systemMenuService.saveMenu(menu);
		if(id>0){
			jb.put("suc", true);
			jb.put("msg", "保存成功");
			return jb;
			
		}else{
			jb.put("suc", false);
			jb.put("msg", "保存失败");
			return jb;
		}
	}
	
	@RequestMapping(value="/detail",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> detailMenu(Model model,@RequestParam(value="pageNum",required=false) Integer pageNum,@RequestParam("id") int id){
		Map<String,Object> jb = new HashMap<>();
		SystemMenu menu = systemMenuService.queryMenu(id);
		if(menu!=null){
				jb.put("suc", true);
				jb.put("msg", "保存成功");
				jb.put("renrenData", menu);
				return jb;
	     }else{
				jb.put("suc", false);
				jb.put("msg", "保存失败");
				return jb;
	    }
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editMenu(
			@RequestParam("id") int id,
			@RequestParam("parentId") int parentId,
			@RequestParam("menuTitle") String menuTitle,
			@RequestParam("realm") String realm,
			@RequestParam("menuDesc") String menuDesc,
			@RequestParam("menuUrl") String menuUrl,
			@RequestParam("sort") int sort
			){
		Map<String,Object> jb = new HashMap<>();
		SystemMenu menu = systemMenuService.queryMenu(id);
		if(menu==null){
			jb.put("suc", false);
			jb.put("msg", "该菜单未找到，不可编辑");
			return jb;
		}
		menu.setParentId(parentId);
		menu.setMenuTitle(menuTitle);
		menu.setRealm(realm);
		menu.setMenuDesc(menuDesc);
		menu.setMenuUrl(menuUrl);
		menu.setSort(sort);
		String checkMsg = this.checkMenu(menu);
		if(StringUtils.isNotBlank(checkMsg)){
			jb.put("suc", false);
			jb.put("msg", checkMsg);
			return jb;
		}
		int result = systemMenuService.updateMenu(menu);
		if(result>0){
			jb.put("suc", true);
			jb.put("msg", "更新成功");
			return jb;
			
		}else{
			jb.put("suc", false);
			jb.put("msg", "保存失败");
			return jb;
		}
	}
	
	@RequestMapping(value="/subMenuListPage",method=RequestMethod.GET)
	public String gotoSubMenuPage(Model model,@RequestParam("parentId") int parentId,
			@RequestParam("pageNum") int pageNum
			){
		//找到父节点信息
		SystemMenu menu = systemMenuService.queryMenu(parentId);
		if(menu.getParentId()==0){
			model.addAttribute("level", 0);
		}else{
			model.addAttribute("level", 1);
		}
		model.addAttribute("grandId", menu.getParentId());
		model.addAttribute("parentId", parentId);
		model.addAttribute("pageNum", pageNum);
		return "menu/submenu_list_page";
	}
	
	public String checkMenu(SystemMenu menu){
		if(StringUtils.isBlank(menu.getMenuTitle())){
			return "资源名称为空";
		}
		if(StringUtils.isBlank(menu.getRealm())){
			return "作用域未选择";
		}
		/*if(StringUtils.isBlank(menu.getMenuUrl())){
			return "资源url为空";
		}*/
		/*if(menu.getSort()<=0){
			return "排序未选择";
		}*/
		return null;
	}
}
