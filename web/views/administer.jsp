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
    <title>管理员登录</title>
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

    function size() {
        var width = $(window).width();
        var height = $(window).height();

        if (width > height) {
            // $("body").css({zoom:'0.5'});
        }
        console.log("width:" + width + " ,height:" + height);

        /**
         *浏览器环境对象 navigator
         */
        var userAgentInfo = navigator.userAgent;
        console.log(userAgentInfo);

        var mobileAgents = ["Android", "iPhone", "SymbianOS", "Windows Phone"];

        var moblie = false;
        //根据userAgent判断是否是手机
        for (var i = 0; i < mobileAgents.length; i++) {
            if (userAgentInfo.indexOf(mobileAgents[i]) > 0) {
                moblie = true;
                break;
            }
        }
        if (moblie) {
            $("body").css({'zoom': '1'});
            console.log("你的是手机浏览器")
        } else {
            $("body").css({'zoom': '0.5'});
            console.log("你的电脑浏览器")
        }

    }

    size();

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
