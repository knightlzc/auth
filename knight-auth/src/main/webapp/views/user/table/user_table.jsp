<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/inc_4_platform/include_header.inc" %>
<link rel="stylesheet" type="text/css" href="${ctx }/static/css/datatable/jquery.dataTables.css">
<link rel="stylesheet" href="${ctx }/static/css/ztree/metroStyle/metroStyle.css" type="text/css">
<script src="${ctx }/static/js/datatable/jquery.dataTables.js"></script>
<script src="${ctx }/static/js/ztree/jquery.ztree.core-3.5.js"></script>
<script src="${ctx }/static/js/ztree/jquery.ztree.excheck-3.5.js"></script>
<script src="${ctx }/static/js/ztree/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript">
var setting = {
        view: {
            addHoverDom: false,
            removeHoverDom: false,
            selectedMulti: false
        },
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    },
    config_msg_text = $("#config_msg_text");
    
    
$(function(){
	$('#configModal').on('hide.bs.modal', function (e) {
		config_msg_text.html("");
		config_msg_text.removeClass();
	    config_msg_text.hide();
		$("#bizId").val("");
		$("#roleTree").html("");
	});
})

function loadRoleTree(userId){
	$("#bizId").val(userId);
	$.ajax({
        url: "${ctx}/ztree/loadRoles",
        type: 'post',
        data:  {userId:userId},
        dataType: 'json',
        success: function(data) {
            if(data.suc) {
				var zNodes = data.data;
				$.fn.zTree.init($("#roleTree"), setting, zNodes);
				$('#configModal').modal('show')
            } else {
	        	config_msg_text.addClass("text-danger");
			    config_msg_text.html(data.msg);
	        	config_msg_text.show();
            }
        },
        error:function(r){
        	config_msg_text.addClass("text-danger");
		    config_msg_text.html("网络异常");
        	config_msg_text.show();
        }
    });
}

function saveUserRole(){
	var userId = $("#bizId").val();
	var roleIds = getNodeIds();
	
	$.ajax({
        url: "${ctx}/user/saveUserRole",
        type: 'post',
        data:  {userId:userId,roleIds:JSON.stringify(roleIds)},
        dataType: 'json',
        success: function(data) {
            if(data.suc) {
        		config_msg_text.addClass("text-success");
		   		config_msg_text.html(data.msg);
	        	config_msg_text.show();
            } else {
        		config_msg_text.addClass("text-danger");
		    	config_msg_text.html(data.msg);

            }
        },
        error:function(r){
        	config_msg_text.addClass("text-danger");
		    config_msg_text.html("网络异常");
            	
        }
    });
}
function getNodeIds(){
	var treeObj=$.fn.zTree.getZTreeObj("roleTree"),
    nodes=treeObj.getCheckedNodes(true),
    nodIds=[];
    for(var i=0;i<nodes.length;i++){
// 	    alert(nodes[i].id); 
	    nodIds.push(nodes[i].id);
    }
// 	    alert(nodIds); 
    return nodIds;
}
function initModel(){
	config_msg_text.html("");
	config_msg_text.removeClass();
    config_msg_text.hide();
	$("#bizId").val("");
	$("#roleTree").html("");
}

</script>


			<div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <!-- /.panel-heading -->
                        <div class="">
                            <div class="">
                                <table class="table table-striped table-bordered table-hover" id="role_table">
                                    <thead>
                                        <tr>
                                            <th>序号</th>
                                            <th>用户id</th>
                                            <th>登陆名</th>
                                            <th>昵称</th>
                                            <th>创建时间</th>
                                            <th>操作</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                   	<c:if test="${not empty contents}">
								     	<c:forEach var="item" items="${contents}" varStatus="status">
	                                        <tr class=" ">
	                                            <td>${status.count }</td>
	                                            <td>${item.id }</td>
	                                            <td>${item.name }</td>
	                                            <td>${item.nickName }</td>
	                                            <td><fmt:formatDate value="${item.registerTime }" pattern="yyyy-MM-dd HH:mm:ss" type="both"/></td>
	                                            <td>
		                                            <div class="btn-group">
												      <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">请选择 <span class="caret"></span></button>
												      <ul class="dropdown-menu " style='min-width:50px' role="menu">
												        <li class=''><a href="javascript:loadRoleTree('${item.id }');">分配角色</a></li>
												      </ul>
													</div>
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
            
       		<div class="panel-body">
                       <!-- Button trigger modal -->
                       <!-- Modal -->
                       <div class="modal fade bs-example-modal-sm" id="configModal" tabindex="-1" role="dialog" aria-labelledby="configLabel" aria-hidden="true" style="margin-top: 40px;">
                           <div class="modal-dialog">
                               <div class="modal-content">
                                   <div class="modal-header">
                                       <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                       <h4 class="modal-title" id="configLabel">分配角色</h4>
                                   </div>
                                   <div class="modal-body" id="">
                                   		<input type="hidden" id="bizId" value="">
                                   		<div>
	                                   		<ul id="roleTree" class="ztree"></ul>
                                   		</div>
                                   		<div class="input-group wd-full top-10 text-center">
											  <span  class="" id="config_msg_text" style="display:none"></span>
										</div>
                                   </div>
                                   <div class="modal-footer">
                                       <a type="button" class="btn btn-default" data-dismiss="modal">关闭</a>
                                       <a id="saveRoleBtn" href="javascript:saveUserRole();" type="button" class="btn btn-primary">保存</a>
                                   </div>
                               </div>
                               <!-- /.modal-content -->
                           </div>
                           <!-- /.modal-dialog -->
                       </div>
                       <!-- /.modal -->
                  </div>
            
