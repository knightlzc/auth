/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p> 
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: DBFieldAnnotation.java
 * @Package knight.annotation
 * @date 2014-6-19 下午6:44:27 
 */
package com.renren.fenqi.dbtool.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD) 
public @interface Colum {
	String columName();
}
