/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Description:  </p>
 * <p>Author:zcliu/刘子萃</p>
 * @Title: SortPage.java
 * @Package knight.common
 * @date 2014-6-24 上午9:41:06 
 */
package com.renren.fenqi.dbtool.common;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Description:  </p> 
 * <p>Author:zcliu/刘子萃</p>
 */

public class SortPage {

	public final static String DESC = "DESC";
	
	public final static String ASC = "ASC";
	
	private int pageNum;
	
	private int pageSize;
	
	private int start;
	
	private int limit;
	
	private String sortRule;
	
	private String sortKey;
	
	public SortPage(){
		
	}
	
	public SortPage(int pageNum,int pageSize,String sortKey,String sortRule){
		if(pageNum<=0){
			pageNum = 1;
		}
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		
		this.setStart((pageNum-1)*pageSize);
		this.limit = pageSize;
		this.sortRule = sortRule;
		this.sortKey = sortKey;
	}
	public SortPage(String sortKey,String sortRule){
		this.sortRule = sortRule;
		this.sortKey = sortKey;
	}
	public SortPage(int pageNum,int pageSize){
		if(pageNum<=0){
			pageNum = 1;
		}
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		
		this.setStart((pageNum-1)*pageSize);
		this.limit = pageSize;
	}


	public String getSortRule() {
		return sortRule;
	}

	public void setSortRule(String sortRule) {
		this.sortRule = sortRule;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public int getPageNum(){
		return pageNum;
	}

	public void setPageNum(int pageNum){
		this.pageNum = pageNum;
	}

	public int getPageSize(){
		return pageSize;
	}

	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}

	public int getStart(){
		return start;
	}

	public void setStart(int start){
		this.start = start;
	}

	public int getLimit(){
		return limit;
	}

	public void setLimit(int limit){
		this.limit = limit;
	}
	
}
