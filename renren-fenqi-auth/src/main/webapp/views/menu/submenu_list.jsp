<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/inc_4_platform/include_header.inc" %>
<link rel="stylesheet" type="text/css" href="${ctx }/static/css/datatable/jquery.dataTables.css">
<%-- <script src="${ctx }/static/js/datatable/jquery.dataTables.js"></script> --%>
<script type="text/javascript">

$(function(){
// 	$('#role_table').DataTable();
})

</script>


			<div class="row">
                <div class="col-lg-12">
                <input type="text" value="${pageNum }" hidden="true" id="pageNum">
                    <div class="panel panel-default">
                        <!-- /.panel-heading -->
                        <div class="">
                            <div class="">
                                <table class="table table-striped table-bordered table-hover" id="role_table">
                                    <thead>
                                        <tr>
                                            <th>序号</th>
                                            <th>资源名称</th>
                                            <th>作用域</th>
                                            <th>排序</th>
                                            <th>资源url</th>
                                            <th>资源描述</th>
                                            <th>创建时间</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                   	<c:if test="${not empty contents}">
								     	<c:forEach var="item" items="${contents}" varStatus="status">
	                                        <tr class=" ">
	                                            <td>${status.count }</td>
	                                            <td>${item.menuTitle }</td>
	                                            <td>${item.realm }</td>
	                                            <td>${item.sort }</td>
	                                            <td>${item.menuUrl }</td>
	                                            <td>${item.menuDesc }</td>
	                                            <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss" type="both"/></td>
	                                             <td><%-- href="javascript:detailMenu(${item.id });" --%>
	                                             <!-- data-target="#editMenuModal" -->
	                                                  <button  class='btn btn-primary btn-sm' onclick="javascript:detailMenu(${item.id });"   id="editMenu_${item.id }">编辑资源</button>
	                                                 &nbsp; &nbsp;
	                                                <%--  ${ctx}/menu/subMenuListPage?parentId=${item.id } --%>
	                                                 <a id="addSubMenuBtn" href="javascript:gotoSubMenu(${item.id });"  >添加子资源</a>
	                                             </td>
	                                        </tr>
								     	</c:forEach>
							     	</c:if>
							     	<c:if test="${ empty contents}">
							     		<tr class=" ">
                                            <td colspan="5" align="center">无记录</td>
                                        </tr>
							     	</c:if>
                                    </tbody>
                                </table>
                                <div style="margin-top: -20px">
							        <c:if test="${not empty contents}">
										<%@include file="/inc_4_platform/page.inc" %>
							     	</c:if>
							    </div>
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
