<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
%>
<html>

<head>
    <title>首页</title>

    <script type="application/javascript">
        $(function(){
            $("#nav-index").addClass("active");
        });
    </script>

</head>

<body>
    <div class="page-header">
        <h1>Sticky footer with fixed navbar</h1>
    </div>
    <p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added with <code>padding-top: 60px;</code> on the <code>body > .container</code>.</p>
    <p>Back to <a href="../sticky-footer">the default sticky footer</a> minus the navbar.</p>
</body>

</html>