package com.renren.fenqi.dbtool.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.renren.fenqi.dbtool.common.SortPage;
import com.renren.fenqi.dbtool.page.Page;
import com.renren.fenqi.dbtool.page.PagingUtil;
import com.renren.fenqi.dbtool.tool.SqlTool;

public abstract class SqlRepository{

	
	public abstract JdbcTemplate getJdbcTemplate();
	
	/**
	 * <p>Description:插入数据库（无自定义列）</p>
	 * <p>Author:zcliu/刘子萃</p>
	 * @Title: insert
	 * @param object
	 * @return
	 * @see knight.repository.ISqlRepository#insert(java.lang.Object)
	 */
	
	public int insert(JdbcTemplate jdbcTemplate,Object object) {
		try {
			return insert(jdbcTemplate, object,null);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * <br/>Description:插入
	 * <p>Author:zcliu/刘子萃</p>
	 * @param object
	 * @return
	 */
	public int insert(Object object) {
		return insert(getJdbcTemplate(), object,null);
	}
	
	public int delete(String tableName,Map<String, Object> andConditions){
		if(null == andConditions){
			return 0;
		}
		String deleteSql = SqlTool.deleteSql(null,tableName,andConditions,null);
		return getJdbcTemplate().update(deleteSql);
	}
	public int delete(Class<?> clazz,Map<String, Object> andConditions){
		if(null == andConditions){
			return 0;
		}
		String deleteSql = SqlTool.deleteSql(clazz,null,andConditions,null);
		return getJdbcTemplate().update(deleteSql);
	}

	
	/**
	 * <p>Description:插入数据库（允许自定义列名）</p>
	 * <p>Author:zcliu/刘子萃</p>
	 * @Title: insert
	 * @param object
	 * @param mapParams
	 * @return
	 * @see knight.repository.ISqlRepository#insert(java.lang.Object, java.util.Map)
	 */
	
	public int insert(JdbcTemplate jdbcTemplate, Object object, Map<String, Object> mapParams) {
		String insertSql =SqlTool.insertSql(object, mapParams); 
		int i = jdbcTemplate.update(insertSql);
		return i;
	}
	
	public List<Map<String, Object>> listMap(JdbcTemplate jdbcTemplate,Class<?> clazz,List<String> columns,Map<String, Object> andConditions,Map<String, Object> orConditions,SortPage sortPage,String tableName) {
		String selectSql = SqlTool.selectSql(clazz, columns, andConditions, orConditions, sortPage, null);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(selectSql);
		return list;
	} 
	
	public <T> List<T> list(Class<?> clazz,Map<String, Object> andParam,SortPage sortPage) {
		return list(getJdbcTemplate(),clazz,null,andParam,null,sortPage,null);
	} 
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public <T> List<T> list(JdbcTemplate jdbcTemplate,Class<?> clazz,List<String> columns,Map<String, Object> andParam,Map<String, Object> orConditions,SortPage sortPage,String tableName) {
		String selectSql = SqlTool.selectSql(clazz, columns, andParam, orConditions, sortPage, null);
		List<T> list = jdbcTemplate.query(selectSql,new BeanPropertyRowMapper(clazz));
		return list;
	} 
	
	/**
	 * <br/>Description:分页查询
	 * <p>Author:zcliu/刘子萃</p>
	 * @param jdbcTemplate 模板
	 * @param clazz 源数据类型
	 * @param andConditions and查询条件 
	 * @param sortPage 分页类
	 * @return
	 */
	public <T> Page<T> page(JdbcTemplate jdbcTemplate,Class<?> clazz,Map<String, Object> andConditions,SortPage sortPage) {
		return page(jdbcTemplate,clazz,null,andConditions,null,sortPage,null);
	} 
	/**
	 * <br/>Description:分页查询
	 * <p>Author:zcliu/刘子萃</p>
	 * @param clazz 源数据类型
	 * @param andConditions and查询条件 
	 * @param sortPage 分页类
	 * @return
	 */
	public <T> Page<T> page(Class<?> clazz,Map<String, Object> andConditions,SortPage sortPage) {
		return page(getJdbcTemplate(),clazz,null,andConditions,null,sortPage,null);
	} 
	
	public <T> Page<T> page(Class<?> clazz,List<String> columns,Map<String, Object> andConditions,Map<String, Object> orConditions,SortPage sortPage,String tableName) {
		return page(getJdbcTemplate(),clazz,columns,andConditions,orConditions,sortPage,tableName);
	} 
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public <T> Page<T> page(JdbcTemplate jdbcTemplate,Class<?> clazz,List<String> columns,Map<String, Object> andConditions,Map<String, Object> orConditions,SortPage sortPage,String tableName) {
		String selectSql = SqlTool.selectSql(clazz, columns, andConditions, orConditions, sortPage, null);
		int count = count(jdbcTemplate,clazz,andConditions,orConditions,tableName);
		List<T> list = jdbcTemplate.query(selectSql,new BeanPropertyRowMapper(clazz));
		return PagingUtil.getPage(list,sortPage.getPageNum(),sortPage.getPageSize(),count);
	} 
	
	public int count(JdbcTemplate jdbcTemplate,Class<?> clazz,Map<String, Object> andConditions,Map<String, Object> orConditions,String tableName) {
		String countSql = SqlTool.countSql(clazz, andConditions, orConditions, tableName);
		Integer count = jdbcTemplate.queryForObject(countSql,Integer.class);
		return count==null?0:count;
	} 

	
//	public Map<String, Object> single(JdbcTemplate jdbcTemplate,Class<?> clazz, List<String> columns,
//			Map<String, Object> andConditions,
//			Map<String, Object> orConditions, SortPage sortPage,
//			String tableName) {
//		String selectSql = SqlTool.selectSql(clazz, columns, andConditions, orConditions, sortPage, null);
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(selectSql);
//		if(null != list && list.size()>0){
//			return list.get(0);
//		}
//		return null;
//	}
	
	public <T> T single(Class<?> clazz,Map<String, Object> andConditions) {
		return  single(null,clazz,null,andConditions,null,null,null);
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public <T> T single(JdbcTemplate jdbcTemplate,Class<?> clazz, List<String> columns,
			Map<String, Object> andConditions,
			Map<String, Object> orConditions, SortPage sortPage,
			String tableName) {
		if(null == jdbcTemplate){
			jdbcTemplate = getJdbcTemplate();
		}
		String selectSql = SqlTool.selectSql(clazz, columns, andConditions, orConditions, sortPage, null);
		List<T> list = jdbcTemplate.query(selectSql,new BeanPropertyRowMapper(clazz));
//		List<T> list = jdbcTemplate.query(selectSql,new BeanPropertyRowMapper(clazz));
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * <p>Description:执行更新操作</p>
	 * <p>Author:zcliu/刘子萃</p>
	 * @Title: update
	 * @param object 实体对象
	 * @param mapParams 除实体对象之外额外需要更新的列
	 * @param conditionsParams
	 * @see knight.repository.ISqlRepository#update(java.lang.Object, java.util.Map, java.util.Map)
	 */
	
	public int update(JdbcTemplate jdbcTemplate,Object object,Map<String, Object> mapParams,Map<String, Object> conditionsParams) {
		String updateSql = SqlTool.updateSql(object, mapParams, conditionsParams);
		return jdbcTemplate.update(updateSql);
	}
	
	
	
	
	
	
}
