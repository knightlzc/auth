<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/inc_4_platform/include_header.inc" %>
<c:set var="searchUrl" value="${ctx }/user/loadUserList"/>
<script type="text/javascript">

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
  $("#user_table").html("数据加载中。。。");
  $("#user_table").load(searchUrl)
}
function jump(){
  var pageNum = $("#go_page").val();
  go(pageNum);
}

function saveUser(){
    $("#saveUserBtn").attr("disabled");
    
    if(!checkParam()){
    	return;
    }
    
    $("#msg_text").removeClass();
    $("#msg_text").html("保存中。。。");
    $("#msg_text").show();
	$.ajax({
        url: "${ctx}/user/save",
        type: 'post',
        data:  $("#userAddForm").serialize(),
        dataType: 'json',
        success: function(data) {
            if(data.suc) {
            	$("#msg_text").addClass("text-success");
			    $("#msg_text").html("保存成功");
            	$("#saveUserBtn").removeAttr("disabled");
            } else {
            	$("#msg_text").addClass("text-danger");
			    $("#msg_text").html(data.msg);
            	$("#saveUserBtn").removeAttr("disabled");
            }
        },
        error:function(r){
            	$("#msg_text").addClass("text-danger");
			    $("#msg_text").html("网络异常");
            	$("#saveUserBtn").removeAttr("disabled");
        }
    });
}
function checkParam(){
	var name = $("#name").val();
	if(checkNull(name)){
		$('#name').popover('show');
		$('#nickName').popover('hide');
		$('#password').popover('hide');
		return false;
	}else{
		$('#name').popover('hide');
	}
	var nickName = $("#nickName").val();
	if(checkNull(nickName)){
		$('#nickName').popover('show');
		$('#password').popover('hide');
		$('#confirmPassword').popover('hide');
		return false;
	}else{
		$('#roleName').popover('hide');
	}
	var password = $("#password").val();
	var confirmPassword = $("#confirmPassword").val();
	if(checkNull(password)){
		$('#password').attr('data-content','密码需要至少8位数字于字母组合');
		$('#password').popover('show');
		return false;
	}else if(password != confirmPassword){
		$('#password').attr('data-content','两次输入的密码不一致');
		$('#password').popover('show');
		return false;
	}else{
		$('#password').popover('hide');
	}
	
	return true;
	
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
$(function(){
		var searchUrl = "${searchUrl}";
		$("#user_table").load(searchUrl);
		$('#addUserModal').on('hide.bs.modal', function (e) {
			$("#msg_text").html("");
			$("#msg_text").removeClass();
		    $("#msg_text").hide();
		    $('#name').val('');
		    $('#nickName').val('');
		    $('#password').val('');
		    $('#name').popover('hide');
		    $('#nickName').popover('hide');
		    $('#password').popover('hide');
		});
}) 
 
</script>
			<div>
				<div class="row">
					<ol class="breadcrumb">
					  <li><a href="javascript:;">用户管理</a></li>
					  <li class="active">用户列表</li>
					</ol>
		                <div class="col-lg-12">
		                    <h4 class="page-header">用户列表</h4>
			                <div class="row">
						            <div class="padding-bootom fr">
						                <button data-toggle="modal" data-target="#addUserModal" class="btn btn-primary" id="newadd">新增用户</button>
						            </div>
							</div>
		                </div>
			            <div id="user_table">
			            
			            </div>
			            
			            <div class="panel-body">
                            <!-- Button trigger modal -->
                            <!-- Modal -->
                            <div class="modal fade bs-example-modal-sm" id="addUserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="margin-top: 40px;overflow-y: hidden">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                            <h4 class="modal-title" id="myModalLabel">新增用户</h4>
                                        </div>
                                        <div class="modal-body">
                                        	<form id="userAddForm">
	                                        	<div class="input-group wd-full">
												  <span class="input-group-addon wd-20" id="">登陆名</span>
												  <input id="name" name="name" type="text" class="form-control" data-content="登录名不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
	                                        	<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">昵称</span>
												  <input id="nickName" name="nickName" type="text" class="form-control" data-content="昵称不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
	                                        	<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">密码</span>
												  <input id="password" name="password" type="password" class="form-control" data-content="密码不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
	                                        	<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">确认密码</span>
												  <input id="confirmPassword" name="confirmPassword" type="password" class="form-control" data-content="密码不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
                                        	</form>
                                        	<div class="input-group wd-full top-10 text-center">
												  <span  class="" id="msg_text" style="display:none"></span>
											</div>
                                        </div>
                                        <div class="modal-footer">
                                            <a type="button" class="btn btn-default" data-dismiss="modal">关闭</a>
                                            <a id="saveUserBtn" href="javascript:saveUser();" type="button" class="btn btn-primary">保存</a>
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
            
