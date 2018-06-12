<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
%>
<html>

<head>
    <title>Zookeeper配置</title>
    <script type="application/javascript">
        $(function(){
            $("#nav-config").addClass("active");
        });

        function deleteZookeeper(id) {
            $.confirm("确认删除该zookeeper配置？", function() {
                submitRequest("/config/zookeeper/delete", {
                    id : id
                }, function(data) {
                    $.alert.Success(data, function() {
                        $.utils.refreshPage();
                    });
                });
            });
        }

        function saveZookeeper(obj) {
            $(obj).attr("disabled", true);

            submitForm("addForm", function(data) {
                $.alert.Success(data, function() {
                    $.utils.refreshPage();
                });
            });

            setTimeout(function() {
                $(obj).attr("disabled", false);
            }, 1000);
        }

    </script>
</head>

<body>

<div class="row">
    <div class="col-md-2">
        <div class="list-group">
            <a href="/config/zookeeper" class="list-group-item active">Zookeeper管理</a>
            <a href="/config/dubbo" class="list-group-item">Dubbo服务管理</a>
        </div>
    </div>

    <div class="col-md-10">
        <div class="panel panel-info">
            <div class="panel-heading">添加Zookeeper配置</div>
            <div class="panel-body">
                <form class="form-inline" id="addForm" action="/config/zookeeper/save" method="post">
                    <div class="form-group">
                        <label class="sr-only" for="id">Zookeeper别名</label>
                        <input type="text" class="form-control" id="id" name="id" placeholder="Zookeeper别名">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="address">Zookeeper地址</label>
                        <input type="text" class="form-control" style="width: 350px;" id="address" name="address" placeholder="Zookeeper地址">
                    </div>
                    <button type="button" onclick="saveZookeeper(this)" class="btn btn-primary">保存</button>
                </form>
            </div>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Zookeeper配置列表&nbsp;&nbsp;&nbsp;&nbsp;<span class="badge"><c:out value="${fn:length(list)}"></c:out></span></div>
            <table class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th width="20%">Zookeeper别名</th>
                        <th width="70%">Zookeeper地址</th>
                        <th width="15%" style="text-align: center;">操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="zk" items="${list}">
                    <tr>
                        <td>${zk.id}</td>
                        <td>${zk.address}</td>
                        <td style="text-align: center;">
                            <a href="#" onclick="deleteZookeeper('${zk.id}')"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </div>
</div>



</body>

</html>