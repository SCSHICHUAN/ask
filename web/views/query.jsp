<%--
  Created by IntelliJ IDEA.
  User: SHICHUAN
  Date: 2018/11/9
  Time: 下午5:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户查询</title>
    <link rel="stylesheet" href="/ask/css/query.css">
    <script type="text/javascript" src="/ask/js/jquery-1.4.2.js"></script>
</head>
<body>
<% String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    System.out.println("basePath = " + basePath);%>
<%String webName = request.getContextPath();%>
<div class="queryPage">
    <input type="text" name="phoneQuery" class="phoneQuery" placeholder="电话" type="number" pattern="\d*">
    <input type="text" name="passwordQuery" class="passwordQuery" placeholder="密码" type="number" pattern="\d*">
    <button class="button">查询</button>
</div>
<div class="resultQuery">
    <img src="/ask/img/male203.png" class="img">
    <div class="uerName"></div>
    <div class="information">
        <div class="uerCom"></div>
        <div class="uerPost"></div>
        <div class="uerTell"></div>
        <div class="uerYnzm"></div>
    </div>
</div>


<script type="text/javascript">

    $(".button").click(function () {

        var phoneQuery = $("[name='phoneQuery']").val();
        var passwordQuery = $("[name='passwordQuery']").val();

        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            url: "/ask/query",
            type: "post",
            data: {
                phoneQuery1: phoneQuery,
                passwordQuery1: passwordQuery,
            },
            dataType: "json",
            success: function (result) {
                $(".resultQuery").css({ 'display':'block'}).animate({top: '0'}, 350);
                $(".queryPage").animate({top: '10%', left: '10%', width: '80%', height: '80%'}, 350);
                console.log('result=' + result.name);
                $(".uerName").html("姓名:&nbsp;&nbsp;" + result.name);
                $(".uerCom").html("公司:&nbsp;&nbsp;" + result.conpany);
                $(".uerPost").html("职务:&nbsp;&nbsp;" + result.post);
                $(".uerTell").html("电话:&nbsp;&nbsp;" + result.tell);
                $(".uerYnzm").html("密码:&nbsp;&nbsp;" + result.yzm);
            }
        })

    })
</script>
</body>
</html>
