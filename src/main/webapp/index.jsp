<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <%
        String fileName = request.getParameter("fileName");
        request.setAttribute("fileName", fileName);
    %>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Excel Upload</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/bootstrap/css/bootstrap.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/css/ionicons.min.css">
    <!-- DataTables -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/plugins/datatables/dataTables.bootstrap.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/dist/css/skins/_all-skins.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!-- ./wrapper -->
    <script src="${pageContext.request.contextPath}/js/angularjs/angular.js"></script>
    <script src="${pageContext.request.contextPath}/js/userjs/index.js"></script>
    <!-- jQuery 2.1.4 -->
    <script src="${pageContext.request.contextPath}/js/plugins/jQuery/jQuery-2.1.4.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="${pageContext.request.contextPath}/js/bootstrap/js/bootstrap.min.js"></script>
    <%--<script src="../bower_components/bootstrap-filestyle/src/bootstrap-filestyle.js"></script>--%>
    <!--[if lt IE 9] -->
    <script src="${pageContext.request.contextPath}/js/css/html5shiv.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/css/respond.min.js"></script>
    <![endif]-->
</head>
<body class="sidebar-mini skin-blue-light sidebar-collapse layout-boxed">
<div class="wrapper" ng-app="myApp">

    <header class="main-header">
        <a href="#" class="logo">
            <!-- mini logo for sidebar mini 50x50 pixels -->
            <span class="logo-mini"><i class="fa fa-upload"></i></span>
            <!-- logo for regular state and mobile devices -->
            <span class="logo-lg"><i class="fa fa-upload"></i></span>
        </a>
        <!-- Header Navbar: style can be found in header.less -->
        <nav class="navbar navbar-static-top" role="navigation">
            <!-- Sidebar toggle button-->

            <div class="navbar-custom-menu">
                <ul class="nav navbar-nav">
                    <!-- User Account: style can be found in dropdown.less -->
                    <li class="dropdown user user-menu">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <img src="${pageContext.request.contextPath}/js/dist/img/user2-160x160.jpg"
                                 class="user-image"
                                 alt="User Image">
                            <span class="hidden-xs">Web ExcelUpload</span>
                        </a>
                        <ul class="dropdown-menu">
                            <!-- User image -->
                            <li class="user-header">
                                <img src="${pageContext.request.contextPath}/js/dist/img/user2-160x160.jpg"
                                     class="img-circle"
                                     alt="User Image">

                                <p>
                                    Excel - Web Uploading
                                    <small>Member since Dec. 2015</small>
                                </p>
                            </li>
                        </ul>
                    </li>
                    <!-- Control Sidebar Toggle Button -->
                    <li>
                        <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
                    </li>
                </ul>
            </div>
        </nav>
    </header>

    <!-- Content Wrapper. Contains page content -->
    <div class="content" ng-controller="myController">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Excel
                <small>黑名单录入</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Excel</a></li>
                <li class="active">Upload</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <!-- file input -->
            <form name="myform" action="${pageContext.request.contextPath}/service/uploadExcel.do" method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <div class="col-sm-11">
                        <input name="file1" ui-jq="filestyle" type="file"
                               data-icon="false"
                               data-classButton="btn btn-default"
                               data-classInput="form-control inline v-middle input-s">
                    </div>
                    <%--<div class="col-sm-1">--%>
                    <button class="btn btn-default" type="submit">
                        <i class="fa fa-cloud-upload"></i>
                    </button>
                    <%--</div>--%>
                </div>
            </form>
            <br>
            <!-- title -->
            <div class="col-sm-12">
                <div class="box box-primary">

                    <table class="table m-b-none">
                        <tbody>
                        <tr>
                            <td>
			<span class="user">
					<strong>当前文件：</strong>
					<span class="text-danger"><c:if test="${fileName!=null}"><i
                            class="fa fa-file-excel-o margin-r-5"></i><%=fileName%>
                    </c:if>
                    </span>
                </span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <strong>categories：</strong>
                                <select ng-model="selected" ng-options="m.categories for m in model">
                                    <option value="">-- 选择 --</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-user margin-r-5"></i>name</span>
                                    <input ng-model="dboname" type="text" class="form-control"/>
                                </div>
                            </td>
                            <td>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-credit-card margin-r-5"></i>id_card</span>
                                    <input ng-model="dboid_card" class="form-control">
                                </div>
                            </td>
                            <td>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-mobile-phone margin-r-5"></i>mobiles</span>
                                    <input ng-model="dbomobiles" class="form-control">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-usd margin-r-5"></i>debt</span>
                                    <input ng-model="dbodebt" type="text" class="form-control">
                                </div>
                            </td>
                            <td>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-tags margin-r-5"></i>sources</span>
                                    <input ng-model="dbosources" type="text" class="form-control"
                                           placeholder="默认等于categories">
                                </div>
                            </td>
                            <td>
                                <div class="input-group">
                                    <span class="input-group-addon danger"><i
                                            class="glyphicon glyphicon-remove margin-r-5"></i>清  除</span>
                                    <input ng-model="dboclear" type="text" class="form-control">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <button class="btn btn-primary" type="button" ng-disabled="uploadStatus!=1"
                                        ng-click="loadFile()">查看Excel数据
                                </button>
                                <button class="btn btn-success" type="button" ng-disabled="mongoStatus!=1"
                                        ng-click="lookExcel()">生成mongo数据
                                </button>
                            </td>
                            <td>
                                <span class="pull-right text-danger">{{saveFlagMsg}}</span>
                            </td>
                            <td>
                                <button class="btn btn-warning" ng-disabled="saveStatus!=1" type="button"
                                        ng-click="saveExcel2Mongo()">存储数据
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- Excel原始数据展示 -->
            <div class="col-sm-12" ng-if="excel1">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <%--<span class="label btn-danger pull-right m-t-xs">文件结果</span>--%>
                        <a class="text-bold">{{successMsg}}</a>
                    </div>
                    <table class="table table-striped m-b-none">
                        <thead>
                        <tr>
                            <td></td>
                            <td ng-repeat="td in reportData[0] track by $index">
                                <%--<input ng-if="mapDbo" type="text" name="input{{$index}}" value="{{mapDbo[$index]}}">--%>
                                <%--<select  ng-model="selected"  ng-options="m.productName for m in model">--%>
                                <%--<option value="">-- 请选择 --</option>--%>
                                <%--</select>--%>
                                <label class="label label-info">{{$index}}</label>
                            </td>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="data in reportData track by $index">
                            <td>{{$index+1}}</td>
                            <td ng-repeat="td in data track by $index">{{td}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="col-md-12" ng-if="excel2">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#activity" data-toggle="tab">Text View</a></li>
                        <li><a href="#timeline" data-toggle="tab">Table View</a></li>
                        <li><a href="#settings" data-toggle="tab">Tree View</a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="active tab-pane" id="activity">
                            <!-- Excel格式化后数据 NO.1 -->
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a class="text-bold text-danger">{{successMsg}}</a>
                                </div>
                                <table class="table table-striped m-b-none">
                                    <tbody>
                                    <tr ng-repeat="data in mongoDate">
                                        <td>{{$index+1}}</td>
                                        <td>{{data}}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!-- /.tab-pane -->
                        <div class="tab-pane" id="timeline">
                            <!-- Excel格式化后数据 NO.2 -->
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a class="text-bold text-danger">{{successMsg}}</a>
                                </div>
                                <table class="table table-striped m-b-none">
                                    <thead>
                                    <tr>
                                        <td></td>
                                        <td>name</td>
                                        <td>id_card</td>
                                        <td>mobiles</td>
                                        <td>debt</td>
                                        <td>categories</td>
                                        <td>sources</td>
                                        <td>others</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="data in mongoDate">
                                        <td>{{$index+1}}</td>
                                        <td>{{data.name}}</td>
                                        <td>{{data.id_card}}</td>
                                        <td>{{data.mobiles}}</td>
                                        <td>{{data.debt}}</td>
                                        <td>{{data.categories}}</td>
                                        <td>{{data.sources}}</td>
                                        <td>{{data.others}}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!-- /.tab-pane -->

                        <div class="tab-pane" id="settings">
                            <div class="modal-body wrapper-lg">
                                <div class="row" ng-init="loadJsonEditor()">
                                    <div id="jsoneditor" class="col-md-12"></div>
                                </div>
                            </div>
                        </div>
                        <!-- /.tab-pane -->
                    </div>
                    <!-- /.tab-content -->
                </div>
                <!-- /.nav-tabs-custom -->
            </div>


        </section>
        <!-- /.content -->
        <div class="clearfix"></div>
    </div>
    <!-- /.content-wrapper -->
    <footer class="main-footer" style="margin-left: 0px !important">
        <div class="pull-right hidden-xs">
            <b>Version</b> 1.0.0
        </div>
        <strong>Copyright &copy; 2015-2016 <a href="#">Juxinli Company</a>.</strong> All rights
        reserved.
    </footer>

    <!-- Control Sidebar -->
    <aside class="control-sidebar control-sidebar-dark">
        <!-- Create the tabs -->
        <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
            <li><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
            <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
        </ul>
        <!-- Tab panes -->
        <div class="tab-content">
            <!-- Home tab content -->
            <div class="tab-pane" id="control-sidebar-home-tab">
                <h3 class="control-sidebar-heading">Recent Activity</h3>
                <ul class="control-sidebar-menu">
                    <li>
                        <a href="javascript:">
                            <i class="menu-icon fa fa-birthday-cake bg-red"></i>

                            <div class="menu-info">
                                <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>

                                <p>Will be 23 on April 24th</p>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:">
                            <i class="menu-icon fa fa-user bg-yellow"></i>

                            <div class="menu-info">
                                <h4 class="control-sidebar-subheading">Frodo Updated His Profile</h4>

                                <p>New phone +1(800)555-1234</p>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:">
                            <i class="menu-icon fa fa-envelope-o bg-light-blue"></i>

                            <div class="menu-info">
                                <h4 class="control-sidebar-subheading">Nora Joined Mailing List</h4>

                                <p>nora@example.com</p>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:">
                            <i class="menu-icon fa fa-file-code-o bg-green"></i>

                            <div class="menu-info">
                                <h4 class="control-sidebar-subheading">Cron Job 254 Executed</h4>

                                <p>Execution time 5 seconds</p>
                            </div>
                        </a>
                    </li>
                </ul>
                <!-- /.control-sidebar-menu -->

                <h3 class="control-sidebar-heading">Tasks Progress</h3>
                <ul class="control-sidebar-menu">
                    <li>
                        <a href="javascript:">
                            <h4 class="control-sidebar-subheading">
                                Custom Template Design
                                <span class="label label-danger pull-right">70%</span>
                            </h4>

                            <div class="progress progress-xxs">
                                <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:">
                            <h4 class="control-sidebar-subheading">
                                Update Resume
                                <span class="label label-success pull-right">95%</span>
                            </h4>

                            <div class="progress progress-xxs">
                                <div class="progress-bar progress-bar-success" style="width: 95%"></div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:">
                            <h4 class="control-sidebar-subheading">
                                Laravel Integration
                                <span class="label label-warning pull-right">50%</span>
                            </h4>

                            <div class="progress progress-xxs">
                                <div class="progress-bar progress-bar-warning" style="width: 50%"></div>
                            </div>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:">
                            <h4 class="control-sidebar-subheading">
                                Back End Framework
                                <span class="label label-primary pull-right">68%</span>
                            </h4>

                            <div class="progress progress-xxs">
                                <div class="progress-bar progress-bar-primary" style="width: 68%"></div>
                            </div>
                        </a>
                    </li>
                </ul>
                <!-- /.control-sidebar-menu -->

            </div>
            <!-- /.tab-pane -->
            <!-- Stats tab content -->
            <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div>
            <!-- /.tab-pane -->
            <!-- Settings tab content -->
            <div class="tab-pane" id="control-sidebar-settings-tab">
                <form method="post">
                    <h3 class="control-sidebar-heading">General Settings</h3>

                    <div class="form-group">
                        <label class="control-sidebar-subheading">
                            Report panel usage
                            <input type="checkbox" class="pull-right" checked>
                        </label>

                        <p>
                            Some information about this general settings option
                        </p>
                    </div>
                    <!-- /.form-group -->

                    <div class="form-group">
                        <label class="control-sidebar-subheading">
                            Allow mail redirect
                            <input type="checkbox" class="pull-right" checked>
                        </label>

                        <p>
                            Other sets of options are available
                        </p>
                    </div>
                    <!-- /.form-group -->

                    <div class="form-group">
                        <label class="control-sidebar-subheading">
                            Expose author name in posts
                            <input type="checkbox" class="pull-right" checked>
                        </label>

                        <p>
                            Allow the user to show his name in blog posts
                        </p>
                    </div>
                    <!-- /.form-group -->

                    <h3 class="control-sidebar-heading">Chat Settings</h3>

                    <div class="form-group">
                        <label class="control-sidebar-subheading">
                            Show me as online
                            <input type="checkbox" class="pull-right" checked>
                        </label>
                    </div>
                    <!-- /.form-group -->

                    <div class="form-group">
                        <label class="control-sidebar-subheading">
                            Turn off notifications
                            <input type="checkbox" class="pull-right">
                        </label>
                    </div>
                    <!-- /.form-group -->

                    <div class="form-group">
                        <label class="control-sidebar-subheading">
                            Delete chat history
                            <a href="javascript:" class="text-red pull-right"><i class="fa fa-trash-o"></i></a>
                        </label>
                    </div>
                    <!-- /.form-group -->
                </form>
            </div>
            <!-- /.tab-pane -->
        </div>
    </aside>
    <!-- /.control-sidebar -->
    <!-- Add the sidebar's background. This div must be placed
         immediately after the control sidebar -->
    <div class="control-sidebar-bg"></div>
</div>
<!-- DataTables -->
<script src="${pageContext.request.contextPath}/js/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/js/plugins/datatables/dataTables.bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="${pageContext.request.contextPath}/js/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="${pageContext.request.contextPath}/js/plugins/fastclick/fastclick.min.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/js/dist/js/app.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="${pageContext.request.contextPath}/js/dist/js/demo.js"></script>
<%--ui--%>
<script src="${pageContext.request.contextPath}/js/ui/ui-load.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/ui-jp.config.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/ui-jp.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/ui-nav.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/ui-toggle.js"></script>
<!--json外部控件-->
<link href="${pageContext.request.contextPath}/js/jsoneditor/dist/jsoneditor.min.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/js/jsoneditor/dist/jsoneditor.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jsoneditor/dist/highcharts.js"></script>
</body>
</html>
