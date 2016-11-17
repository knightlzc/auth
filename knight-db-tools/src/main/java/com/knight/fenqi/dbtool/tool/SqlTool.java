/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: SqlTool.java
 * @Package knight.tools
 * @date 2014-6-19 下午1:06:22 
 */
package com.renren.fenqi.dbtool.tool;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.renren.fenqi.dbtool.annotation.Colum;
import com.renren.fenqi.dbtool.annotation.Colum;
import com.renren.fenqi.dbtool.annotation.TableName;
import com.renren.fenqi.dbtool.common.Constant;
import com.renren.fenqi.dbtool.common.DateUtils;
import com.renren.fenqi.dbtool.common.SortPage;


/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class SqlTool {
	
	private static ThreadLocal<String> sql = new ThreadLocal<>();
	
	public static String insertSql(Object object) {
		return insertSql(object,null);
	}

	/**
	 * <br/>Description:插入语句
	 * <p>Author:zcliu/刘子萃</p>
	 * @param obj
	 * @return
	 */
	public static String insertSql(Object object,Map<String, Object> mapParams) {
		String insertSql = null; 
		String tableName = getTableName(object);
		try {
			if(null!=tableName && !"".equals(tableName)){
				StringBuffer keyStr = new StringBuffer("INSERT INTO ");
				StringBuffer valueStr = new StringBuffer(" VALUES (");
				List<Field> fieldList = covertFieldList(object);
				if(null != fieldList && fieldList.size() > 0){
					keyStr.append(tableName + " (");
					if (mapParams != null && mapParams.size() > 0) {  
	                    Iterator<String> keyNames = mapParams.keySet().iterator();  
	                    while (keyNames.hasNext()) {  
	                        String keyName = (String) keyNames.next();  
	                        keyStr.append(keyName + ",");  
	                        if(null==mapParams.get(keyName) || isNumberValue(mapParams.get(keyName)) || mapParams.get(keyName) instanceof Boolean){
								valueStr.append(mapParams.get(keyName)+ ",");
							}else if(isDate(mapParams.get(keyName))){
								valueStr.append("'" + DateUtils.dateToString( (Date) mapParams.get(keyName), "yyyy-MM-dd HH:mm:ss") + "',"); 
							}else{
								valueStr.append("'" + mapParams.get(keyName) + "',"); 
							}
	                    }  
	                } 
					for (Field field : fieldList) {
						boolean nextFieldFlag = false;
						Colum dbAnno = field.getAnnotation(Colum.class);
						if (mapParams != null && mapParams.size() > 0) { 
							Iterator<String> keyNames = mapParams.keySet().iterator();  
							while (keyNames.hasNext()) {  
		                        String keyName = (String) keyNames.next();  
		                        if(null==dbAnno.columName() || "".equals(dbAnno.columName())){
									if(keyName.equals(field.getName())){
										nextFieldFlag = true;
										break;
									}
								}else{
									if(keyName.equals(dbAnno.columName())){
										nextFieldFlag = true;
										break;
									}
								}
		                    }  
						}
						if(nextFieldFlag){
							continue;
						}
						if(null==dbAnno.columName() || "".equals(dbAnno.columName())){
							keyStr.append(field.getName()+ ",");
						}else{
							keyStr.append(dbAnno.columName()+ ",");
						}
						Object fieldValue = getFieldValue(object, field);
						if(null==fieldValue || isNumberValue(fieldValue) || fieldValue instanceof Boolean){
							valueStr.append(fieldValue+ ",");
						}else if(isDate(fieldValue)){
							valueStr.append("'" + DateUtils.dateToString( (Date) fieldValue, "yyyy-MM-dd HH:mm:ss") + "',"); 
						}else{
							valueStr.append("'" + fieldValue + "',"); 
						}
					}
				}
				insertSql = keyStr.toString().substring(0, keyStr.length()-1)+")"
							+valueStr.toString().substring(0,valueStr.length()-1)+")"; 
			}else{
				throw new NullPointerException("Table name is null");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return insertSql;
	}
	
	public static String updateSql(Object object,Map<String, Object> conditionsParams){
		return updateSql(object, null, conditionsParams);
	}
	
	public static String updateSql(Object object,Map<String, Object> mapParams,Map<String, Object> conditionsParams){
		String updateSql = null; 
		String tableName = getTableName(object);
		try {
			if(null!=tableName && !"".equals(tableName)){
				StringBuffer tableStr = new StringBuffer("UPDATE "+tableName);
				StringBuffer setStr = new StringBuffer(" SET ");
				StringBuffer whereStr = new StringBuffer(" WHERE ");
				List<Field> fieldList = covertFieldList(object);
				if(null != fieldList && fieldList.size() > 0){
					if (mapParams != null && mapParams.size() > 0) {  
	                    Iterator<String> keyNames = mapParams.keySet().iterator();  
	                    while (keyNames.hasNext()) {  
	                        String keyName = (String) keyNames.next();  
	                        setStr.append(keyName + " = ");  
	                        if(null==mapParams.get(keyName) || isNumberValue(mapParams.get(keyName)) || mapParams.get(keyName) instanceof Boolean){
								setStr.append(mapParams.get(keyName)+ ",");
							}else if(isDate(mapParams.get(keyName))){
								setStr.append("'" + DateUtils.dateToString( (Date) mapParams.get(keyName), "yyyy-MM-dd HH:mm:ss") + "',"); 
							}else{
								setStr.append("'" + mapParams.get(keyName) + "',"); 
							}
	                    }  
	                } 
					for (Field field : fieldList) {
						boolean nextFieldFlag = false;
						Colum dbAnno = field.getAnnotation(Colum.class);
						if (mapParams != null && mapParams.size() > 0) { 
							Iterator<String> keyNames = mapParams.keySet().iterator();  
							while (keyNames.hasNext()) {  
		                        String keyName = (String) keyNames.next();  
		                        if(null==dbAnno.columName() || "".equals(dbAnno.columName())){
									if(keyName.equals(field.getName())){
										nextFieldFlag = true;
										break;
									}
								}else{
									if(keyName.equals(dbAnno.columName())){
										nextFieldFlag = true;
										break;
									}
								}
		                    }  
						}
						if(nextFieldFlag){
							continue;
						}
						if(null==dbAnno.columName() || "".equals(dbAnno.columName())){
							setStr.append(field.getName()+ " = ");
						}else{
							setStr.append(dbAnno.columName()+ " = ");
						}
						Object fieldValue = getFieldValue(object, field);
						if(null==fieldValue || isNumberValue(fieldValue) || fieldValue instanceof Boolean){
							setStr.append(fieldValue+ ",");
						}else if(isDate(fieldValue)){
							setStr.append("'" + DateUtils.dateToString( (Date) fieldValue, "yyyy-MM-dd HH:mm:ss") + "',"); 
						}else{
							setStr.append("'" + fieldValue + "',"); 
						}
					}
				}
				//条件语句
				if (conditionsParams != null && conditionsParams.size() > 0) {  
                    Iterator<String> conditionKeys = conditionsParams.keySet().iterator();  
                    while (conditionKeys.hasNext()) {  
                        String conditionKey = (String) conditionKeys.next();  
                        whereStr.append(conditionKey + " = ");  
                        if(null==conditionsParams.get(conditionKey) || isNumberValue(conditionsParams.get(conditionKey)) || conditionsParams.get(conditionKey) instanceof Boolean){
                        	whereStr.append(conditionsParams.get(conditionKey)+ " AND ");
						}else if(isDate(mapParams.get(conditionKey))){
							whereStr.append("'" + DateUtils.dateToString( (Date) mapParams.get(conditionKey), "yyyy-MM-dd HH:mm:ss") + "' AND "); 
						}else{
							whereStr.append("'" + conditionsParams.get(conditionKey) + "' AND "); 
						}
                    }  
                } else {
                	throw new NullPointerException("Update sql condition is null");
                }
				updateSql = tableStr.toString()+setStr.toString().substring(0,setStr.length()-1)
							+whereStr.toString().substring(0,whereStr.length()-4); 
			}else{
				throw new NullPointerException("Table name is null");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateSql;
	}
	public static String updateSql(Map<String, Object> mapParams,Map<String, Object> conditionsParams,Class<?> clazz){
		String updateSql = null;
		try {
			Object obj = Class.forName(clazz.getName()).newInstance();
			String tableName = getTableName(obj);
			if(null!=tableName && !"".equals(tableName)){
				StringBuffer tableStr = new StringBuffer("UPDATE "+tableName);
				StringBuffer setStr = new StringBuffer(" SET ");
				StringBuffer whereStr = new StringBuffer(" WHERE ");;
				if (mapParams != null && mapParams.size() > 0) {  
					Iterator<String> keyNames = mapParams.keySet().iterator();  
					while (keyNames.hasNext()) {  
						String keyName = (String) keyNames.next();  
						setStr.append(keyName + " = ");  
						if(null==mapParams.get(keyName) || isNumberValue(mapParams.get(keyName)) || mapParams.get(keyName) instanceof Boolean){
							setStr.append(mapParams.get(keyName)+ ",");
						}else if(isDate(mapParams.get(keyName))){
							setStr.append("'" + DateUtils.dateToString( (Date) mapParams.get(keyName), "yyyy-MM-dd HH:mm:ss") + "',"); 
						}else{
							setStr.append("'" + mapParams.get(keyName) + "',"); 
						}
					}  
				} 
				//条件语句
				if (conditionsParams != null && conditionsParams.size() > 0) {  
					Iterator<String> conditionKeys = conditionsParams.keySet().iterator();  
					while (conditionKeys.hasNext()) {  
						String conditionKey = (String) conditionKeys.next();  
						whereStr.append(conditionKey + " = ");  
						if(null==conditionsParams.get(conditionKey) || isNumberValue(conditionsParams.get(conditionKey)) || conditionsParams.get(conditionKey) instanceof Boolean){
							whereStr.append(conditionsParams.get(conditionKey)+ " AND ");
						}else if(isDate(conditionsParams.get(conditionKey))){
							setStr.append("'" + DateUtils.dateToString( (Date) conditionsParams.get(conditionKey), "yyyy-MM-dd HH:mm:ss") + "',"); 
						}else{
							whereStr.append("'" + conditionsParams.get(conditionKey) + "' AND "); 
						}
					}  
				} else {
					throw new NullPointerException("Update sql condition is null");
				}
				updateSql = tableStr.toString()+setStr.toString().substring(0,setStr.length()-1)
						+whereStr.toString().substring(0,whereStr.length()-4); 
			}else{
				throw new NullPointerException("Table name is null");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateSql;
	}
	
	public static String selectObjectSql(Class<?> clazz,SortPage sortPage){
		return selectSql(clazz, null, null, null, sortPage,null);
	}
	
	/**
	 * <br/>Description:
	 * <p>Author:zcliu/刘子萃</p>
	 * @param <T>
	 * @param object 实体参数（查询字段为实体注释属性）
	 * @param colums 所查列名（查询字段为list的值，与实体冲突会覆盖实体属性）
	 * @param andConditions （AND 查询语句条件）
	 * @param orConditions （OR 查询语句条件）
	 * @param queryConditions（分页，排序查询条件）
	 * @param tableName （表名）
	 * @return
	 */
	public static String selectSql(Class<?> clazz,List<String> colums,Map<String, Object> andConditions,Map<String, Object> orConditions,SortPage sortPage,String tableName){
		String selectSql = null;
		try {
			Object object = clazz.newInstance();
			if(null==tableName || "".equals(tableName)){
				tableName = getTableName(object);
			}
			if(null!=tableName && !"".equals(tableName)){
				StringBuffer selectStr = new StringBuffer("SELECT ");
				StringBuffer whereStr = new StringBuffer(" FROM "+tableName+" WHERE 1=1");
				if(null!=colums && 0!=colums.size()){
					for (String colum : colums) {
						selectStr.append(colum+",");
					}
				}
				List<Field> fieldList = covertFieldList(object);
				if(null != fieldList && fieldList.size() > 0){
					for (Field field : fieldList) {
						boolean nextFieldFlag = false;
						Colum dbAnno = field.getAnnotation(Colum.class);
						if (null != colums && colums.size() > 0) { 
							for (String colum : colums) {
								if(null==dbAnno.columName() || "".equals(dbAnno.columName())){
									if(colum.equalsIgnoreCase(field.getName())){
										nextFieldFlag = true;
										break;
									}
								}else{
									if(colum.equalsIgnoreCase(dbAnno.columName())){
										nextFieldFlag = true;
										break;
									}
								}
							} 
						}
						if(nextFieldFlag){
							continue;
						}
						if(null==dbAnno.columName() || "".equals(dbAnno.columName())){
							selectStr.append(field.getName()+ ",");
						}else{
							selectStr.append(dbAnno.columName()+ ",");
						}
					}
				}
				
				//and条件语句
				if (andConditions != null && andConditions.size() > 0) {  
					Iterator<String> conditionKeys = andConditions.keySet().iterator();  
					while (conditionKeys.hasNext()) {  
						String conditionKey = (String) conditionKeys.next();  
						whereStr.append(" AND "+conditionKey + " = ");  
						if(null==andConditions.get(conditionKey) || isNumberValue(andConditions.get(conditionKey)) || andConditions.get(conditionKey) instanceof Boolean){
							whereStr.append(andConditions.get(conditionKey));
						}else{
							whereStr.append("'" + andConditions.get(conditionKey) + "'"); 
						}
					}  
				}
				//or条件语句
				if (orConditions != null && orConditions.size() > 0) {  
					Iterator<String> conditionKeys = orConditions.keySet().iterator();  
					while (conditionKeys.hasNext()) {  
						String conditionKey = (String) conditionKeys.next();  
						whereStr.append(" OR "+conditionKey + " = ");  
						if(null==orConditions.get(conditionKey) || isNumberValue(orConditions.get(conditionKey)) || orConditions.get(conditionKey) instanceof Boolean){
							whereStr.append(orConditions.get(conditionKey));
						}else{
							whereStr.append("'" + orConditions.get(conditionKey) + "'"); 
						}
					}  
				}
				//query条件语句
				if (sortPage != null ) { 
					String sortRule = SortPage.ASC;
					if(null!=sortPage.getSortKey() && !"".equals(sortPage.getSortKey())){
						whereStr.append(" ORDER BY "+sortPage.getSortKey()); 
						if(Constant.DESC.equalsIgnoreCase(sortPage.getSortRule())){
							sortRule = sortPage.getSortRule();
						}
						whereStr.append(" "+sortRule+" ");
					}
					if(sortPage.getStart()>=0 && sortPage.getLimit()>0){
						int start = sortPage.getStart();
						int limit = sortPage.getLimit();
						whereStr.append(" LIMIT "+start+", "+limit);
					}
				}
				selectSql = selectStr.toString().substring(0, selectStr.length()-1)
						+whereStr.toString().replaceFirst(" 1=1 AND ", " ").replaceFirst(" 1=1 OR ", " "); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectSql;
	}
	
	public static String countSql(Class<?> clazz,Map<String, Object> andConditions,Map<String, Object> orConditions,String tableName){
		String selectSql = null;
		try {
			Object object = clazz.newInstance();
			if(null==tableName || "".equals(tableName)){
				tableName = getTableName(object);
			}
			if(null!=tableName && !"".equals(tableName)){
				StringBuffer selectStr = new StringBuffer("SELECT count(1) ");
				StringBuffer whereStr = new StringBuffer(" FROM "+tableName+" WHERE 1=1");
				
				//and条件语句
				if (andConditions != null && andConditions.size() > 0) {  
					Iterator<String> conditionKeys = andConditions.keySet().iterator();  
					while (conditionKeys.hasNext()) {  
						String conditionKey = (String) conditionKeys.next();  
						whereStr.append(" AND "+conditionKey + " = ");  
						if(null==andConditions.get(conditionKey) || isNumberValue(andConditions.get(conditionKey)) || andConditions.get(conditionKey) instanceof Boolean){
							whereStr.append(andConditions.get(conditionKey));
						}else{
							whereStr.append("'" + andConditions.get(conditionKey) + "'"); 
						}
					}  
				}
				//or条件语句
				if (orConditions != null && orConditions.size() > 0) {  
					Iterator<String> conditionKeys = orConditions.keySet().iterator();  
					while (conditionKeys.hasNext()) {  
						String conditionKey = (String) conditionKeys.next();  
						whereStr.append(" OR "+conditionKey + " = ");  
						if(null==orConditions.get(conditionKey) || isNumberValue(orConditions.get(conditionKey)) || orConditions.get(conditionKey) instanceof Boolean){
							whereStr.append(orConditions.get(conditionKey));
						}else{
							whereStr.append("'" + orConditions.get(conditionKey) + "'"); 
						}
					}  
				}
				//query条件语句
				selectSql = selectStr.toString().substring(0, selectStr.length()-1)
						+whereStr.toString().replaceFirst(" 1=1 AND ", " ").replaceFirst(" 1=1 OR ", " "); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectSql;
	}
	
	public static String deleteSql(Class<?> clazz,String tableName,Map<String, Object> andConditions,Map<String, Object> orConditions){
		String deleteSql = null;
		try {
			if(null==tableName || "".equals(tableName)){
				Object object = Class.forName(clazz.getName()).newInstance();
				tableName = getTableName(object);
			}
			StringBuffer deleteStr = new StringBuffer("DELETE FROM "+tableName+" WHERE 1=1");
			//and条件语句
			if (andConditions != null && andConditions.size() > 0) {  
				Iterator<String> conditionKeys = andConditions.keySet().iterator();  
				while (conditionKeys.hasNext()) {  
					String conditionKey = (String) conditionKeys.next();  
					deleteStr.append(" AND "+conditionKey + " = ");  
					if(null==andConditions.get(conditionKey) || isNumberValue(andConditions.get(conditionKey)) || andConditions.get(conditionKey) instanceof Boolean){
						deleteStr.append(andConditions.get(conditionKey));
					}else{
						deleteStr.append("'" + andConditions.get(conditionKey) + "'"); 
					}
				}  
			}
			//or条件语句
			if (orConditions != null && orConditions.size() > 0) {  
				Iterator<String> conditionKeys = orConditions.keySet().iterator();  
				while (conditionKeys.hasNext()) {  
					String conditionKey = (String) conditionKeys.next();  
					deleteStr.append(" OR "+conditionKey + " = ");  
					if(null==orConditions.get(conditionKey) || isNumberValue(orConditions.get(conditionKey)) || orConditions.get(conditionKey) instanceof Boolean){
						deleteStr.append(orConditions.get(conditionKey));
					}else{
						deleteStr.append("'" + orConditions.get(conditionKey) + "'"); 
					}
				}  
			}
			
			deleteSql = deleteStr.toString().replaceFirst(" 1=1 AND ", " ").replaceFirst(" 1=1 OR ", " ");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return deleteSql;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Field> covertFieldList(Object object){
		List<Field> list = new ArrayList<>();
		Class superClass = object.getClass().getSuperclass();
		while(true){
			if(superClass != null){
				Field[] superFields = superClass.getDeclaredFields();
				for (Field field : superFields) {
					if(field.isAnnotationPresent(Colum.class)){
						list.add(field);
					}
				}
			}else{
				break;
			}
			superClass = superClass.getSuperclass();
		}
		Field[] objFields = object.getClass().getDeclaredFields();
		if (objFields != null && objFields.length > 0){
			for (Field field : objFields) {
				if(field.isAnnotationPresent(Colum.class)){
					list.add(field);
				}
			}
		}
		return list;
	}
	
    private static Object getFieldValue(Object object, Field field) {  
        String name = field.getName();  
        Method method = null;  
        Object methodValue = null;  
        try {  
        	PropertyDescriptor pd = new PropertyDescriptor(name, object.getClass());
        	method = pd.getReadMethod();
        } catch (SecurityException e1) {  
           e1.printStackTrace();
        } catch (IntrospectionException e) {
			e.printStackTrace();
		}  
        if (null != method) {  
            try {  
                methodValue = method.invoke(object);  
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {  
                e.printStackTrace();
            }  
        }  
        return methodValue;  
    }  
	 
	private static boolean isNumberValue(Object obj){
		boolean flag = false;
		if((obj instanceof Double)||(obj instanceof Integer)||(obj instanceof Float)||(obj instanceof Long)||(obj instanceof Byte)){
			flag = true;
		}
		return flag;
	} 
	
	private static boolean isDate(Object obj){
		boolean flag = false;
		if((obj instanceof Date)){
			flag = true;
		}
		return flag;
	} 
	
	private static String getTableName(Object obj){
		String tableName = null;
		if (obj.getClass().isAnnotationPresent(TableName.class)){
			tableName = obj.getClass().getAnnotation(TableName.class).tableName();
		}
		return tableName;
	}
}
