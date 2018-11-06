<%--
  Created by IntelliJ IDEA.
  User: SHICHUAN
  Date: 2018/11/5
  Time: 上午10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/main.css">
    <script type="text/javascript" src="js/jquery-1.4.2.js"></script>
</head>
<body>

<img src="img/timg.jpeg" class="img1">
<img src="img/le.png" class="img2">
<img src="img/logo.png" class="img3">

<div class="t1">AZ市场部测评项目</div>
<div class="t2">市场部基础知识测评试卷</div>

<input type="text" name="name" class="name" placeholder="姓名">
<input type="text" name="tell" class="tell" placeholder="电话" type="number" pattern="\d*">
<div class="yzm-tell">
    <input type="text" name="yzm" class="yzm" placeholder="验证码" type="number" pattern="\d*">
    <button class="buttonYzm">获取验证码</button>
    </input>
</div>
<input type="text" name="company" class="company" placeholder="公司">
<input type="text" name="post" class="post" placeholder="职位">
<button class="button">确定</button>

<script>
    $(".buttonYzm").click(function () {
        var tell = $(".tell").val();
        console.log(tell);

        $.ajax({
            url:,
            type: post,
            data: {
                tell: tell
            },
            dataType: "json",
            success: function (json) {
                var flag = json.flag;
            }
        })


    })


</script>
</body>
</html>
