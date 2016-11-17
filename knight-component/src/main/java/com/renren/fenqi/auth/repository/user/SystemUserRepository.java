/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: PfUserRepository.java
 * @Package com.renren.fenqi.auth.repository.user
 * @date 2016-11-10 下午6:01:16 
 */
package com.renren.fenqi.auth.repository.user;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.renren.fenqi.auth.common.Constant;
import com.renren.fenqi.dbtool.repository.SqlRepository;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description: 用户管理数据仓 </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Repository
public class SystemUserRepository extends SqlRepository{

	@Resource(name=Constant.AUTH_JDBC_DATASOURCE)
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}

}
