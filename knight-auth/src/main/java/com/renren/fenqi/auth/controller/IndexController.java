package com.renren.fenqi.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.renren.fenqi.auth.Utils.MenuUtils;
import com.renren.fenqi.auth.model.menu.SystemMenu;
import com.renren.fenqi.auth.service.menu.SystemMenuService;


@Controller
public class IndexController {

	@Autowired
	private SystemMenuService systemMenuService;
    @RequestMapping("")
    public String index(Model model) {
    	List<SystemMenu> menus = systemMenuService.listAllByRealm("auth");
    	model.addAttribute("menus",MenuUtils.menuTree(menus));
        return "index";
    }

    public static void main(String[] args) {
        String s = "<dt class='titleNavMenu'>商家渠道管理</dt></ul></dd>";
        System.out.println(s.indexOf("href"));
    }

}
