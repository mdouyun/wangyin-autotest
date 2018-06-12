<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
%>
<html>

<head>
    <title>编辑Dubbo Case</title>
    <link rel="stylesheet" href="<%=path%>/plugins/jsoneditor/jsoneditor.css">
    <script src="<%=path%>/plugins/jsoneditor/jsoneditor.js" type="text/javascript"></script>

    <script type="application/javascript">
        var jsonEditor;

        $(function(){
            $("#nav-edit").addClass("active");

            var error = '<c:out value="${error}" escapeXml="false"/>';
            if(error != "") {
                $.alert.Error(error, function() {
                    $.utils.toUrl("/create/dubbo")
                });
                return ;
            }

            var options = {
                mode: 'tree',
                //modes: ['code', 'form', 'text', 'tree', 'view'], // allowed modes
                modes: ['code', 'text', 'tree', 'view'], // allowed modes
                error: function (err) {
                    alert(err.toString());
                }
            };

            jsonEditor = new JSONEditor(document.getElementById('jsoneditor'), options);


            $("#checkFlag").blur(function() {
                var check = $("#checkFlag").val();
                var data = $("#responseResult").val();
                $("#responsePre").removeClass("alert-success");
                $("#responsePre").removeClass("alert-danger");
                if(data != "" && check != "" && !$.utils.contain(data, check)) {
                    $("#responsePre").addClass("alert-danger");
                } else if(data != "" && check != "" && $.utils.contain(data, check)) {
                    $("#responsePre").addClass("alert-success");
                }
            });

            jsonEditor.set(eval($("#requestJson").val()));
            jsonEditor.expandAll();

            var showJson = JSON.stringify(JSON.parse($("#responseResult").val()), null, "\t");
            $("#responsePre").html(showJson);

            $("#checkFlag").trigger("blur");
        });

        function sendRequest(obj) {

            $(obj).attr("disabled", true);

            setTimeout(function() {
                $(obj).attr("disabled", false);
            }, 1000);

            var requestJson = JSON.stringify(jsonEditor.get());
            $("#requestJson").val(requestJson);
            if(requestJson == null || requestJson == "" || requestJson == undefined) {
                return ;
            }

            var dubboUrl = $("#dubboUrl").val();

            if(!dubboUrl) {
                return ;
            }

            submitForm("requestForm", function(data) {
                $("#responseResult").val(data);
                var showJson = JSON.stringify(JSON.parse(data), null, "\t");
                $("#responsePre").html(showJson);

                $("#checkFlag").trigger("blur");
            })
        }

        function saveCase(obj) {
            $(obj).attr("disabled", true);

            setTimeout(function() {
                $(obj).attr("disabled", false);
            }, 1000);


            var requestJson = JSON.stringify(jsonEditor.get());

            submitRequest("/dubbo/save",

                    {dubboUrl : $("#dubboUrl").val(),
                        methodName : $("#methodName").val(),
                        requestJson : requestJson,
                        responseJson : $("#responseResult").val(),
                        checkFlag : $("#checkFlag").val(),
                        savePath : $("#savePath").val()
                    },

                    function(data) {
                        $.alert.Success(data);
                    });
        }

        function getRequestJson(obj) {

            $(obj).attr("disabled", true);

            setTimeout(function() {
                $(obj).attr("disabled", false);
            }, 1000);

            submitRequest("/dubbo/getRequestData",

                    {dubboUrl : $("#dubboUrl").val(),
                        methodName : $("#methodName").val()},

                    function(data) {
                        jsonEditor.set(eval(data));
                        jsonEditor.expandAll();
                    });

        }

    </script>
</head>

<body>

<div class="row">
    <div class="col-md-6">
        <div class="panel panel-primary">
            <div class="panel-heading">接口请求域</div>
            <div class="panel-body">
                <form class="form-horizontal" id="requestForm" action="/dubbo/sendRequest" method="post">
                    <div class="form-group">
                        <label for="serverUrl" class="col-sm-3 control-label">Dubbo服务</label>
                        <div class="col-sm-9">
                            <input type="hidden" id="dubboUrl" name="dubboUrl" value="${provider.dubboUrl}">
                            <input type="text" readonly class="form-control" id="serverUrl" name="serverUrl" value="${provider.serverUrl}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="methodName" class="col-sm-3 control-label">接口方法</label>
                        <div class="col-sm-9">
                            <input type="text" readonly class="form-control" id="methodName" name="methodName" value="${case.method}">
                        </div>
                    </div>

                    <div class="form-group">
                        <input type="hidden" id="requestJson" name="requestJson" value='${case.request}'>
                        <label class="col-sm-3 control-label">JSON参数</label>
                        <div class="col-sm-9">
                            <button type="button" onclick="getRequestJson(this);" class="btn btn-sm btn-info">重新生成JSON参数</button>
                        </div>
                    </div>

                    <div class="form-group">
                        <div id="jsoneditor" class="col-sm-12" style="height: 500px;"></div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-8">
                            <button type="button" onclick="sendRequest(this);" class="btn btn-primary btn-block">发送请求</button>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>

    <div class="col-md-6">
        <div class="panel panel-warning">
            <div class="panel-heading">接口响应域</div>
            <div class="panel-body">
                <form class="form-horizontal" id="responseForm" action="" method="post">
                    <div class="form-group">
                        <label for="checkFlag" class="col-sm-3 control-label">成功标示</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="checkFlag" name="checkFlag" value='${case.checkFlag}'>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="savePath" class="col-sm-3 control-label">保存地址</label>
                        <div class="col-sm-9">
                            <div class="input-group">
                                <input type="text" class="form-control" id="savePath" name="savePath" value="${case.path}">
                                <div class="input-group-addon">.xml</div>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">响应报文</label>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-12">
                            <input type="hidden" id="responseResult" value='${case.response}'>
                            <pre id="responsePre" style="height: 500px;margin-bottom: 0px;" class="alert"></pre>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-8">
                            <button type="button" onclick="saveCase(this);" class="btn btn-success btn-block">保存</button>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>



</body>

</html>