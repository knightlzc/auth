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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.renren.fenqi.auth.common.Constant;
import com.renren.fenqi.dbtool.repository.SqlRepository;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description: 用户角色映射数据仓 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Repository
public class UserRoleRepository extends SqlRepository{
	
	private final static String TABLE = "system_user_role"; 

	@Resource(name=Constant.AUTH_JDBC_DATASOURCE)
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}
	public List<Integer> getRoleIdsByUserId(int userId) {
		String sql = "select role_id from "+ TABLE +" where user_id = "+userId;
		return jdbcTemplate.queryForList(sql, Integer.class);
	}
	/**
	 * <br/>Description:根据用户id删除
	 * <p>Author:zcliu/刘子萃</p>
	 * @param roleId
	 * @return
	 */
	public int deleteByUserId(int userId){
		Map<String,Object> param = new HashMap<>();
		param.put("user_id",userId);
		return this.delete(TABLE,param);
	}

}
