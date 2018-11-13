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
<div class="page1">
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
    <button class="button4">加入题库</button>


</div>


<div class="page3">
    <div class="backView">
        <div class="t5"></div>
    </div>
</div>
<script type="text/javascript">


    $(".button2").click(function () {
        $(".page1").css({'display': 'block'});
    })

    $(".button4").click(function () {

        var category = $("[name=\"category\"]").val();
        var title = $("[placeholder=\"题目\"]").val();

        var tA = $("[placeholder=\"A选项\"]").val();
        var tB = $("[placeholder=\"B选项\"]").val();
        var tC = $("[placeholder=\"C选项\"]").val();
        var tD = $("[placeholder=\"D选项\"]").val();


        var A = $("[name=\"answerA\"]").attr('checked');
        var B = $("[name=\"answerB\"]").attr('checked');
        var C = $("[name=\"answerC\"]").attr('checked');
        var D = $("[name=\"answerD\"]").attr('checked');

        if ((category == "")) {
            showtips("请输入类别....")
        } else if ((title == "")) {
            showtips("请输入题目....")
        } else if ((tA == "")) {
            showtips("请输入A选项....")
        } else if ((tB == "")) {
            showtips("请输入B选项....")
        } else if ((tC == "")) {
            showtips("请输入C选项....")
        } else if ((tD == "")) {
            showtips("请输入D选项....")
        } else if (!(A | B | C | D)) {
            showtips("答案为空....")
        }


        var answer = "";
        if (A) {
            answer = answer + "A";
        }
        if (B) {
            answer = answer + "B"
        }
        if (C) {
            answer = answer + "C"
        }
        if (D) {
            answer = answer + "D"
        }

        console.log(answer);

        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: "post",
            url: "/ask/ques",
            data: {
                category: category,
                title: title,
                tA: tA,
                tB: tB,
                tC: tC,
                tD: tD,
                answer:answer
            },
            dataType: "json",
            success: function (json) {

            }
        })


    })


    function showtips(tipsStr) {
        if (tipsStr == '') return;
        $(".page3").css({display: 'block'});
        $(".t5").html(tipsStr)
        setTimeout(function () {
            $(".page3").css({display: 'none'});
        }, 2500)

    }


</script>

</body>
</html>
