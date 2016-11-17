<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/inc_4_platform/include_header.inc" %>
<c:set var="searchUrl" value="${ctx }/role/loadRoleList"/>
<script type="text/javascript">
</script>
			<div>
				<div class="row">
					<ol class="breadcrumb">
					  <li><a href="javascipt:;">系统管理</a></li>
					  <li href="javascipt:;">角色列表</li>
					  <li class="active">角色配置</li>
					</ol>
		                <div class="col-lg-12">
		                    <h4 class="page-header">角色配置</h4>
		                    <div class="btn-group">
						      <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Primary <span class="caret"></span></button>
						      <ul class="dropdown-menu" role="menu">
						        <li><a href="javascript:;">分配菜单</a></li>
	<!-- 					        <li><a href="#">Another action</a></li> -->
	<!-- 					        <li><a href="#">Something else here</a></li> -->
	<!-- 					        <li class="divider"></li> -->
	<!-- 					        <li><a href="#">Separated link</a></li> -->
						      </ul>
							</div>
		                </div>
			            <div id="role_table" class="listform">
				            <div class="form-wrap">
								<div class="listrow">
									<div class="wd-30">
										<span>id：</span>
										<span>${role.roleId}</span>
									</div>
									<div class="wd-30">
										<span>名称：</span>
										<span>${role.roleName}</span>
									</div>
									<div class="wd-30">
										<span>作用域：</span>
										<span>${role.realm}</span>
									</div>
								</div>
							</div>
			            </div>
				</div>
	            
			</div>
            
