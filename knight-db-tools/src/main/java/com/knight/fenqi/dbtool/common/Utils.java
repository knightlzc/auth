package com.renren.fenqi.dbtool.common;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.regex.Pattern;

public class Utils {
	private static Lock lock = null;
	
	

	public static String getWebInfPath() {
		URL url = Utils.class.getProtectionDomain().getCodeSource().getLocation();
		String path = url.toString();
		
		int index = path.indexOf("WebContent");
		if (index == -1) {
			index = path.indexOf("WEB-INF");
		}

		if (index == -1) {
			index = path.indexOf("build");
		}

		if (index == -1) {
			index = path.indexOf("classes");
		}

		if (index == -1) {
			index = path.indexOf("bin");
		}
		
		path = path.substring(0, index);

		if (path.startsWith("zip")) {// 当class文件在war中时，此时返回zip:D:/...这样的路径
			path = path.substring(4);
		} else if (path.startsWith("file")) {// 当class文件在class文件中时，此时返回file:/D:/...这样的路径
			path = path.substring(5);
		} else if (path.startsWith("jar")) {// 当class文件在jar文件里面时，此时返回jar:file:/D:/...这样的路径
			path = path.substring(10);
		}
		try {
			path = URLDecoder.decode(path, "UTF-8");
			if(path.indexOf("WebContent") == -1) {
				path += "WebContent/WEB-INF/";
			}else if(path.indexOf("WEB-INF") == -1) {
				path += "WEB-INF/";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return path;
	}
	
	

	public static String generateUUID() {
		lock.lock();
		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		lock.unlock();
		
		return uuid;
	}
	
	public static class ExecutorsService {
		private static ExecutorService es = null;
		
		static {
			//es = Executors.newCachedThreadPool();
			es = Executors.newFixedThreadPool(5);
		}
		
		static public void execute(Runnable command) {
			es.execute(command);
		}
		
		static public <T> Future<T> submit(Callable<T> task) {
			return es.submit(task);
		}
	}

	/**
	 * 
	 * <br/>Description:判断操作系统是否是windows
	 * 
	 * @return
	 */
	public static boolean isWindowsOS(){
		boolean boo = false;
		try{
			Properties sp = System.getProperties();
			String osName = sp.getProperty("os.name");
			int osInt = osName.toLowerCase().indexOf("win");

			if(osInt != - 1){
				boo = true;
			}
		}catch(Exception e){
			System.out.println("获取操作系统名称异常。");
		}

		return boo;
	}

	/**
	 * 
	 * <br/>Description:获取serverip配置文件路径
	 * @return
	 */
	static public String getRootPath() {
		String rootPath = null;
		if(Utils.isWindowsOS()) {
			rootPath = "c:/";
		}else {
			rootPath = "/etc/";
		}
		
		return rootPath;
	}

	/**
	 * 
	 * <br/>Description:获取ip
	 * @param request
	 * @return
	 */
//	static public String getIpAddr(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)?"127.0.0.1":ip;
//    }
	/**
	 * 
	 * <br/>Description:读取配置文件
	 * @param fileName
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getProperties(String fileName, String key) throws Exception{
		java.util.Properties properties = new java.util.Properties();
		String rootPath = Utils.getRootPath();
		
		java.io.FileInputStream fileStream = new java.io.FileInputStream(rootPath+"icloudq/"+fileName);
		properties.load(fileStream);
		fileStream.close();
		String value = properties.getProperty(key);
		
		return value;
	}

	
	/**
	 * 
	 * <br/>Description:获取分页对象(数据库)
	 * <p>Author:zcliu/刘子萃</p>
	 * @param content
	 * @param pageNo
	 * @param pageSize
	 * @param count
	 * @return
	 * @throws Exception
	 */
	
	
	/**
	 * <br/>Description:格式化数字
	 * @param value 传入值
	 * @param min 保留最小小数位数
	 * @param max 保留最大小数位数
	 * @return
	 */
	public static String formatNum(double value,int min,int max){
		String retValue = null;
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(min);
		df.setMaximumFractionDigits(max);
		retValue = df.format(value);
		retValue = retValue.replaceAll(",","");
		return retValue;
	}
	
	/**
	 * <br/>Description:发送邮件
	 * <p>Author:zcliu/刘子萃</p>
	 * @param user
	 * @param subject
	 * @param content
	 * @param toAddr
	 * @param isSendHtmlMail
	 * @throws Exception
	 */
	
	/**
	 * <br/>Description:
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String formatDate(String pattern,Date date){
		String dateStr = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		dateStr = sdf.format(date);
		return dateStr;
	}
	

	
	/**
	 * <br/>Description:SQL注入判断
	 * @param str
	 * @return true：表示有SQL注入，false表示没有SQL注入
	 */
	public static boolean sqlValidate(String str){
		String reg = "(?:\\//)|(?:\\/)|(?:\\#)|(?:\\%)|(?:\\*)|(?:,)|(?:;)|(?:')|(?:\\+)|(?:\\=)|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|" +
				"(\\b(select|update|and|or|delete|insert|trancate|char|chr|into|substr|ascii|declare|exec|count|mid|master|into|create|drop|" +
				"execute|union|where|order|by|like|group)\\b)";		
		Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		if(sqlPattern.matcher(str).find()){
			return true;
		}
		return false;
	}
	
	/**
	 * <br/>Description:判断是否为数字
	 * <p>Author:zcliu/刘子萃</p>
	 * @param obj
	 * @return
	 */
	public static boolean isNumberValue(Object obj){
		boolean flag = false;
		if((obj instanceof Double)||(obj instanceof Integer)||(obj instanceof Float)||(obj instanceof Long)||(obj instanceof Byte)){
			flag = true;
		}
		return flag;
	}
	
	public static boolean isDate(Object obj){
		boolean flag = false;
		if(obj instanceof Date){
			flag = true;
		}
		return flag;
	}
}
