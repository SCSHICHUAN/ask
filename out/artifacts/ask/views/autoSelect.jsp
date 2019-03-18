<%--
  Created by IntelliJ IDEA.
  User: SHICHUAN
  Date: 2018/12/18
  Time: 上午9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>自动生成</title>
    <link rel="stylesheet" href="/ask/css/autoSelect.css">
    <script type="text/javascript" src="/js/jquery-1.4.2.js"></script>
</head>
<body>
    <h2>自动生成试卷设置</h2>
<form action="/ask/autoCategory">
    <div class="categorys">

        <c:forEach var="mapItem" items="${keys}">
            <div class="category">${mapItem.key}<span class="span">共${mapItem.value}题</span>

                <select name="${mapItem.key}">
                    <% Integer i = 0; %>
                    <c:forEach begin="0" end="${mapItem.value}" step="1">
                        <option value="<%=i%>"> <%=i%> </option>
                        <% i++; %>
                    </c:forEach>
                </select>

            </div>
        </c:forEach>
    </div>
    <button>设置</button>
</form>

<script>


</script>
</body>
</html>
