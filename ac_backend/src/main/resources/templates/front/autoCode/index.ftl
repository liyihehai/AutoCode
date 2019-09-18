<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Demo Frame</title>
    <#include "../taglib.ftl">
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- Main Header -->
    <header class="main-header">
        <!-- Logo -->
        <a href="javascript:void(0);" class="logo"
           style="background-image:url(${envData.staticRoot!''}/images/m-logo.png);background-size:144px 55px;
                   background-repeat:no-repeat;background-position:22px 0;">
        </a>
        <!-- Header Navbar -->
        <nav class="navbar navbar-static-top" role="navigation">
            <!-- Sidebar toggle button
            <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
              <span class="sr-only">Toggle navigation</span>
            </a>-->
            <!-- Navbar Right Menu -->
            <div class="navbar-custom-menu">
                <ul class="nav navbar-nav">
                    <!-- Tasks Menu -->
                    <li class="dropdown tasks-menu">
                        <ul class="dropdown-menu">
                            <li class="footer">
                                <a href="#">敬请期待</a>
                            </li>
                        </ul>
                    </li>
                    <!-- User Account Menu -->
                    <li class="dropdown user user-menu">
                        <!-- Menu Toggle Button -->
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <!-- The user image in the navbar-->
                            <#assign defHeadPic=envData.staticRoot+'/images/qjbian.png'/>
                            <img src="${headPic!defHeadPic}" class="user-image" alt="User Image">
                        </a>
                        <ul class="dropdown-menu">
                            <!-- The user image in the menu -->
                            <li class="user-header">
                                <img src="${headPic!defHeadPic}" class="img-circle" alt="User Image">
                                <p>
                                    <#if nickName?exists>${nickName!''}<#else>${userName!''}</#if> - ${roleName!''}
                                    <small>最后登录：${loginTime!''}</small>
                                </p>
                            </li>
                            <!-- Menu Footer-->
                            <li class="user-footer">
                                <div class="pull-left">
                                </div>
                                <div class="pull-right">
                                    <a href="/w/logout.action" class="btn btn-default btn-flat btn-loginout">退出</a>
                                </div>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </nav>
    </header>
    <!-- Left side column. contains the logo and sidebar -->
    <aside class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
            <!-- Sidebar Menu -->
            <ul class="sidebar-menu">
                <li class="treeview">
                    <a href="#">
                        <i class="fa fa-fw fa-home"></i>
                        <span>自动代码</span>
                        <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu">
                        <li>
                            <a href="javascript:void(0);" data-link="/autoCode/projectMgrIndex"
                               data-menukey="projectMgrIndex"><i class="fa fa-link"></i> <span>项目管理</span> </a>
                        </li>
                    </ul>
                </li>
            </ul><!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
    </aside>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
            <div class="nav-tabs-custom"> <!-- nav-tabs-custom -->
                <ul class="nav nav-tabs" id="menu_title">
                    <li class="active"><a href="#HOME0" data-toggle="tab" aria-expanded="true" id="HOME">首页</a></li>
                </ul>
                <div class="tab-content" id="menu_content">
                    <div class="tab-pane active" id="HOME0">
                        <iframe src="/autoCode/mainPage" frameborder=0 width="100%" height="850px" scrolling=no>
                        </iframe>
                    </div><!-- /.tab-pane -->
                </div><!-- /.tab-content -->
            </div><!-- /.nav-tabs-custom -->
        </section>
    </div><!-- /.content-wrapper -->
    <!-- Main Footer -->
    <footer class="main-footer">
        <!-- To the right -->
        <div class="pull-right hidden-xs">
            Demo
        </div>
        <!-- Default to the left -->
        <strong>Demo Frame Copyright &copy; 2016-2017 <a href="#">nnte Technology</a>.</strong>
    </footer>
    <!-- Control Sidebar -->
    <aside class="control-sidebar control-sidebar-dark">
        <!-- Create the tabs -->
        <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
            <li class="active"><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
            <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
        </ul>
        <!-- Tab panes -->
        <div class="tab-content">
            <!-- Home tab content -->
            <div class="tab-pane active" id="control-sidebar-home-tab">
                <h3 class="control-sidebar-heading">Recent Activity</h3>
                <ul class="control-sidebar-menu">
                    <li>
                        <a href="javascript:;">
                            <i class="menu-icon fa fa-birthday-cake bg-red"></i>
                            <div class="menu-info">
                                <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>
                                <p>Will be 23 on April 24th</p>
                            </div>
                        </a>
                    </li>
                </ul><!-- /.control-sidebar-menu -->
                <h3 class="control-sidebar-heading">Tasks Progress</h3>
                <ul class="control-sidebar-menu">
                    <li>
                        <a href="javascript::;">
                            <h4 class="control-sidebar-subheading">
                                Custom Template Design
                                <span class="label label-danger pull-right">70%</span>
                            </h4>
                            <div class="progress progress-xxs">
                                <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
                            </div>
                        </a>
                    </li>
                </ul><!-- /.control-sidebar-menu -->
            </div><!-- /.tab-pane -->
            <!-- Stats tab content -->
            <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div><!-- /.tab-pane -->
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
                    </div><!-- /.form-group -->
                </form>
            </div><!-- /.tab-pane -->
        </div>
    </aside><!-- /.control-sidebar -->
    <!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
    <div class="control-sidebar-bg"></div>
</div><!-- ./wrapper -->
</body>
</html>
<script type="text/javascript">
    $('.sidebar-menu li').click(function () {
        var link = $(this).children('a').attr('data-link');
        var menuKey = $(this).children('a').attr('data-menukey');
        var menuName = $(this).children('a').children('span').text();
        var nav = '<i class="fa fa-dashboard"></i>首页>>' + menuName;
        jumpPage(link, menuName, menuKey, nav);
    });

    function showIndexProduct(id) {
        var title = '<i class="fa fa-dashboard"></i>首页>>商品详情';
        var url = '/w/goodsDetail.action?productId=' + id;
        jumpPage(url, title);
    }

    function memberReception() {
        var nav = '<i class="fa fa-dashboard"></i>首页>>会员接待';
        jumpPage("/w/memberReceptionIndex.action", "会员接待", "MEMBER_FANS_RECEPTION", nav);
    }
</script>