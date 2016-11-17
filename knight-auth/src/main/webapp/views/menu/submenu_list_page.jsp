<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/inc_4_platform/include_header.inc" %>
<c:set var="searchUrl" value="${ctx}/menu/loadMenuList"/>
<c:set var="parentId" value="${parentId }"/>
<c:set var="grandId" value="${grandId }"/>
<c:set var="subMenuUrl" value="${ctx}/menu/subMenuListPage"/>
<c:set var="menuUrl" value="${ctx}/menu/menuListPage"/>
<c:set var="level" value="${level}"/>

<script type="text/javascript">
$(function(){
	var searchUrl = "${searchUrl}"+"?parentId="+${parentId}+"&pageNum="+${pageNum};
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
	$('#addMenuModal').on('hide.bs.modal', function (e) {
		$("#msg_text").html("");
		$("#msg_text").removeClass();
	    $("#msg_text").hide();
	});
	
	$('#editMenuModal').on('hide.bs.modal', function (e) {
		$("#edit_msg_text").html("");
		$("#edit_msg_text").removeClass();
	    $("#edit_msg_text").hide();
	});
	
})

function go(pageNum){
	  var searchUrl="${searchUrl}";
	  if(null==pageNum || pageNum<1){
	      pageNum=1;
	  }
	  searchUrl += "?parentId=${parentId }&pageNum="+pageNum;
	  $("#role_table").html("数据加载中。。。");
	  $("#role_table").load(searchUrl)
}
	
 function goBack(){
	    var level = "${level}";
	    var url = "";
	    if(level==0){
	    	url= "${menuUrl}"+"?parentId="+${parentId}+"&pageNum=${pageNum}";
	    }else{
	    	url ="${subMenuUrl}"+"?parentId="+${grandId}+"&pageNum=${pageNum}";
	    }
		$('#page-wrapper').load(url);
 }
 

 
function editMenu(){
	$("#editMenuBtn").attr("disabled");
	$("#edit_msg_text").removeClass();
    $("#edit_msg_text").html("保存中。。。");
    $("#edit_msg_text").show();
   /*  if(!checkParam()){
    	return;
    } */
    var id =$("#edit_id").val();
    var menuTitle = $("#edit_menuTitle").val();
    var realm = $("#edit_realm").val();
    var menuDesc = $("#edit_menuDesc").val();
    var sort = $("#edit_sort").val();
    var menuUrl=$("#edit_menuUrl").val();
    var ctx = "${ctx}";
	$.ajax({
        url: ctx+"/menu/edit",
        type: 'post',
        data:{
        	"id":id,
        	"menuTitle":menuTitle,
        	"realm":realm,
        	"menuDesc":menuDesc,
        	"menuUrl":menuUrl,
        	"parentId":${parentId },
        	"sort":sort
        } ,
        dataType: 'json',
        success: function(data) {
            if(data.suc) {
            	$("#edit_msg_text").addClass("text-success");
			    $("#edit_msg_text").html("保存成功");
            	$("#editMenuBtn").removeAttr("disabled");
            } else {
            	$("#edit_msg_text").addClass("text-danger");
			    $("#edit_msg_text").html(data.msg);
            	$("#editMenuBtn").removeAttr("disabled");
            }
        },
        error:function(r){
            	$("#edit_msg_text").addClass("text-danger");
			    $("#edit_msg_text").html("网络异常");
            	$("#editMenuBtn").removeAttr("disabled");
        }
    });
}

function checkParam(){
	 var menuTitle = $("#edit_menuTitle").val();
	if(checkNull(menuTitle)){
		$('#edit_menuTitle').popover('show');
		return false;
	}else{
		$('#edit_menuTitle').popover('hide');
	}
	var sort = $("#edit_sort").val();
	if(checkNull(sort)){
		$('#edit_sort').popover('show');
		return false;
	}else{
		if(!/^[0-9]*$/.test(sort)){
			$('#edit_sort').attr("data-content","请输入数字");
			$('#edit_sort').popover('show');
			return false;
		}
		$('#edit_sort').popover('hide');
	}
	var realm =  $("#edit_realm").val();
	if(checkNull(realm)){
		$('#edit_realm').popover('show');
		return false;
	}else{
		$('#edit_realm').popover('hide');
	}
	
	var  menuUrl =  $("#edit_menuUrl").val();
	if(checkNull(menuUrl)){
		$('#edit_menuUrl').popover('show');
		return false;
	}else{
		$('#edit_menuUrl').popover('hide');
	}
	
	return true;
	
}
</script>
			<div>
				<div class="row">
					<ol class="breadcrumb">
					  <li><a href="javascipt:;">系统管理</a></li>
					  <li class="active">子菜单列表</li>
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
		                    <h4 class="page-header">子菜单列表</h4>
			                <div class="row">
			                 <a id="goback" href="javascript:goBack();"  >返回</a>
						            <div class="padding-bootom fr">
						                <button data-toggle="modal" data-target="#addMenuModal" class="btn btn-primary" id="newadd">新增菜单</button>
						            </div>
							</div>
		                </div>
			            <div id="role_table">
			            
			            </div>
			            
			            <div class="panel-body">
                            <!-- Button trigger modal -->
                            <!-- Modal -->
                            <div class="modal fade bs-example-modal-sm" id="addMenuModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="margin-top: 40px;overflow-y: hidden">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                            <h4 class="modal-title" id="myModalLabel">新增资源</h4>
                                        </div>
                                        <div class="modal-body">
                                        	<form id="roleAddForm">
                                        	     <input id="parentId" name="parentId" type="text" value="${parentId }" hidden="true">
	                                        	<div class="input-group wd-full">
												  <span class="input-group-addon wd-20" id="">资源名称</span>
												  <input id="menuTitle" name="menuTitle" type="text" class="form-control" data-content="资源名称不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
	                                        	<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">作用域</span>
												  <select id="realm" name="realm" class="form-control" data-container="body" data-toggle="popover">
												  		<option value=''>请选择</option>
												  		<option value='auth'>权限平台</option>
												  		<option value='51fenqi'>51平台</option>
												  </select>
												</div>
												<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">排序</span>
												  <input id="sort" name="sort" type="text" class="form-control" data-content="排序不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
												<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">资源url</span>
												  <input id="menuUrl" name="menuUrl" type="text" class="form-control" data-content="资源url不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
												<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">资源描述</span>
												  <input id="menuDesc" name="menuDesc" type="text-area" class="form-control" data-content="排序不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
                                        	</form>
                                        	<div class="input-group wd-full top-10">
											  <center>
												  <span  class="" id="msg_text" style="display:none"></span>
											  </center>
											</div>
                                        </div>
                                        <div class="modal-footer">
                                            <a type="button" class="btn btn-default" data-dismiss="modal">关闭</a>
                                            <a id="saveMenuBtn" href="javascript:saveMenu();" type="button" class="btn btn-primary">保存</a>
                                        </div>
                                    </div>
                                    <!-- /.modal-content -->
                                </div>
                                <!-- /.modal-dialog -->
                            </div>
                            <div class="modal fade bs-example-modal-sm" id="editMenuModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"  style="margin-top: 40px;overflow-y: hidden">
                               <input id="edit_id" value="" type="text" hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                            <h4 class="modal-title" id="myModalLabel">编辑资源</h4>
                                        </div>
                                        <div class="modal-body">
                                        	<form id="roleAddForm">
	                                        	<div class="input-group wd-full">
												  <span class="input-group-addon wd-20" id="">资源名称</span>
												  <input id="edit_menuTitle" name="edit_menuTitle" type="text" class="form-control" data-content="菜单名称不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
	                                        	<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">作用域</span>
												  <select id="edit_realm" name="realm" class="form-control" data-container="body" data-toggle="popover">
												  		<option value=''>请选择</option>
												  		<option value='auth'>权限平台</option>
												  		<option value='51fenqi'>51平台</option>
												  </select>
												</div>
												<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">排序</span>
												  <input id="edit_sort" name="edit_sort" type="text" class="form-control" data-content="排序不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
												<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">资源url</span>
												  <input id="edit_menuUrl" name="edit_menuUrl" type="text" class="form-control" data-content="排序不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
												<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">资源描述</span>
												  <input id="edit_menuDesc" name="edit_menuDesc" type="text" class="form-control" data-content="资源描述不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
                                        	</form>
                                        	<div class="input-group wd-full top-10">
											  <center>
												  <span  class="" id="edit_msg_text" style="display:none"></span>
											  </center>
											</div>
                                        </div>
                                        <div class="modal-footer">
                                            <a type="button" class="btn btn-default" data-dismiss="modal">关闭</a>
                                            <a id="editMenuBtn" href="javascript:editMenu();" type="button" class="btn btn-primary">更新</a>
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
            
