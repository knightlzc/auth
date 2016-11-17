/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司   http://www.renren-inc.com</p> 
 * <p>Author:zicui.liu/刘子萃</p>
 * @Title: Page.java
 * @Package com.renren.fenqi.carloan.common
 * @date 2015-1-31 上午11:12:53 
 */
package com.renren.fenqi.dbtool.page;

import java.io.Serializable;
import java.util.List;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司   http://www.renren-inc.com</p> 
 * <p>Description:  </p> 
 * <p>Author:zicui.liu/刘子萃</p>
 * @param <T>
 */

public class Page<T> implements Serializable {
	
	private static final long serialVersionUID = -1349342623806342943L;

	/**
	 * 当前页码
	 */
	private int pageNum;
	
	/**
	 * 总页数
	 */
	private int totalPages;
	
	/**
	 * 总记录数
	 */
	private int totalNum;

	/**
	 * 起始记录数
	 */
	private int start;
	
	/**
	 * 查询记录条数
	 */
	private int pageSize;
	
	/**
	 * 查询结果
	 */
	private List<T> content;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

}
