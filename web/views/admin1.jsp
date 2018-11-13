<%--
  Created by IntelliJ IDEA.
  User: SHICHUAN
  Date: 2018/11/13
  Time: 下午12:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理系统</title>
    <link rel="stylesheet" href="/ask//css/admin1.css">
    <script type="text/javascript" src="/ask/js/jquery-1.4.2.js"></script>
</head>
<body>
<img src="/ask/img/logo.png" class="img3">
<div class="headerT">壹萌质询管理系统</div>
<div class="header"></div>
<div class="left">
    <button class="button1">查看</button>
    <button class="button2">添加</button>
    <button class="button3">试卷</button>
</div>
<i class="page1">
    <h2>请输入你要调查的问题:</h2>
    <input type="text" name="category" placeholder="类别">
    <textarea name="" cols="30" rows="10" placeholder="题目"></textarea>
    <textarea name="" cols="30" rows="10" placeholder="A选项"></textarea>
    <textarea name="" cols="30" rows="10" placeholder="B选项"></textarea>
    <textarea name="" cols="30" rows="10" placeholder="C选项"></textarea>
    <textarea name="" cols="30" rows="10" placeholder="D选项"></textarea>
    <div class="answer">
        <h2>答案：</h2>
        <input type="checkbox" name="answerA" placeholder="答案">A</input>
        <input type="checkbox" name="answerB" placeholder="答案">B</input>
        <input type="checkbox" name="answerC" placeholder="答案">C</input>
        <input type="checkbox" name="answerD" placeholder="答案">D</input>
    </div>
    </div>

    <script type="text/javascript">


        $(".button2").click(function () {
            $(".page1").css({'display': 'block'});
        })


    </script>

</body>
</html>
