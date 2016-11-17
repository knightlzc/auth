package com.renren.fenqi.dbtool.page;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 人人公司   http://www.renren-inc.com</p> 
 * <p>Description: 分页工具类 </p> 
 * <p>Author:zicui.liu/刘子萃</p>
 */
public class PagingUtil {

    
    public final static int DEF_PAGE_SIZE= 10;
	
	/**
	 * 
	 * <br/>Description:获取分页对象(数据库)
	 * <p>Author:zcliu/刘子萃</p>
	 * @param content
	 * @param pageNim
	 * @param pageSize
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public static <T extends Object> Page<T> getPage(List<T> content ,int pageNum,int pageSize,int count) {
		Page<T> page = new Page<T>();
		page.setContent(content);
		// 起始记录条数
		int start = (pageNum - 1) * pageSize + 1;
		page.setStart(start);
		// 查询记录条数
		page.setPageSize(pageSize);
		// 当前页码
		page.setPageNum(pageNum);
		// 总记录数
		int totalNum = count;
		page.setTotalNum(totalNum);
		// 总页数
		int totalPages = 0;
		if(totalNum > 0){
			if(totalNum % pageSize == 0){
				totalPages = totalNum / pageSize;
			}else{
				totalPages = (totalNum / pageSize) + 1;
			}
		}
		page.setTotalPages(totalPages);
		return page;
	}
	
	/**
	 * <br/>Description:页码计算
	 * <p>Author:zicui.liu/刘子萃</p>
	 * @param page
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getPaginationList(Page<?> page, int number) {
		List<Integer> paginationList = new ArrayList<Integer>();
		
		try {
			//总页数
			int totalPages = page.getTotalPages();
			
			//当前页码
			int currentNumber = page.getPageNum();
			
			//当前是第几屏，比如总共6页。number是5，第一屏幕显示 1,2,3,4,5；第二屏显示6,1——5的pageSizeNum为1,6的为2(此处实际所得的pageSizeNum+1才是第几屏幕数)
			
			int pageSizeNum = currentNumber/number;
			
			//取余数
			double remainder = currentNumber%number;
			//排除整数 比如第5页，其实是第一屏，但是根据上面算法会是第二屏
			if(remainder == 0.0){
				pageSizeNum = pageSizeNum - 1;
			}
			//计算显示的起始页码
			int startPage = pageSizeNum*number+1;
			if(0 >= startPage) {
				startPage = 1;
			}
//			else if(0 < startPage) {
//				if(startPage+number > totalPages) {
//					if(totalPages > number) {
//						startPage = totalPages-number+1;
//					}else {
//						startPage = 1;
//					}
//				}
//			}
			
			boolean flag = true;
			while(flag) {
				paginationList.add(startPage);
				//到达最大页数 或 到达指定显示数时跳出
				if(startPage >= totalPages || paginationList.size() == number) {
					flag = false;
				}
				startPage++ ;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return paginationList;
	}
	
}
