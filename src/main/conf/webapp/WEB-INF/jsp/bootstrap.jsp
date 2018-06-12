<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    String thisUrl = request.getServletPath();
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title><decorator:title default="autotest"/></title>

    <!-- Bootstrap -->
    <%--<link rel="stylesheet" href="<%=path%>/plugins/bootstrap-v3.3.5/css/bootstrap.min.css">--%>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <link rel="stylesheet" href="<%=path%>/css/boot.css">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script src="<%=path%>/plugins/jquery.min.js" type="text/javascript"></script>
    <script src="<%=path%>/plugins/bootstrap-v3.3.5/js/bootstrap.min.js" type="text/javascript"></script>
    <%--<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>--%>
    <script src="<%=path%>/plugins/jquery-form/jquery.form.js" type="text/javascript"></script>

    <script src="<%=path%>/js/utils.js" type="text/javascript"></script>
    <script src="<%=path%>/js/alert.js" type="text/javascript"></script>


    <script src="<%=path%>/js/knife.js" type="text/javascript"></script>

    <decorator:head />

    <script type="application/javascript">
        $(function() {
            $("a[href=#]").attr("href", "javascript:void(0);");
        });
    </script>

</head>
<body>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Autotest</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li id="nav-index"><a href="/index">首页</a></li>
                <li id="nav-config"><a href="/config/zookeeper">配置管理</a></li>
                <li id="nav-edit" class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"
                       role="button" aria-haspopup="true" aria-expanded="false">Case编辑 <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="/create/dubbo">新建Dubbo Case</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="/create/http">新建Http Case</a></li>
                    </ul>
                </li>
                <li id="nav-case"><a href="/file/index">Case管理</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <decorator:body />
</div>


<footer class="footer">
    <div class="container">
        <p class="text-muted" style="padding-top: 5px;">Autotest - wangyin - p 本网站使用<a href="http://v3.bootcss.com/">Bootstrap</a>构建</p>
        <p class="text-muted"> Powered By C.y_Chris</p>
    </div>
</footer>

</body>
</html>