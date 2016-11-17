/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: SystemRoleRepository.java
 * @Package com.renren.fenqi.auth.repository
 * @date 2016-11-9 下午6:02:15 
 */
package com.renren.fenqi.auth.repository.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.renren.fenqi.auth.common.Constant;
import com.renren.fenqi.auth.model.menu.SystemMenu;
import com.renren.fenqi.dbtool.repository.SqlRepository;
import com.renren.fenqi.dbtool.tool.SqlTool;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:系统角色数据仓 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Repository
public class SystemMenuRepository extends SqlRepository{

	@Resource(name=Constant.AUTH_JDBC_DATASOURCE)
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}

	public SystemMenu queryByid(int id) {
		String selectSql = "SELECT id,menu_title,parent_id,realm,menu_desc,menu_url,sort,update_time,create_time FROM system_menu WHERE id="+id+" LIMIT 1";
		List<SystemMenu> list =(List<SystemMenu>) jdbcTemplate.query(selectSql,new BeanPropertyRowMapper(SystemMenu.class));
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	
    public int update(SystemMenu menu){
    	Map<String, Object> conditionsParams = new HashMap<String, Object>();
    	conditionsParams.put("id", menu.getId());
    	Map<String, Object> mapParams = new HashMap<String, Object>();
    	mapParams.put("menu_title", menu.getMenuTitle());
    	mapParams.put("realm", menu.getRealm());
    	mapParams.put("menu_desc", menu.getMenuDesc());
    	mapParams.put("menu_url", menu.getMenuUrl());
    	mapParams.put("sort", menu.getSort());
    	mapParams.put("parent_id", menu.getParentId());
    	String updateSql = SqlTool.updateSql(mapParams, conditionsParams,SystemMenu.class);
    	return jdbcTemplate.update(updateSql);
    }
    
    public List<SystemMenu>  queryListByid(List<Integer> ids) {
    	if(ids==null||ids.size()==0){
    		return null;
    	}
    	StringBuilder build = new StringBuilder();
    	for(Integer id:ids){
    		build.append(id);
    		build.append(",");
    	}
    	int endIndex = build.toString().lastIndexOf(",");
    	String inSql = build.toString().substring(0, endIndex);
		String selectSql = "SELECT id,menu_title,parent_id,realm,menu_desc,menu_url,sort,update_time,create_time FROM system_menu WHERE id in (?) order by parent_id,sort;";
		selectSql = selectSql.replace("?", inSql);
		List<SystemMenu> list =(List<SystemMenu>) jdbcTemplate.query(selectSql,new BeanPropertyRowMapper(SystemMenu.class));
		return list;
	}
    
    public static void main(String[] args) {
    	List<Integer> ids = new ArrayList<Integer>();
    	ids.add(1);
    	ids.add(2);
    	ids.add(3);
    	ids.add(4);
    	StringBuilder build = new StringBuilder();
    	for(Integer id:ids){
    		build.append(id);
    		build.append(",");
    	}
    	int endIndex = build.toString().lastIndexOf(",");
    	String inSql = build.toString().substring(0, endIndex);
    	String selectSql = "SELECT id,menu_title,parent_id,realm,menu_desc,menu_url,sort,update_time,create_time FROM system_menu WHERE id in (?) order by parent_id,sort;";
    	selectSql = selectSql.replace("?", inSql);
    	System.out.println(selectSql);
	}
}
