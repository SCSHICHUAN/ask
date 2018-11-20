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
<div class="yulan">
    <div class="header">
        <img src="/ask/img/logo.png" class="img3">
        <div class="headerT">上海壹萌商务咨询有限公司管理系统</div>
        <button class="button1">查看成绩</button>
        <button class="button3">问卷卷库</button>
        <button class="button5">问卷题库</button>
    </div>
    <div class="left">
        <div class="buttonPage1">
            <button class="button8">试题</button>
            <button class="button9">题目</button>
            <button class="button7">选题</button>
            <button class="button2">添加</button>
            <button class="button14">删除</button>
        </div>
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

    <div class="page2">
        <div class="clear">
            <%--<div class="ques">--%>
            <%--<div class="category">类别</div>--%>
            <%--<div class="title">是目前调查业中所广泛采用的调查方式</div>--%>
            <%--<div class="A">A:调查方式具有较强的科学性，同时也便于操作</div>--%>
            <%--<div class="B">B:调查方式具有较强的科学性</div>--%>
            <%--<div class="C">C:不同的问卷所包括的开头部分会有一定的差别</div>--%>
            <%--<div class="D">D:问候语一方面要反映以上内容；另一方面要求尽量简</div>--%>
            <%--<div class="answer">答案:AC</div>--%>
            <%--</div>--%>
            <%--<div class="shouse">选择</div>--%>
        </div>
        <div class="bacBU">
            <div class="buttons">
                <div class="totalPages"></div>
                <input class="shousuo" name="shousuo" placeholder="页码" PATTERN="\d*">
                <button class="pgQuery">搜索</button>
                <button class="pgUp">上一页</button>
                <button class="pgDn">下一页</button>
                <button class="button10">加入试卷</button>
            </div>
        </div>
        <div class="QuesTitle">
            <div class="title2">生层试卷</div>
            <input name="title" class="title" placeholder="试卷名称">
            <button class="button1QuesTitle">✕</button>
            <button class="button12">预览试卷</button>
            <button class="button11">发送到试卷库</button>
        </div>
    </div>
</div>

<div class="page3">
    <div class="backView">
        <div class="t5"></div>
    </div>
</div>
<div class="page4">
    <div class="title1"></div>
    <div class="body"></div>
    <button class="button13">关闭</button>
</div>
<div class="page5">
    <button class="page5button">取消删除题目</button>
    <button class="page5button1">确定删除题目</button>
</div>
<div class="page6">
    <div class="page6Left">
        <div class="page6h2">问卷列表</div>
        <div class="page6title">
            <%--<div class="page6body" style="background-color: red">关于贵州的问卷调查</div>--%>
            <%--<div class="page6body" style="background-color: red">关于贵州的问卷调查</div>--%>
            <%--<div class="page6body" style="background-color: red">关于贵州的问卷调查</div>--%>
        </div>
    </div>
    <div class="page6Right">
        <div class="table"></div>
        <div class="page6body"></div>
    </div>
</div>



<script type="text/javascript">


    var xuanti = 0;
    var timu = 0;

    /**
     * 添加题目
     */
    $(".button2").click(function () {
        $(".page1").css({'display': 'block'});
        $(".page2").css({'display': 'none'});
        $(".page6").css({'display': 'none'});

    })

    /**
     * 添加入题库
     */
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
            return;
        } else if ((title == "")) {
            showtips("请输入题目....")
            return;
        } else if ((tA == "")) {
            showtips("请输入A选项....")
            return;
        } else if ((tB == "")) {
            showtips("请输入B选项....")
            return;
        } else if ((tC == "")) {
            showtips("请输入C选项....")
            return;
        } else if ((tD == "")) {
            showtips("请输入D选项....")
            return;
        } else if (!(A | B | C | D)) {
            showtips("答案为空....")
            return;
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
                answer: answer
            },
            dataType: "text",
            success: function (json) {
                if (json == "true") {
                    showtips("添加成功...");


                    $("[name=\"category\"]").val("");
                    $("[placeholder=\"题目\"]").val("");

                    $("[placeholder=\"A选项\"]").val("");
                    $("[placeholder=\"B选项\"]").val("");
                    $("[placeholder=\"C选项\"]").val("");
                    $("[placeholder=\"D选项\"]").val("");


                    $("[name=\"answerA\"]").attr('checked', false);
                    $("[name=\"answerB\"]").attr('checked', false);
                    $("[name=\"answerC\"]").attr('checked', false);
                    $("[name=\"answerD\"]").attr('checked', false);


                } else {
                    showtips("添加失败，题目可能重复...")
                }
            }
        })


    })


    var currentPage = 1;
    var totalPage = 0;
    var idArry = [];
    /**
     * 题库
     */
    $(".button5").click(function () {
        xuanti = 0;
        timu = 0;
        idArry = [];
        $(".page2").css({'display': 'block'});
        $(".buttonPage1").css({display: 'block'});
        $(".page1").css({'display': 'none'});
        $(".page6").css({'display': 'none'});
        $(".A,.B,.C,.D,.answer").css({'display': 'block'});
        refreshQuestions();
    })
    $(".button5").click();
    $(".pgUp").click(function () {

        --currentPage;
        if (currentPage <= 0) {
            currentPage = 1;
        }
        getTestItem(currentPage);

    })
    $(".pgDn").click(function () {
        ++currentPage;
        if (currentPage >= totalPage) {
            currentPage = totalPage;
        }
        getTestItem(currentPage);
    })

    $(".pgQuery").click(function () {

        var page = $("[name=\"shousuo\"]").val();
        if (isNaN(page) | page == "") {
            showtips("请输入数字.....");
            $("[name=\"shousuo\"]").val("");
            return;
        } else if (page > totalPage) {
            showtips("超过最大页....");
            $("[name=\"shousuo\"]").val("");
            return;
        } else if (page <= 0) {
            showtips("请输入正确的数字.....");
            $("[name=\"shousuo\"]").val("");
            return;
        }
        currentPage = page;
        getTestItem(page);
    })

    /**
     * 通过页数查看题库
     */
    function getTestItem(page) {

        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: "post",
            url: "/ask/queryItem",
            traditional: true,
            data: {
                currentPage: page
            },
            dataType: 'json',
            error: function () {// 请求失败处理函数
                showtips("请求过于频繁.....")
            },
            success: function (json) {
                console.log(json);
                var i = 1;
                i = json.length;
                console.log(i);


                var html = "";
                $(".clear").html(html);
                for (var i = 0; i < json.length - 1; i++) {

                    var id = json[i].idQus;
                    var category = json[i].category;
                    var title = json[i].title;
                    var A = json[i].A;
                    var B = json[i].B;
                    var C = json[i].C;
                    var D = json[i].D;
                    var answer = json[i].answer;

                    var color = 'rgb(250,250,250)';
                    if (i % 2 == 0) {
                        color = 'rgb(255,255,255)';
                    }

                    html += "<div class=\"ques\" style=\"position: relative; background-color:" + color + "\">\n" +
                        "    <div class=\"category\">类别:" + category + "</div>\n" +
                        "    <div class=\"title\">标题:" + title + "</div>\n" +
                        "    <div class=\"A\">A:" + A + "</div>\n" +
                        "    <div class=\"B\">B:" + B + "</div>\n" +
                        "    <div class=\"C\">C:" + C + "</div>\n" +
                        "    <div class=\"D\">D:" + D + "</div>\n" +
                        "    <div class=\"answer\">答案:" + answer + "</div>\n" +
                        "    <div class=\"shouse\" id = \'" + id + "\'>选择</div>" +
                        "</div>";

                }

                html +="<div class=\"page6Buttom\"></div>"

                console.log(html);
                totalPage = json[json.length - 1].pages;
                $(".totalPages").html("共&nbsp;" + totalPage + "&nbsp;页");
                $("[name=\"shousuo\"]").val(currentPage);

                $(".clear").prepend(html);

                console.log("totalPage: " + totalPage);
                console.log("currentPage: " + currentPage);


                if (xuanti == 1) {
                    $(".shouse").css({'display': 'block'});
                } else if (xuanti == 0) {
                    $(".shouse").css({'display': 'none'});
                }

                if (timu == 1) {
                    $(".A,.B,.C,.D,.answer").css({'display': 'none'});
                } else if (timu == 0) {
                    $(".A,.B,.C,.D,.answer").css({'display': 'block'});
                }


                /**
                 * 设置选中的按钮的颜色，如果数组中有就变色
                 */
                chouseedItemBackgroundColor();

            }

        })


    }


    function refreshQuestions() {
        getTestItem(1);
    }


    /**
     * 题目
     */
    $(".button9").click(function () {
        refreshQuestions();
        $(".page2").css({'display': 'block'});
        $(".page1").css({'display': 'none'});

        $(".A,.B,.C,.D,.answer").css({'display': 'none'});
        timu = 1;

    })


    /**
     * 试题
     */
    $(".button8").click(function () {
        refreshQuestions();

        $(".page2").css({'display': 'block'});
        $(".page1").css({'display': 'none'});

        $(".A,.B,.C,.D,.answer").css({'display': 'block'});
        timu = 0;
    })


    /**
     * 选题
     * 如果选题按钮按下就把xuanti设置为"1"，隐藏题目，每次刷新都要看一下是不是
     * "1"如果是，就要隐藏选项和答案，反之亦然。
     */
    $(".button7").click(function () {
        refreshQuestions();

        $(".page2").css({'display': 'block'});
        $(".page1").css({'display': 'none'});
        $(".shouse").css({'display': 'block'});

        xuanti = 1;

    })


    /**
     * 添加题目
     */
    $(".clear").click(function (even) {

        var emel = even.target;
        if (emel.className != "shouse") return;
        var id = emel.id;


        idArry.push(id);

        for (var i = 0; i < idArry.length - 1; i++) {
            var oldId = idArry[i];

            if (oldId == id) {

                var elm = $("[id=\"" + id + "\"]")
                /**
                 *用户返选马上变色，默认是没有选中的颜色
                 */
                elm.css({'background-color': 'rgb(255,255,255)'});
                /**
                 * 删除已经选中的id，和当前加入的id。
                 */
                idArry.splice(i, 1)
                idArry.pop();
            }

        }
        /**
         * 每点击一次就要给所有的选择按钮变色
         */
        chouseedItemBackgroundColor();
        console.log(emel);
    })


    function chouseedItemBackgroundColor() {
        for (var i = 0; i < idArry.length; i++) {
            console.log(idArry[i]);
            var id = idArry[i];
            var elm = $("[id=\"" + id + "\"]")
            elm.css({'background-color': 'rgb(3,192,222)'});
        }
    }

    function showtips(tipsStr) {
        if (tipsStr == '') return;
        $(".page3").css({display: 'block'});
        $(".t5").html(tipsStr)
        setTimeout(function () {
            $(".page3").css({display: 'none'});
        }, 1500)

    }

    /**
     * 加入题库
     */
    $(".button10").click(function () {
        if (idArry.length == 0) {
            return showtips("请选择题目...")
        }
        $(".QuesTitle").css({display: 'block'});
        // var textTitle =

    })

    $(".button11").click(function () {

        var title = $("[placeholder=\"试卷名称\"]").val();
        if (title == "") return showtips("请输入试卷名称.....");
        $(".QuesTitle").css({display: 'none'});

        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: 'post',
            url: '/ask/QuestionBack',
            traditional: true,
            data: {
                title: title,
                ids: JSON.stringify(idArry),
            },
            error: function () {// 请求失败处理函数
                showtips("请求过于频繁.....")
            },
            dataType: 'text',
            success: function (text) {
                $(".QuesTitle .title").val("");
                showtips(text);
            }
        })

    })
    $(".button12").click(function () {
        $(".page6").css({display: 'none'});
        var title = $("[placeholder=\"试卷名称\"]").val();
        if (title == "") return showtips("请输入试卷名称.....");


        $(".page4 .title1").html("");
        $(".page4 .title1").html(title);


        for (var i = 0; i < idArry.length; i++) {
            console.log(idArry[i] + '\n');
        }


        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: 'post',
            url: '/ask/preview',
            traditional: true,
            data: {
                title: title,
                ids: JSON.stringify(idArry),
            },
            error: function () {// 请求失败处理函数
                showtips("请求过于频繁.....")
            },
            dataType: 'json',
            success: function (json) {


                $(".page4").css({display: 'block'});
                $(".yulan").css({display: 'none'});


                var html = "";
                $(".page4 .body").html(html);
                for (var i = 0; i < json.length; i++) {


                    var title = json[i].title;
                    var A = json[i].A;
                    var B = json[i].B;
                    var C = json[i].C;
                    var D = json[i].D;


                    var color = 'rgb(250,250,250)';
                    if (i % 2 == 0) {
                        color = 'rgb(255,255,255)';
                    }

                    html += "<div class=\"ques\" style=\"position: relative\">\n" +
                        "    <div class=\"title\">" + (i + 1) + ".&nbsp;&nbsp;" + title + "</div>\n" +
                        "    <div class=\"A\">A:" + A + "</div>\n" +
                        "    <div class=\"B\">B:" + B + "</div>\n" +
                        "    <div class=\"C\">C:" + C + "</div>\n" +
                        "    <div class=\"D\">D:" + D + "</div>\n" +
                        "</div>";

                }

                html +="<div class=\"page6Buttom\"></div>"
                $(".page4 .body").html(html);

            }
        })


    })

    $(".button13").click(function () {
        $(".page4").css({display: 'none'});
        $(".yulan").css({display: 'block'});
    })
    $(".button14").click(function () {
        if (idArry.length <= 0) return showtips("请选择要删除的题目...");
        $(".page5").css({display: 'block'});

    })
    $(".page5button").click(function () {
        $(".page5").css({display: 'none'});
    })
    $(".page5button1").click(function () {

        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: 'post',
            url: '/ask/delectItem',
            traditional: true,
            data: {
                ids: JSON.stringify(idArry),
            },
            error: function () {// 请求失败处理函数
                showtips("请求过于频繁.....")
            },
            dataType: 'text',
            success: function (text) {
                refreshQuestions();
                $(".page5").css({display: 'none'});
                idArry = [];
                console.log("你成功删除" + text + "题目...");
                showtips("成功删除:&nbsp;" + text + "&nbsp;道题。");
            }
        })


    })
    $(".button3").click(function () {
        $(".page1").css({'display': 'none'});
        $(".page6").css({display: 'block'});
        $(".page2").css({display: 'none'});
        $(".buttonPage1").css({display: 'none'});


        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: 'post',
            url: '/ask/showTables',
            traditional: true,
            error: function () {// 请求失败处理函数
                showtips("请求过于频繁.....")
            },
            dataType: 'json',
            success: function (json) {


                var html = "";
                $(".page6title").html(html);
                for (var i = 0; i < json.length; i++) {


                    var table = json[i].table;

                    console.log("table=" + table)

                    var color = 'rgb(250,250,250)';
                    if (i % 2 == 0) {
                        color = 'rgb(255,255,255)';
                    }


                    html += " <div class=\"page6body\" style=\"background-color: " + color + "\"><div class='page6hover'>" + table + "</div></div>";

                }

                $(".page6title").html(html);

            }
        })


    })
    $(".button1QuesTitle").click(function () {
        $(".QuesTitle .title").val("");
        $(".QuesTitle").css({display: 'none'});
    })
    $(".page6title").click(function (even) {
        var elem = even.target;
        if (elem.className != "page6hover") return;
        var table = $(elem).text()
        console.log(table);


        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: 'post',
            url: '/ask/showQuestions',
            traditional: true,
            data: {
                table: table,
            },
            error: function () {// 请求失败处理函数
                showtips("请求过于频繁.....")
            },
            dataType: 'json',
            success: function (json) {
                console.log(json);


                $(".page6Right .table").text(table);

                var html = "";

                for (var i = 0; i < json.length; i++) {


                    var title = json[i].title;
                    var A = json[i].A;
                    var B = json[i].B;
                    var C = json[i].C;
                    var D = json[i].D;


                    var color = 'rgb(250,250,250)';
                    if (i % 2 == 0) {
                        color = 'rgb(255,255,255)';
                    }

                    html += "<div class=\"ques1\">" +
                        "    <div class=\"title1\">" + (i + 1) + ".&nbsp;&nbsp;" + title + "</div>\n" +
                        "    <div class=\"A1\">A:" + A + "</div>\n" +
                        "    <div class=\"B1\">B:" + B + "</div>\n" +
                        "    <div class=\"C1\">C:" + C + "</div>\n" +
                        "    <div class=\"D1\">D:" + D + "</div>\n" +
                        "</div>";

                }

                html +="<div class=\"page6Buttom\"></div>"

                $(".page6Right .page6body").html(html);


            }
        })


    })


</script>
</body>
</html>
