/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com </p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: JedisUtils.java
 * @Package com.renren.auth.tools.redis
 * @date 2016-11-15 下午4:49:08 
 */
package com.renren.auth.tools.redis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司  http://www.renren-inc.com</p> 
 * <p>Description:redis工具类  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class JedisUtils{

private static final Log log = LogFactory.getLog(JedisUtils.class);
	
	public static String CONF_FILE = "redis_client.conf";
	private static Properties cacheProperties;
	private static GenericObjectPoolConfig jedisPoolConfig;
	
	private static ShardedJedisPool shardedJedisPool;
	
	private static List<JedisShardInfo> jedisShardInfos;
	
	//add by wangting
	private static JedisSentinelPool sentinelPool ;
	private static Set<String> sentinels;
//	private static Jedis jedis;
	static {
		init();
	}
	
	/**
	 * <br/>Description:初始化
	 * <p>Author:zcliu/刘子萃</p>
	 */
	private static synchronized void init() {
		try {
			cacheProperties = new Properties();
			cacheProperties.load(JedisUtils.class.getResourceAsStream(CONF_FILE));
			
			jedisPoolConfig = new GenericObjectPoolConfig();
			jedisPoolConfig.setMaxTotal(Integer.parseInt(cacheProperties.getProperty("cache.maxActive")));
			jedisPoolConfig.setMaxIdle(Integer.parseInt(cacheProperties.getProperty("cache.maxIdle")));
			jedisPoolConfig.setMaxWaitMillis(Integer.parseInt(cacheProperties.getProperty("cache.maxWait")));
			jedisPoolConfig.setTestOnBorrow(Boolean.parseBoolean(cacheProperties.getProperty("cache.testOnBorrowt")));
			
			String urls = cacheProperties.getProperty("cache.url");
			String[] urlsArray = urls.split(";");
			//add by wangting
			if("true".equals(cacheProperties.getProperty("cache.ha"))){
				sentinels = new HashSet<String>();
				for(String url : urlsArray) {
					 sentinels.add(url);
				}
				String mastername=cacheProperties.getProperty("cache.mastername");
				String password=cacheProperties.getProperty("cache.password");
				sentinelPool = new JedisSentinelPool(mastername, sentinels,jedisPoolConfig,password);
			}else{
				jedisShardInfos = new ArrayList<JedisShardInfo>();
				
				for(String url : urlsArray) {
					String[] urlInfo = url.split(":");
					
					//ip:port
					JedisShardInfo jedisShardInfo = new JedisShardInfo(urlInfo[0].trim().toString(),Integer.parseInt(urlInfo[1].trim().toString()));
					
					//auth
					if(urlInfo.length > 2) {
						jedisShardInfo.setPassword(urlInfo[2].trim().toString());
					}
					jedisShardInfos.add(jedisShardInfo);
				}
				//pool
				shardedJedisPool = new ShardedJedisPool(jedisPoolConfig,jedisShardInfos);
			}
			
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	public static ShardedJedis getJedis() {
		if(null == shardedJedisPool) {
			init();
		}
		return shardedJedisPool.getResource();
	}
	
	public static Jedis getSentinelJedis() {
		if(null == sentinelPool) {
			init();
		}
		return sentinelPool.getResource();
	}
	
	
	
	
	/**
	 * <br/>Description:模糊查询
	 * <p>Author:zcliu/刘子萃</p>
	 * @param key
	 * @return
	 */
	public static Map<String, String> like(String key) {
		if("true".equals(cacheProperties.getProperty("cache.ha"))){
			Jedis j = getSentinelJedis();
			try {
				Map<String, String> map = new HashMap<String, String>();
				Set<String> sets = j.keys("*"+key+"*");
				Iterator<String> _iter = sets.iterator();
				while(_iter.hasNext()) {
					String _key = _iter.next();
					String value = j.get(_key);
					map.put(_key, value);
				}
				return map;
			} catch (Exception e) {
				throw e;
			}finally{
				close(j);
			}
		}else{
			ShardedJedis jedis = getJedis();
			try {
				Map<String, String> map = new HashMap<String, String>();
				Collection<Jedis> c = jedis.getAllShards();
				Iterator<Jedis> iter = c.iterator();
				while(iter.hasNext()) {
					Jedis j = iter.next();
					Set<String> sets = j.keys("*"+key+"*");
					Iterator<String> _iter = sets.iterator();
					while(_iter.hasNext()) {
						String _key = _iter.next();
						String value = j.get(_key);
						map.put(_key, value);
					}
				}
				return map;
			} catch (Exception e) {
				throw e;
			}finally{
				closeShardedJedis(jedis);
			}
		}
		
	}
	
	/**
	 * <br/>Description:左侧匹配模糊查询
	 * <p>Author:zcliu/刘子萃</p>
	 * @param key
	 * @return
	 */
	public static Map<String, String> getMapLeftLike(String key) {
		if("true".equals(cacheProperties.getProperty("cache.ha"))){
			Jedis j = getSentinelJedis();
			try {
				Map<String, String> map = new HashMap<String, String>();
				Set<String> sets = j.keys(key+"*");
				Iterator<String> _iter = sets.iterator();
				while(_iter.hasNext()) {
					String _key = _iter.next();
					String value = j.get(_key);
					map.put(_key, value);
				}
				return map;
			} catch (Exception e) {
				throw e;
			}finally{
				close(j);
			}
		}else{
			ShardedJedis jedis = getJedis();
			try {
				Map<String, String> map = new HashMap<String, String>();
				Collection<Jedis> c = jedis.getAllShards();
				Iterator<Jedis> iter = c.iterator();
				while(iter.hasNext()) {
					Jedis j = iter.next();
					Set<String> sets = j.keys(key+"*");
					Iterator<String> _iter = sets.iterator();
					while(_iter.hasNext()) {
						String _key = _iter.next();
						String value = j.get(_key);
						map.put(_key, value);
					}
				}
				return map;
			} catch (Exception e) {
				throw e;
			}finally{
				closeShardedJedis(jedis);
			}
		}
		
	}
	/**
	 * <br/>Description:左侧匹配模糊查询
	 * <p>Author:zcliu/刘子萃</p>
	 * @param key
	 * @return
	 */
	public static List<String> getListLeftLike(String key) {
		if("true".equals(cacheProperties.getProperty("cache.ha"))){
			Jedis j = getSentinelJedis();
			try {
				List<String> list = new ArrayList<String>();
				Set<String> sets = j.keys(key+"*");
				Iterator<String> _iter = sets.iterator();
				while(_iter.hasNext()) {
					String _key = _iter.next();
					String value = j.get(_key);
					list.add(value);
				}
				return list;
			} catch (Exception e) {
				throw e;
			}finally{
				close(j);
			}
		}else{
			ShardedJedis jedis = getJedis();
			try {
				List<String> list = new ArrayList<String>();
				Collection<Jedis> c = jedis.getAllShards();
				Iterator<Jedis> iter = c.iterator();
				while(iter.hasNext()) {
					Jedis j = iter.next();
					Set<String> sets = j.keys(key+"*");
					Iterator<String> _iter = sets.iterator();
					while(_iter.hasNext()) {
						String _key = _iter.next();
						String value = j.get(_key);
						list.add(value);
					}
				}
				return list;
			} catch (Exception e) {
				throw e;
			}finally{
				closeShardedJedis(jedis);
			}
		}
		
	}
	
	/**
	 * <br/>Description:对象序列化之后存储
	 * <p>Author:zcliu/刘子萃</p>
	 * @param key
	 * @param object
	 * @return
	 */
	public static String addObject(String key, Object object) {
		ShardedJedis jedis = null;
		try{
			jedis = getJedis();
			String result = jedis.set(key.getBytes(),SerializeUtils.serialize(object));
			return result;
		}catch(Exception e){
			return null;
		}finally{
			closeShardedJedis(jedis);
		}
	}
	
	/**
	 * <br/>Description:对象序列化之后存储并且设定超时时间
	 * <p>Author:zcliu/刘子萃</p>
	 * @param key
	 * @param object
	 * @param timeout
	 * @return
	 */
	public static String addObject(String key, Object object,int timeout) {
		ShardedJedis jedis = null;
		try{
			jedis = getJedis();
			String result = jedis.setex(key.getBytes(),timeout,SerializeUtils.serialize(object));
			return result;
		}catch(Exception e){
			log.error(e);
			return null;
		}finally{
			closeShardedJedis(jedis);
		}
	}
	
	public static Object getObject(String key) {
		ShardedJedis jedis = null;
		try{
			
			jedis = getJedis();
			byte[] bytes = jedis.get(key.getBytes());
			return SerializeUtils.unserialize(bytes);
		}catch(Exception e){
			log.error(e);
			return null;
		}finally{
			closeShardedJedis(jedis);
		}
		
	}
	
	public static String add(String key, String value) {
		if("true".equals(cacheProperties.getProperty("cache.ha"))){
			Jedis j = getSentinelJedis();
			String result = j.set(key, value);
			return result;
		}else{
			ShardedJedis jedis = getJedis();
			String result = jedis.set(key, value);
			return result;
		}
	}

	public static String add(String key, String value, int seconds) {
		if("true".equals(cacheProperties.getProperty("cache.ha"))){
			Jedis j = getSentinelJedis();
			try {
				String result = j.setex(key, seconds, value);
				return result;
			} catch (Exception e) {
				throw e;
			}finally{
				close(j);
			}
		}else{
			ShardedJedis jedis = getJedis();
			try {
				String result = jedis.setex(key, seconds, value);
				return result;
				
			} catch (Exception e) {
				throw e;
			}finally{
				closeShardedJedis(jedis);
			}
		}
		
		
	}

	public static String get(String key) {
		if("true".equals(cacheProperties.getProperty("cache.ha"))){
			Jedis j = getSentinelJedis();
			try {
				String data = j.get(key);
				return data;
			} catch (Exception e) {
				throw e;
			}finally{
				close(j);
			}
		}else{
			ShardedJedis jedis = getJedis();
			try {
				String data = jedis.get(key);
				return data;
			} catch (Exception e) {
				throw e;
			}finally{
				closeShardedJedis(jedis);
			}
		}
		
	}
	
	public static Long del(String key) {
		if("true".equals(cacheProperties.getProperty("cache.ha"))){
			Jedis j = getSentinelJedis();
			try {
				Long result = j.del(key);
				return result;
				
			} catch (Exception e) {
				throw e;
			}finally{
				if(null != j){
					close(j);
				}
			}
		}else{
			ShardedJedis jedis = getJedis();
			try {
				Long result = jedis.del(key);
				return result;
				
			} catch (Exception e) {
				throw e;
			}finally{
				closeShardedJedis(jedis);
			}
		}
		
	}
	
	/**
	 * <br/>Description:模糊量删除
	 * <p>Author:zcliu/刘子萃</p>
	 * @param keys
	 * @return
	 */
	public static Long delLike(String key) {
		if("true".equals(cacheProperties.getProperty("cache.ha"))){
			Jedis j = getSentinelJedis();
			try {
				long result = 0;
				Set<String> keys = j.keys(key + "*");  
				result = j.del(keys.toArray(new String[keys.size()]));
				return result;
				
			} catch (Exception e) {
				throw e;
			}finally{
				close(j);
			}
		}else{
			ShardedJedis jedis = getJedis();
			try {
				Collection<Jedis> c = jedis.getAllShards();
				Iterator<Jedis> iter = c.iterator();
				long result = 0;
				while(iter.hasNext()) {
					Jedis _jedis = iter.next();  
					Set<String> keys = _jedis.keys(key + "*");  
					result += _jedis.del(keys.toArray(new String[keys.size()]));
				}
				return result;
				
			} catch (Exception e) {
				throw e;
			}finally{
				
			}
		}
		
	}
	/**
	 * <br/>Description:批量删除
	 * <p>Author:zcliu/刘子萃</p>
	 * @param keys
	 * @return
	 */
	public static Long delByKeys(String[] keys) {
		if("true".equals(cacheProperties.getProperty("cache.ha"))){
			Jedis j = getSentinelJedis();
			try {
				long result = 0;  
				result = j.del(keys);  
				return result;
			} catch (Exception e) {
				throw e;
			}finally{
				close(j);
			}
		}else{
			ShardedJedis jedis = getJedis();
			try {
				Collection<Jedis> jedisC = jedis.getAllShards();  
				Iterator<Jedis> iter = jedisC.iterator();  
				long result = 0;  
				while (iter.hasNext()) {  
					Jedis _jedis = iter.next();  
					result += _jedis.del(keys);  
				}  
				return result;
			} catch (Exception e) {
				throw e;
			}finally{
				closeShardedJedis(jedis);
			}
		}
		
	}
	
	public static void close(Jedis jedis){
		if(null != jedis){
			jedis.close();
		}
	}
	public static void closeShardedJedis(ShardedJedis jedis){
		if(null != jedis){
			jedis.close();
		}
	}
	
	public static void main(String[] args){
		
	}
	
}
