package com.renren.fenqi.dbtool.service.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.jdbc.core.JdbcTemplate;

import com.renren.fenqi.dbtool.common.RepositoryFactory;
import com.renren.fenqi.dbtool.common.SortPage;
import com.renren.fenqi.dbtool.common.Utils;
import com.renren.fenqi.dbtool.repository.SqlRepository;
import com.renren.fenqi.dbtool.service.ISqlService;
import com.renren.fenqi.dbtool.tool.SqlTool;


public class SqlServiceImpl implements ISqlService{
	
//	private ThreadLocal<String> sql_tl = new ThreadLocal<>();
	
	private ThreadLocal<Map<String,Object>> andParams_tl = new ThreadLocal<>();
	
	private ThreadLocal<Map<String,Object>> orParams_tl = new ThreadLocal<>();
	
	private ThreadLocal<SortPage> sp_tl = new ThreadLocal<>();
	
	private ThreadLocal<Object> obj_tl = new ThreadLocal<>();
	
	private ThreadLocal<Class<?>> clazz_tl = new ThreadLocal<>();
	
	private ThreadLocal<List<String>> columns_tl = new ThreadLocal<>();
	
	private SqlRepository sqlRepository = RepositoryFactory.get(SqlRepository.class);
	
	
	/**
	 * <br/>Description:设置初始化实体
	 * <p>Author:zcliu/刘子萃</p>
	 * @param object
	 * @return
	 */
	public SqlServiceImpl initSelect(Object object){
		obj_tl.set(object);
		andParams_tl.remove();
		orParams_tl.remove();
		sp_tl.remove();
		return this;
	}
	
	public SqlServiceImpl initSelect(Class<?> clazz){
		clazz_tl.set(clazz);
		andParams_tl.remove();
		orParams_tl.remove();
		sp_tl.remove();
		return this;
	}
	
	public SqlServiceImpl initUpdate(Object object){
		obj_tl.set(object);
		andParams_tl.remove();
		orParams_tl.remove();
		return this;
	}
	
	public SqlServiceImpl initUpdate(Class<?> clazz){
		clazz_tl.set(clazz);
		andParams_tl.remove();
		orParams_tl.remove();
		return this;
	}
	
	/**
	 * <br/>Description:设置and语句查询集合
	 * <p>Author:zcliu/刘子萃</p>
	 * @param mapParam
	 * @return
	 */
	public SqlServiceImpl and(Map<String,Object> mapParam){
		Map<String,Object> andParams = andParams_tl.get();
		if(null==andParams){
			andParams = new HashMap<String, Object>();
			andParams_tl.set(andParams);
		}
		andParams.putAll(mapParam);
		return this;
	}
	
	/**
	 * <br/>Description:设置and语句查询集合
	 * <p>Author:zcliu/刘子萃</p>
	 * @param mapParam
	 * @return
	 */
	public SqlServiceImpl and(String key,Object value){
		Map<String,Object> andParams = andParams_tl.get();
		if(null==andParams){
			andParams = new HashMap<String, Object>();
			andParams_tl.set(andParams);
		}
		andParams.put(key, value);
		return this;
	}
	
	/**
	 * <br/>Description:设置or语句查询集合
	 * <p>Author:zcliu/刘子萃</p>
	 * @param mapParam
	 * @return
	 */
	public SqlServiceImpl or(String key,Object value){
		Map<String,Object> orParams = orParams_tl.get();
		if(null==orParams){
			orParams = new HashMap<String, Object>();
			orParams_tl.set(orParams);
		}
		orParams.put(key, value);
		return this;
	}
	
	/**
	 * <br/>Description:设置or语句查询集合
	 * <p>Author:zcliu/刘子萃</p>
	 * @param mapParam
	 * @return
	 */
	public SqlServiceImpl or(Map<String,Object> mapParam){
		Map<String,Object> orParams = orParams_tl.get();
		if(null==orParams){
			orParams = new HashMap<String, Object>();
			orParams_tl.set(orParams);
		}
		orParams.putAll(mapParam);
		return this;
	}
	
	/**
	 * <br/>Description:倒序排列
	 * <p>Author:zcliu/刘子萃</p>
	 * @param sortKey
	 * @return
	 */
	public SqlServiceImpl desc(String sortKey){
		if(null==sortKey){
			return this;
		}
		SortPage sp = sp_tl.get();
		if(null==sp){
			sp = new SortPage();
			sp_tl.set(sp);
		}
		sp.setSortRule(SortPage.DESC);
		sp.setSortKey(sortKey);
		return this;
	}
	
	/**
	 * <br/>Description:顺序排列
	 * <p>Author:zcliu/刘子萃</p>
	 * @param sortKey
	 * @return
	 */
	public SqlServiceImpl asc(String sortKey){
		if(null==sortKey){
			return this;
		}
		SortPage sp = sp_tl.get();
		if(null==sp){
			sp = new SortPage();
		}
		sp.setSortRule(SortPage.ASC);
		sp.setSortKey(sortKey);
		sp_tl.set(sp);
		return this;
	}
	
	/**
	 * <br/>Description:分页查询
	 * <p>Author:zcliu/刘子萃</p>
	 * @param start
	 * @param limit
	 * @return
	 */
	public SqlServiceImpl limit(Integer start,Integer limit){
		SortPage sp = sp_tl.get();
		if(Utils.isNumberValue(start) && Utils.isNumberValue(limit)){
			if(null==sp){
				sp = new SortPage();
			}
			sp.setStart(start);
			sp.setLimit(limit);
		}
		sp_tl.set(sp);
		return this;
	}
	
	/**
	 * <br/>Description:添加额外列
	 * <p>Author:zcliu/刘子萃</p>
	 * @param columes
	 * @return
	 */
	public SqlServiceImpl addColumes(List<String> columes){
		List<String> cols = columns_tl.get();
		if(null==cols){
			cols = new ArrayList<>();
			columns_tl.set(cols);
		}
		cols.addAll(columes);
		return this;
	}
	
	/**
	 * <br/>Description:生产查询语句
	 * <p>Author:zcliu/刘子萃</p>
	 * @return
	 */
	public String createSelectSql() {
//		Object object = obj_tl.get();
		Class<?> clazz = clazz_tl.get();
		List<String> columns = columns_tl.get();
		Map<String, Object> andConditions = andParams_tl.get();
		Map<String, Object> orCondtions = orParams_tl.get();
		SortPage sortPage = sp_tl.get();
		return SqlTool.selectSql(clazz, columns, andConditions, orCondtions, sortPage, null);
	}
	
	public <T> List<T> list(JdbcTemplate jdbcTemplate, Class<?> clazz) {
		Object object = obj_tl.get();
		List<String> columns = columns_tl.get();
		Map<String, Object> andConditions = andParams_tl.get();
		Map<String, Object> orConditions = orParams_tl.get();
		SortPage sortPage = sp_tl.get();
		return sqlRepository.list(jdbcTemplate,clazz, columns, andConditions, orConditions, sortPage, null);
	}
	
	public List<Map<String, Object>> list(JdbcTemplate jdbcTemplate) {
		Object object = obj_tl.get();
		Class<?> clazz = clazz_tl.get();
		List<String> columns = columns_tl.get();
		Map<String, Object> andConditions = andParams_tl.get();
		Map<String, Object> orConditions = orParams_tl.get();
		SortPage sortPage = sp_tl.get();
		return sqlRepository.list(jdbcTemplate,clazz, columns, andConditions, orConditions, sortPage, null);
	}
	
	public <T> T single(JdbcTemplate jdbcTemplate, Class<?> clazz) {
//		Object object = obj_tl.get();
		List<String> columns = columns_tl.get();
		Map<String, Object> andConditions = andParams_tl.get();
		Map<String, Object> orConditions = orParams_tl.get();
		SortPage sortPage = sp_tl.get();
		return sqlRepository.single(jdbcTemplate,clazz, columns, andConditions, orConditions, sortPage, null);
	}
	
	public Map<String, Object> single(JdbcTemplate jdbcTemplate) {
		Object object = obj_tl.get();
		Class<?> clazz = clazz_tl.get();
		List<String> columns = columns_tl.get();
		Map<String, Object> andConditions = andParams_tl.get();
		Map<String, Object> orConditions = orParams_tl.get();
		SortPage sortPage = sp_tl.get();
		return sqlRepository.single(jdbcTemplate,clazz, columns, andConditions, orConditions, sortPage, null);
	}
	
	public int updateById(JdbcTemplate jdbcTemplate, Object object){
		Method method = null;
		try {
			PropertyDescriptor pd = new PropertyDescriptor("id", object.getClass());
			method = pd.getReadMethod();
			Object id = method.invoke(object);  
			Map<String, Object> conditionsParams = new HashMap<>();
			conditionsParams.put("id", id);
			return sqlRepository.update(jdbcTemplate,object, null, conditionsParams);
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return 0;
	}
}



