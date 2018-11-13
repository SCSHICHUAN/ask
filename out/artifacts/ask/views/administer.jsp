<%--
  Created by IntelliJ IDEA.
  User: SHICHUAN
  Date: 2018/11/13
  Time: 上午11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/ask/css/administer.css">
    <script type="text/javascript" src="/ask/js/jquery-1.4.2.js"></script>
</head>
<body>

<div class="queryPage">
    <form action="/ask/adm" method="post">
        <input type="text" name="adminiNum" class="adminiNum" placeholder="管理号" type="number" pattern="\d*">
        <input type="text" name="adminiPassword" class="adminiPassword" placeholder="密码" type="number" pattern="\d*">
        <div class="tips">管理号或密码错误</div>
        <button class="button">进入管理系统</button>
    </form>
</div>

<script type="text/javascript">

    function s() {

        if ("${flag}" == "false") {
            $(".tips").css({display: 'block'});
        }


        console.log("+++++flag="+"${flag}");
    }

    s();


</script>
</body>
</html>
