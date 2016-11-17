<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/inc_4_platform/include_header.inc" %>
<c:set var="searchUrl" value="${ctx}/menu/loadMenuList"/>
<c:set var="subMenuUrl" value="${ctx}/menu/subMenuListPage"/>


<script type="text/javascript">
$(function(){
	var searchUrl = "${searchUrl}"+"?parentId=0&pageNum=${pageNum}";
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

//查询
function search(pageNum){
  var searchUrl="${searchUrl}";
  window.location.href=searchUrl+"?parentId=0&pageNum="+pageNum;
}

//跳页
function go(pageNum){
  var searchUrl="${searchUrl}";
  if(null==pageNum || pageNum<1){
      pageNum=1;
  }
  searchUrl += "?parentId=0&pageNum="+pageNum;
  $("#role_table").html("数据加载中。。。");
  $("#role_table").load(searchUrl)
}
function jump(){
  var pageNum = $("#go_page").val();
  go(pageNum);
}

function saveMenu(){
    $("#saveMenuBtn").attr("disabled");
    
    if(!checkParam()){
    	return;
    }
    
    $("#msg_text").removeClass();
    $("#msg_text").html("保存中。。。");
    $("#msg_text").show();
    var ctx = "${ctx}";
	$.ajax({
        url: ctx+"/menu/save",
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
function detailMenu(id){
	  var ctx = "${ctx}";
	  $.ajax({
        url: ctx+"/menu/detail",
        type: 'get',
        data: {"id":id,"pageNum":1} ,
        dataType: 'json',
        success: function(data) {
           if(data.suc) {
//         	    $("#editMenu_"+id).attr('data-target','#editMenuModal');
//         	    $("#editMenu_"+id).attr('data-toggle','modal');
//         	    $("#editMenu_"+id).trigger('click');
            	$("#edit_menuTitle").val(data.renrenData.menuTitle);
            	$("#edit_menuDesc").val(data.renrenData.menuDesc);
            	$("#edit_sort").val(data.renrenData.sort);
            	$("#edit_id").val(id);
            	$("#edit_menuUrl").val(data.renrenData.menuUrl);
            	$("#edit_realm option[value="+data.renrenData.realm+"]").attr("selected",true);
            	$("#editMenuModal").modal("show");
           }
        },
        error:function(r){
            	$("#msg_text").addClass("text-danger");
			    $("#msg_text").html("网络异常");
			   
            	//$("#editMenuModal").removeAttr("disabled");
        }
    }); 
	
}

function editMenu(){
	$("#editMenuBtn").attr("disabled");
	$("#edit_msg_text").removeClass();
    $("#edit_msg_text").html("保存中。。。");
    $("#edit_msg_text").show();
    var id =$("#edit_id").val();
    var menuTitle = $("#edit_menuTitle").val();
    var realm = $("#edit_realm").val();
    var menuDesc = $("#edit_menuDesc").val();
    var sort = $("#edit_sort").val();
   // var menuUrl=$("#menuUrl").val();
    var ctx = "${ctx}";
	$.ajax({
        url: ctx+"/menu/edit",
        type: 'post',
        data:{
        	"id":id,
        	"menuTitle":menuTitle,
        	"realm":realm,
        	"menuDesc":menuDesc,
        	"menuUrl":'',
        	"parentId":0,
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
            	$("#msg_text").addClass("text-danger");
			    $("#msg_text").html("网络异常");
            	$("#editMenuBtn").removeAttr("disabled");
        }
    });
}

function gotoSubMenu(id){
	var pageNum=$("#pageNum").val();
	var url = "${subMenuUrl}"+"?parentId="+id+"&pageNum="+pageNum;
	$('#page-wrapper').load(url);
}

function checkParam(){
	/* var roleId = $("#roleId").val();
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
	} */
	return true;
	
}
function initModel(){
	$("#msg_text").html("");
	$("#msg_text").removeClass();
    $("#msg_text").hide();
  /*   $('#roleId').popover('hide');
    $('#roleName').popover('hide'); */
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
					  <li class="active">菜单列表</li>
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
		                    <h4 class="page-header">菜单列表</h4>
			                <div class="row">
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
                                        	     <input id="parentId" name="parentId" type="text" value="0" hidden="true">
	                                        	 <input id="menuUrl" name="menuUrl" type="text" value="" hidden="true">
	                                        	<div class="input-group wd-full">
												  <span class="input-group-addon wd-20" id="">资源名称</span>
												  <input id="menuTitle" name="menuTitle" type="text" class="form-control" data-content="菜单名称不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
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
												  <input id="edit_sort" name="sort" type="text" class="form-control" data-content="排序不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
												</div>
												<div class="input-group wd-full top-10">
												  <span class="input-group-addon wd-20" id="">资源描述</span>
												  <input id="edit_menuDesc" name="menuDesc" type="text" class="form-control" data-content="排序不可为空" data-container="body" data-toggle="popover" data-placement="right"  data-trigger='manual'>
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
            
