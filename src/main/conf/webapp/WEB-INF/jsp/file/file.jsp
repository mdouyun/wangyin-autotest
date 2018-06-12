<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
%>
<html>

<head>
    <title>Case管理</title>

    <script type="application/javascript">
        $(function(){
            $("#nav-case").addClass("active");


            $("#all_check").change(function(){
                $("#caseList :checkbox").prop("checked", this.checked);
            });
        });

        function deleteCase(path) {
            $.confirm("确认删除该Case？", function() {
                submitRequest("/file/case/delete", {
                    path : path
                }, function(data) {
                    $.alert.Success(data, function() {
                        $.utils.refreshPage();
                    });
                });
            });
        }

        function deleteDir()  {
            $.confirm("目录必须为空才能删除，确认删除该目录？", function() {
                submitRequest("/file/dir/delete", {
                    path : $("#path").val()
                }, function(data) {
                    $.alert.Success(data, function() {
                        $.utils.toUrl("/file/index?path=${lastPath}");
                    });
                });
            });
        }

        function createDir(obj) {
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

        function toEdit(path) {
            path = encodeURIComponent(path);
            $.utils.openPage("/case/edit?path=" + path);
        }

        function runCase() {

            var checkedArray = new Array();

            $("#caseList :checkbox").each(function() {

                if($(this).prop("checked")) {
                    var obj = new Object();
                    obj.path = $(this).val();
                    obj.id = $(this).attr("id");

                    checkedArray.push(obj);
                }

            });

            if(checkedArray.length == 0) {
                return ;
            }

            $.each(checkedArray, function(i, obj) {

                var trCls = $("#" + obj.id).closest("tr");

                trCls.css("color", "");
                trCls.tooltip("destroy");

                $.ajax({
                    url: "/case/run",
                    type : "POST",
                    data: {
                        path : obj.path
                    },
                    dataType: 'json',
                    success: function(data) {
                        if(data.success || data.success == "true") {

                            if(data.data || data.success == "true") {
                                trCls.css("color", "blue");
                            } else {
                                trCls.css("color", "red");
                            }

                        } else {
                            trCls.css("color", "red");
                            trCls.tooltip({
                                title : data.error,
                                placement : "top"
                            });

                        }
                    },
                    error: function() {
                        trCls.css("color", "red");
                        trCls.tooltip({
                            title : "提交请求出错，请稍后再试！",
                            placement : "top"
                        });
                    }
                });


            });


        }

    </script>
</head>

<body>

<div class="row">
    <div class="col-md-3">
        <div class="list-group">
            <a href="/file/index?path=${path}" class="list-group-item list-group-item-info">
                当前目录：${path}
            </a>
            <a href="/file/index?path=${lastPath}" class="list-group-item">
                <span class="glyphicon glyphicon-share-alt mr10"></span>返回上级目录
            </a>
        <c:forEach var="case" items="${dirs}">
            <a href="/file/index?path=${case.path}" class="list-group-item">
                <span class="glyphicon glyphicon-folder-open mr10"></span>${case.name}
            </a>
        </c:forEach>
        </div>
    </div>

    <div class="col-md-9">

        <div class="panel panel-info">
            <div class="panel-heading">新建Case目录</div>
            <div class="panel-body">
                <form class="form-inline" id="addForm" action="/file/dir/create" method="post">
                    <div class="form-group">
                        <label for="dirName">目录名称</label>
                        <input type="hidden" id="path" name="path" value="${path}">
                        <input type="text" class="form-control" style="width: 450px;" id="dirName" name="dirName" placeholder="目录之间使用/分隔符">
                    </div>
                    <button type="button" onclick="createDir(this)" class="btn btn-primary">创建</button>
                    <button type="button" onclick="deleteDir()" class="btn btn-danger">
                        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> 删除当前目录
                    </button>
                </form>
            </div>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Case列表
                <a class="btn btn-default btn-xs ml10" href="#" onclick="runCase();" role="button">批量执行选中Case</a>
                <span class="badge ml30"><c:out value="${fn:length(cases)}"></c:out></span>
            </div>
            <table class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th width="5%">
                            <input type="checkbox" id="all_check">
                        </th>
                        <th width="65%">名称</th>
                        <th width="20%">修改时间</th>
                        <th width="10%" style="text-align: center;">操作</th>
                    </tr>
                </thead>
                <tbody id="caseList">
                <c:forEach var="case" items="${cases}" varStatus="cf">
                    <tr>
                        <td>
                            <input type="checkbox" name="ck-${cf.index}" id="ck-${cf.index}" value="${case.path}">
                        </td>
                        <td>${case.name}</td>
                        <td>${case.modifiedTime}</td>
                        <td style="text-align: center;">
                            <a href="#" onclick="toEdit('${case.path}')"><span class="glyphicon glyphicon-edit mr15" aria-hidden="true"></span></a>
                            <a href="#" onclick="deleteCase('${case.path}')"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
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