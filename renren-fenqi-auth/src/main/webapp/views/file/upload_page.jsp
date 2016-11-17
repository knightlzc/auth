<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="searchUrl" value="/role/loadRoleList"/>
<script type="text/javascript" src="/static/js/jquery-form-3.46.js"></script>
<script type="text/javascript">

function upload(){
	alert($("#orderFile").val());
	var path = $("#orderFile").val();
	$("#file_name").html("上传中。。。");
	$("#fileForm").ajaxSubmit({
        url: "/file/upload",
        type: 'post',
//         data:  $("#fileForm").serialize(),
        dataType: 'json',
        success: function(data) {
            if(data.suc) {
			    $("#file_name").html(data.data);
			    $("#downloadBtn").attr("href","http://fastdfs.fenqi.d.xiaonei.com/"+data.data);
            } else {
			    $("#file_name").html("上传失败");
            	
            }
        },
        error:function(r){
			    $("#file_name").html("网络异常");
        }
    });
}
$(function (){
	
});

/*if (typeof $w.RSAUtils === 'undefined') */

// RSAUtils.encryptString('14110933383322153778','010001','008baf14121377fc76eaf7794b8a8af17085628c3590df47e6534574efcfd81ef8635fcdc67d141c15f51649a89533df0db839331e30b8f8e4440ebf7ccbcc494f4ba18e9f492534b8aafc1b1057429ac851d3d9eb66e86fce1b04527c7b95a2431b07ea277cde2365876e2733325df04389a9d891c5d36b7bc752140db74cb69f');
</script>
			<div>
				<div class="row">
					<ol class="breadcrumb">
					  <li><a href="javascipt:;">系统管理</a></li>
					  <li class="active">文件管理</li>
					</ol>
		                <div class="col-lg-12">
		                    <h4 class="page-header">文件上传</h4>
		                </div>
			            <div id="file_upload_div">
			            
			            	<form id='fileForm'>
				            	<div class="form-group">
									<input type="hidden" name="orderId" value="${orderId }"/>
									<input type="file" name="orderFile" id="orderFile" class=""/>
	                            </div>
								<a id="uploadBtn" href="javascript:upload();" class="btn btn-primary btn-sm ">上传</a>
			            	</form>
		            	<div class="form-group">
		            		<lable>文件名</lable>
							<span id="file_name"><span/>
                          </div>
							<a id="downloadBtn" target="_blank" href="" class="btn btn-primary btn-sm ">下载</a>
			            </div>
			            
				</div>
	
	            
			</div>
            
