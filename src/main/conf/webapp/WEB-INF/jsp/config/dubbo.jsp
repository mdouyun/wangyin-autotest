<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
%>
<html>

<head>
    <title>Dubbo配置</title>

    <script type="application/javascript">
        $(function(){
            $("#nav-config").addClass("active");
            $('[data-toggle="popover"]').popover({
                container: "body",
                html : true
            })
        });

        function queryDubboServer() {
            submitRequest("/config/dubbo/query", {
                interfaceClass : $("#dubbo_name").val(),
                zkId :  $("#zk_id").val()
            }, function(data) {

                $("#dubbo_id").empty();

                $.each(data, function(i, obj) {

                    var show = obj.serverIp + "?version=" + obj.version + "&group=" + obj.group;
                    var option = "<option value='" + obj.dubboUrl + "'>" + show + "</option>";
                    $("#dubbo_id").append(option);
                });

            });
        }

        function saveDubbo() {
            submitRequest("/config/dubbo/save", {
                dubboUrl : $("#dubbo_id").val(),
                zkId : $("#zk_id").val()
            }, function(data) {
                $.alert.Success(data, function() {
                    $.utils.refreshPage();
                });
            });
        }

        function moreInfo(methods, version, group) {

            $("#info dd[node=m]").remove();

            var ma = methods.split(",");

            $.each(ma, function(i, m) {
                $("#info-methods").after("<dd node='m'>" + m + "</dd>");
            });

            $("#info-version").html(version);
            $("#info-group").html(group);

            $("#info").modal("show");
        }

        function deleteDubbo(dubboUrl) {
            $.confirm("确认删除该Dubbo服务？", function() {
                submitRequest("/config/dubbo/delete", {
                    dubboUrl : dubboUrl
                }, function(data) {
                    $.alert.Success(data, function() {
                        $.utils.refreshPage();
                    });
                });
            });
        }

    </script>
</head>

<body>

<div class="row">
    <div class="col-md-2">
        <div class="list-group">
            <a href="/config/zookeeper" class="list-group-item">Zookeeper管理</a>
            <a href="/config/dubbo" class="list-group-item active">Dubbo服务管理</a>
        </div>
    </div>

    <div class="col-md-10">
        <div class="panel panel-info">
            <div class="panel-heading">添加Dubbo服务</div>
            <div class="panel-body">
                <form class="form-horizontal" id="addForm" action="/config/dubbo/save" method="post">
                    <div class="form-group">
                        <label for="zk_id" class="col-sm-2 control-label">Zookeeper</label>
                        <div class="col-sm-10">
                            <select class="form-control" id="zk_id" name="zk_id">
                                <option value="">---------请选择Dubbo服务注册的Zookeeper---------</option>
                            <c:forEach var="zk" items="${zkList}">
                                <option value="${zk.id}">${zk.id}=${zk.address}</option>
                            </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="dubbo_name" class="col-sm-2 control-label">接口名称</label>
                        <div class="col-sm-10">
                            <div class="input-group">
                                <input type="text" class="form-control" id="dubbo_name" name="dubbo_name" placeholder="Dubbo接口类名全称" value="com.wangyin.npp.payment.facade.PaymentManageService">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" type="button" onclick="queryDubboServer();">
                                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span> 查询Dubbo服务
                                    </button>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="dubbo_id" class="col-sm-2 control-label">Dubbo服务</label>
                        <div class="col-sm-10">
                            <select class="form-control" id="dubbo_id" name="dubbo_id">
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" onclick="saveDubbo(this);" class="btn btn-primary">保存</button>
                        </div>
                    </div>

                </form>
            </div>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Dubbo服务列表&nbsp;&nbsp;&nbsp;&nbsp;<span class="badge"><c:out value="${fn:length(dubboList)}"></c:out></span></div>
            <table class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                <tr>
                    <th width="25%">服务提供方</th>
                    <th width="65%">接口名称</th>
                    <th width="10%" style="text-align: center;">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="dubbo" items="${dubboList}">
                    <tr>
                        <td>${dubbo.serverIp}</td>
                        <td>${dubbo.interfaceName}</td>
                        <td style="text-align: center;">
                            <a href="#" onclick="moreInfo('${dubbo.methods}','${dubbo.version}','${dubbo.group}')"><span class="glyphicon glyphicon-info-sign mr10" aria-hidden="true"></span></a>
                            <a href="/create/dubbo?serverUrl=${dubbo.serverUrl}" target="_blank"><span class="glyphicon glyphicon-check mr10" aria-hidden="true"></span></a>
                            <a href="#" onclick="deleteDubbo('${dubbo.dubboUrl}')"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="info" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <dl class="dl-horizontal">
                    <dt id="info-methods">Methods:</dt>
                    <dt>Version:</dt>
                    <dd id="info-version"></dd>
                    <dt>Group:</dt>
                    <dd id="info-group"></dd>
                </dl>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>



</body>

</html>