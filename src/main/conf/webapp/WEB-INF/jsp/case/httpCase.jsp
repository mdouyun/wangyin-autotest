<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
%>
<html>

<head>
    <title>新建Http Case</title>
    <link rel="stylesheet" href="<%=path%>/plugins/jsoneditor/jsoneditor.css">
    <script src="<%=path%>/plugins/jsoneditor/jsoneditor.js" type="text/javascript"></script>
    <script type="application/javascript">

        var index_header = 2;
        var index_form = 2;

        $(function(){
            $("#nav-edit").addClass("active");

            $("#ctArea").hide();
            $("#requestArea").hide();
            $("#formArea").hide();

            $("#method").change(function() {

                var mt = $("#method").val();

                if(mt == "get") {
                    $("#ctArea").hide();
                    $("#requestArea").hide();
                } else if(mt == "post") {
                    $("#ctArea").show();
                    $("#requestArea").show();
                }

            });


            $("#mimeType").change(function() {

                var mt = $("#mimeType").val();

                if(mt == "multipart/form-data") {
                    $("#formArea").show();
                    $("#requestArea").hide();
                } else {
                    $("#formArea").hide();
                    $("#requestArea").show();

                }

            });


        });

        function addHeader() {

            var html = '<tr id="henader-tr-'+ index_header + '"><td><input type="text" class="form-control" id="h-k-'+ index_header +'" name="h-k-1"></td>' +
                            '<td><div class="input-group">' +
                            '<input type="text" class="form-control" id="h-v-'+ index_header +'" name="h-v-'+ index_header +'">' +
                                '<div class="input-group-addon" onclick="delHeader('+ index_header +');"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span></div>' +
                        '</div></td></tr>';
            index_header++;

            $("#headerTb").append(html);
        }

        function delHeader(i) {
            $("#henader-tr-" + i).remove();
        }

        function addFormRow() {
            var html = '<tr id="form-tr-'+ index_form + '"><td><input type="text" class="form-control" id="f-k-'+ index_form +'" name="f-k-1"></td>' +
                    '<td><div class="input-group">' +
                    '<input type="text" class="form-control" id="f-v-'+ index_form +'" name="f-v-'+ index_form +'">' +
                    '<div class="input-group-addon" onclick="delFormRow('+ index_form +');"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span></div>' +
                    '</div></td></tr>';
            index_form++;

            $("#formTb").append(html);
        }

        function delFormRow(i) {
            $("#form-tr-" + i).remove();
        }

        function sendRequest(obj) {

            $(obj).attr("disabled", true);

            setTimeout(function() {
                $(obj).attr("disabled", false);
            }, 1000);

            var url = $("#url").val();

            if(!url) {
                return ;
            }

            var header = new Object();

            $("#headerTb input[id^=h-k-]").each(function(i, n){

                var id = $(this).attr("id");
                var key = $(this).val();

                var vid = id.replace(/k/, "v");
                var value = $("#" + vid).val();

                header[key] = value;
            });

            var form = new Object();
            $("#formTb input[id^=f-k-]").each(function(i, n){

                var id = $(this).attr("id");
                var key = $(this).val();

                var vid = id.replace(/k/, "v");
                var value = $("#" + vid).val();

                form[key] = value;
            });


            submitRequest("/http/sendRequest",

                    {url : url,
                        method : $("#method").val(),
                        mimeType : $("#mimeType").val(),
                        request : $("#request").val(),
                        charset : $("#charset").val(),
                        header : JSON.stringify(header),
                        form : JSON.stringify(form)
                    },

                    function(data) {
                        $("#responseResult").val(data);
                        var showJson = JSON.stringify(JSON.parse(data), null, "\t");
                        $("#responsePre").html(showJson);

                        $("#checkFlag").trigger("blur");
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
                <form class="form-horizontal" id="requestForm" action="/http/sendRequest" method="post">
                    <div class="form-group">
                        <label for="url" class="col-sm-2 control-label">Url</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="url" name="url">
                        </div>
                        <div class="col-sm-3">
                            <select class="form-control" id="charset" name="charset">
                                <option>-设置编码格式-</option>
                                <option value="UTF-8">UTF-8</option>
                                <option value="GBK">GBK</option>
                                <option value="GB2312">GB2312</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="method" class="col-sm-2 control-label">Method</label>
                        <div class="col-sm-3">
                            <select class="form-control" id="method" name="method">
                                <option value="get">get</option>
                                <option value="post">post</option>
                            </select>
                        </div>
                        <div class="col-sm-7" id="ctArea">
                            <select class="form-control" id="mimeType" name="mimeType">
                                <option>---------设置Content-Type---------</option>
                                <option value="text/xml">text/xml</option>
                                <option value="text/html">text/html</option>
                                <option value="text/plain">text/plain</option>
                                <option value="multipart/form-data">multipart/form-data</option>
                                <option value="application/xml">application/xml</option>
                                <option value="application/xhtml+xml">application/xhtml+xml</option>
                                <option value="application/json">application/json</option>
                                <option value="application/x-www-form-urlencoded">application/x-www-form-urlencoded</option>
                                <option value="application/atom+xml">application/atom+xml</option>
                            </select>
                        </div>
                    </div>


                    <div class="form-group">
                        <div class="col-sm-12">
                            <table id="headerTb" class="table table-condensed table-bordered" style="margin-bottom: 0px;">
                                <thead>
                                    <tr>
                                        <th width="40%">Header key</th>
                                        <th width="60%">Header Value</th>
                                    </tr>
                                </thead>
                                <tr>
                                    <td><input type="text" class="form-control" id="h-k-1" name="h-k-1"></td>
                                    <td>
                                        <div class="input-group">
                                            <input type="text" class="form-control" id="h-v-1" name="h-v-1">
                                            <div onclick="addHeader();" class="input-group-addon"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></div>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div class="form-group" id="formArea">
                        <div class="col-sm-12">
                            <table id="formTb" class="table table-condensed table-bordered" style="margin-bottom: 0px;">
                                <thead>
                                <tr>
                                    <th width="40%">表单 key</th>
                                    <th width="60%">表单 Value</th>
                                </tr>
                                </thead>
                                <tr>
                                    <td><input type="text" class="form-control" id="f-k-1" name="f-k-1"></td>
                                    <td>
                                        <div class="input-group">
                                            <input type="text" class="form-control" id="f-v-1" name="f-v-1">
                                            <div onclick="addFormRow();" class="input-group-addon"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></div>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div class="form-group" id="requestArea">
                        <div class="col-sm-12">
                            <textarea class="form-control" rows="23" id="request" name="request" placeholder="报文内容"></textarea>
                        </div>
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
                            <input type="text" class="form-control" id="checkFlag" name="checkFlag" placeholder="根据响应包含该字符串判断是否成功">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="savePath" class="col-sm-3 control-label">保存地址</label>
                        <div class="col-sm-9">
                            <div class="input-group">
                                <input type="text" class="form-control" id="savePath" name="savePath">
                                <div class="input-group-addon">.xml</div>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">响应报文</label>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-12">
                            <input type="hidden" id="responseResult">
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