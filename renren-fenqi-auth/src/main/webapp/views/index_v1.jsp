<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/inc_4_platform/include_header.inc" %>
<!DOCTYPE html>
<html >
  <head>
    <%@include file="/inc_4_platform/head.inc" %>

    <title>分期管理平台</title>
  </head>
<style>

</style>
  <body>

    <div id="wrapper">

      <!-- Sidebar -->
      <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="javascript:;">分期管理平台</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse navbar-ex1-collapse">
          <ul class="nav navbar-nav side-nav" id="leftmenu">
            <li class="dropdown " >
              <a href="#" class="dropdown-toggle left-sub-menu" > 用户管理</a>
              <ul class="dropdown-menu" >
                <li class="dropdown">
                	<a href="#" class="dropdown-toggle left-sub-menu" >二级菜单1</a>
		              <ul class="dropdown-menu" style="position:relative">
		                <li class=""><a href="#" class="dropdown-toggle left-sub-menu">三级菜单1</a></li>
		                <li class=""><a href="#" class="dropdown-toggle left-sub-menu">三级菜单2</a></li>
		                <li class=""><a href="#" class="dropdown-toggle left-sub-menu">三级菜单3</a></li>
		                <li class=""><a href="#" class="dropdown-toggle left-sub-menu">三级菜单4</a></li>
		              </ul>
                </li>
                <li class="dropdown"><a href="#" class="dropdown-toggle left-sub-menu">二级菜单2</a></li>
                <li class="dropdown"><a href="#" class="dropdown-toggle left-sub-menu">二级菜单3</a></li>
                <li class="dropdown"><a href="#" class="dropdown-toggle left-sub-menu">二级菜单4</a></li>
              </ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"> 车辆管理</a>
              <ul class="dropdown-menu">
                <li><a href="#">Dropdown Item</a></li>
                <li><a href="#">Another Item</a></li>
                <li><a href="#">Third Item</a></li>
                <li><a href="#">Last Item</a></li>
              </ul>
            </li>
          </ul>

        </div><!-- /.navbar-collapse -->
      </nav>
      
      <div id="main_div">dfsfd</div>

    </div><!-- /#wrapper -->

    <!-- JavaScript -->
    <script src="${ctx }/static/js/jquery-1.10.2.js"></script>
    <script src="${ctx }/static/js/index.js"></script>
    <script src="${ctx }/static/js/bootstrap.js"></script>
    

    <!-- Page Specific Plugins -->    
<%--     <script src="${ctx }/static/js/raphael-min.js"></script> --%>
<%--     <script src="${ctx }/static/js/morris.js"></script> --%>
<%--     <script src="${ctx }/static/js/morris/chart-data-morris.js"></script> --%>
    <script src="${ctx }/static/js/tablesorter/jquery.tablesorter.js"></script>
    <script src="${ctx }/static/js/tablesorter/tables.js"></script>

  </body>
</html>
