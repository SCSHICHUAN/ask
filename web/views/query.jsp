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
    <title>查询</title>
    <link rel="stylesheet" href="/ask/css/query.css">
    <script type="text/javascript" src="/ask/js/jquery-1.4.2.js"></script>
</head>
<body>
<input type="text"  name="phoneQuery"  class="phoneQuery" placeholder="电话">
<input type="text"  name="passwordQuery" class="passwordQuery" placeholder="密码">
<button class="button">查询</button>
<div class="uerName"></div>
<div class="uerTell"></div>
<div class="uerYnzm"></div>
<div class="uerCom"></div>
<div class="uerPost"></div>



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
                console.log('result='+result.name);
                $(".uerName").html("姓名:&nbsp;&nbsp;"+result.name);
                $(".uerTell").html("电话:&nbsp;&nbsp;"+result.tell);
                $(".uerYnzm").html("密码:&nbsp;&nbsp;"+result.yzm);
                $(".uerCom").html("公司:&nbsp;&nbsp;"+result.conpany);
                $(".uerPost").html("职务:&nbsp;&nbsp;"+result.post);

            }
        })




    })






</script>

</body>
</html>
