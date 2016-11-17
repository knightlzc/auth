/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: SystemRoleRepository.java
 * @Package com.renren.fenqi.auth.repository
 * @date 2016-11-9 下午6:02:15 
 */
package com.renren.fenqi.auth.repository.role;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.renren.fenqi.auth.common.Constant;
import com.renren.fenqi.dbtool.repository.SqlRepository;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:系统角色数据仓 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Repository
public class SystemRoleRepository extends SqlRepository{
	
	public static final String TABLE = "system_role";

	@Resource(name=Constant.AUTH_JDBC_DATASOURCE)
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}

}
