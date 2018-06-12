<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
%>
<html>

<head>
    <title>Http配置</title>

    <script type="application/javascript">
        $(function(){
            $("#nav-config").addClass("active");
        });
    </script>
</head>

<body>

<div class="row">
    <div class="col-sm-3 col-md-2">
        <div class="list-group">
            <a href="/config/zookeeper" class="list-group-item">Zookeeper管理</a>
            <a href="/config/dubbo" class="list-group-item">Dubbo服务管理</a>
            <a href="/config/http" class="list-group-item active">Http服务管理</a>
        </div>
    </div>

    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2">

    </div>
</div>



</body>

</html>