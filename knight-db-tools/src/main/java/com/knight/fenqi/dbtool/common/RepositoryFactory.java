package com.renren.fenqi.dbtool.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class RepositoryFactory {
	private static final Log log = LogFactory.getLog(RepositoryFactory.class);
	
	private static Map<String, Object> map = new HashMap<String, Object>();
	
	private RepositoryFactory() {}
	
	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz) {
		T t = (T)map.get(clazz.getName());
		
		if(null == t) {
			try {
				t = (T)Class.forName(clazz.getName()).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				log.error(e);
			}
			map.put(clazz.getName(), t);
		}
		
		return t;
	}
}
