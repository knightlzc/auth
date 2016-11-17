<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/inc_4_platform/include_header.inc" %>
<c:set var="searchUrl" value="${ctx }/role/loadRoleList"/>
<script type="text/javascript">
$(function(){
	var searchUrl = "${searchUrl}";
	$("#role_table").load(searchUrl);
// 	$('#roleId').popover({
// 		'placement':'right',
// 		'content':'角色id不可为空',
// 		'container': 'body',
// 		'trigger':'manual'
// 	});
// 	$('#roleName').popover({
// 		'placement':'right',
// 		'content':'角色名不可为空',
// 		'container': 'body',
// 		'trigger':'manual'
// 	});
	$('#addRoleModal').on('hide.bs.modal', function (e) {
		  initModel();
	});
})

//查询
function search(){
  var searchUrl="${searchUrl}";
  window.location.href=searchUrl+"?pageNum="+1;
}

//跳页
function go(pageNum){
  var searchUrl="${searchUrl}";
  if(null==pageNum || pageNum<1){
      pageNum=1;
  }
  searchUrl += "?pageNum="+pageNum;
  $("#role_table").html("数据加载中。。。");
  $("#role_table").load(searchUrl)
}
function jump(){
  var pageNum = $("#go_page").val();
  go(pageNum);
}

function saveRole(){
    $("#saveRoleBtn").attr("disabled");
    
    if(!checkParam()){
    	return;
    }
    
    $("#msg_text").removeClass();
    $("#msg_text").html("保存中。。。");
    $("#msg_text").show();
	$.ajax({
        url: "${ctx}/role/save",
        type: 'post',
        data:  $("#roleAddForm").serialize(),
        dataType: 'json',
        success: function(data) {
            if(data.suc) {
            	$("#msg_text").addClass("text-success");
			    $("#msg_text").html("保存成功");
            	$("#saveRoleBtn").removeAttr("disabled");
            } else {
            	$("#msg_text").addClass("text-danger");
			    $("#msg_text").html(data.msg);
            	$("#saveRoleBtn").removeAttr("disabled");
            }
        },
        error:function(r){
            	$("#msg_text").addClass("text-danger");
			    $("#msg_text").html("网络异常");
            	$("#saveRoleBtn").removeAttr("disabled");
        }
    });
}
function checkParam(){
	var roleId = $("#roleId").val();
	if(checkNull(roleId)){
		$('#roleId').popover('show');
		$('#roleName').popover('hide');
		return false;
	}else{
		$('#roleId').popover('hide');
	}
	var roleName = $("#roleName").val();
	if(checkNull(roleName)){
		$('#roleName').popover('show');
		return false;
	}else{
		$('#roleName').popover('hide');
	}
	return true;
	
}
function initModel(){
	$("#msg_text").html("");
	$("#msg_text").removeClass();
    $("#msg_text").hide();
    $('#roleId').popover('hide');
    $('#roleName').popover('hide');
}

/**
 * 检查是否为空
 * @param data 传入值
 * @returns {Boolean} 如果为空，undefined或者null则返回false；否则返回true
 */
function checkNull(data){
    if(null == data || undefined == data ||$.trim(data).length == 0){
        return true;
    }else{
        return false;
    }
}
</script>
			<div>
				<div class="row">
					<ol class="breadcrumb">
					  <li><a href="javascipt:;">系统管理</a></li>
					  <li class="active">角色列表</li>
					</ol>
<!-- 	                <div class="col-lg-12"> -->
<!-- 	                    <h3 class="page-header">角色列表</h3> -->
<!-- 	                </div> -->
<!-- 						<div class=""> -->
<!--                             <button class="btn btn-primary" data-toggle="modal" data-target="#myModal"> -->
<!--                                	 添加 -->
<!--                             </button> -->
<!-- 		                </div> -->
		                <div class="col-lg-12">
		                    <h4 class="page-header">角色列表</h4>
			                <div class="row">
						            <div class="padding-bootom fr">
						                <button data-toggle="modal" data-target="#addRoleModal" class="btn btn-primary" id="newadd">新增角色</button>
						            </div>
							</div>
		                </div>
			            <div id="role_table">
			            
			            </div>
			            
			            <div class="panel-body">
                            <!-- Button trigger modal -->
                            <!-- Modal -->
                            <div class="modal fade bs-example-modal-sm" id="addRoleModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="margin-top: 40px;overflow-y: hidden">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                            <h4 class="modal-title" id="myModalLabel">新增角色</h4>
                                        </div>
                                        <div class="modal-body">
                                        	<form id="roleAddForm">
	                                        	<div class="input-group wd-full">
												  <span class="input-group-addon wd-20" id="">角色id</span>
												  <input id="roleId" name="roleId" type="text" class="form-control" data-content="角色id不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
	                                        	<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">角色名</span>
												  <input id="roleName" name="roleName" type="text" class="form-control" data-content="角色名不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
	                                        	<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">作用域</span>
												  <select id="realm" name="realm" class="form-control" data-container="body" data-toggle="popover">
												  		<option value=''>请选择</option>
												  		<option value='auth'>权限平台</option>
												  		<option value='51fenqi'>51平台</option>
												  </select>
												</div>
                                        	</form>
                                        	<div class="input-group wd-full top-10 text-center">
												  <span  class="" id="msg_text" style="display:none"></span>
											</div>
                                        </div>
                                        <div class="modal-footer">
                                            <a type="button" class="btn btn-default" data-dismiss="modal">关闭</a>
                                            <a id="saveRoleBtn" href="javascript:saveRole();" type="button" class="btn btn-primary">保存</a>
                                        </div>
                                    </div>
                                    <!-- /.modal-content -->
                                </div>
                                <!-- /.modal-dialog -->
                            </div>
                            <!-- /.modal -->
                        </div>
				</div>
	
	            
			</div>
            
