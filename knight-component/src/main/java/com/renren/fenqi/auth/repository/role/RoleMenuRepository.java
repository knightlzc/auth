/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: RoleMenuRepository.java
 * @Package com.renren.fenqi.auth.repository.role
 * @date 2016-11-11 下午6:00:16 
 */
package com.renren.fenqi.auth.repository.role;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.ArgumentTypePreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.renren.fenqi.auth.common.Constant;
import com.renren.fenqi.dbtool.repository.SqlRepository;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description: 角色菜单映射数据仓 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Repository
public class RoleMenuRepository extends SqlRepository{
	
	private final static String TABLE = "system_role_menu"; 

	@Resource(name=Constant.AUTH_JDBC_DATASOURCE)
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}
	public List<Integer> getMenuIdsByRoleId(int roleId) {
		String sql = "select menu_id from "+ TABLE +" where role_id = "+roleId;
		return jdbcTemplate.queryForList(sql, Integer.class);
	}
	/**
	 * <br/>Description:根据角色id删除
	 * <p>Author:zcliu/刘子萃</p>
	 * @param roleId
	 * @return
	 */
	public int deleteByRoleId(int roleId){
		Map<String,Object> param = new HashMap<>();
		param.put("role_id",roleId);
		return this.delete(TABLE,param);
	}
	
	/**
	 * <br/>Description:
	 * <p>Author:zcliu/刘子萃</p>
	 * @param roleIds
	 * @return
	 */
	public List<Integer> getMenuIdsByRoleIds(List<Integer> roleIds){
		if(null == roleIds || roleIds.size()<=0){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(Integer roleId:roleIds){
			sb.append(roleId+",");
		}
		sb.substring(0,sb.length()-1);
		String sql = "select menu_id from "+ TABLE +" where role_id in ("+sb.substring(0,sb.length()-1).toString()+")";
		return jdbcTemplate.queryForList(sql, Integer.class);
	}

}
