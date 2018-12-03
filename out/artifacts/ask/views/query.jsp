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


<div class="page11">
    <div class="queryPage">
        <input type="text" name="phoneQuery" class="phoneQuery" placeholder="电话" type="number" pattern="\d*">
        <input type="text" name="passwordQuery" class="passwordQuery" placeholder="密码" type="number" pattern="\d*">
        <div class="tips"></div>
        <button class="button">登录</button>
    </div>
    <div class="resultQuery">
        <%--<img src="/ask/img/male203.png" class="img">--%>
        <div class="uerName"></div>
        <div class="information">
            <div class="uerCom"></div>
            <div class="uerPost"></div>
            <div class="uerTell"></div>
            <div class="uerYnzm"></div>
        </div>
        <button class="resultGraph">查询测试结果</button>
        <div class="start">开始测试</div>
    </div>

    <div class="page2">
        <div class="clock">29:59</div>

        <div class="quesAry">
            <%--<div class="question">1、对定位的描述下列哪个选项正确?</div>--%>
            <%--<div class="chose">--%>
            <%--<div class="A">A.是要将品牌留在客户心目中</div>--%>
            <%--<div class="B">B.客户熟知品牌</div>--%>
            <%--<div class="C">C.给客户传递的关键信息</div>--%>
            <%--<div class="D">D.与竞品区隔, 以实现公司的潜在利益最大化</div>--%>
            <%--</div>--%>
        </div>


        <button class="upPage">上一页</button>
        <button class="wellDowne">交卷</button>
        <button class="dnPabe">下一页</button>

    </div>
</div>
<div class="removeSelf" style="background-color: rgb(45,45,45);
border-radius: 5px;box-shadow: 0 0 50px rgba(0,0,0,0.2);
width: 400px;height: 200px;position: fixed;margin: auto;top: 0;
bottom: 0;right: 0;left: 0;color: white;text-align: center;line-height: 200px;
z-index: 999;">请求失败...
</div>
<div class="resultScore">
    <div class="mark">D<sup>-</sup></div>
    <div class="scoreName">石川&nbsp;60分</div>
    <table>
        <tr class="tr1">
            <th>类别</th>
        </tr>
        <tr class="tr2">
            <th>3</th>
        </tr>
        <tr class="tr3">
            <th>10</th>
        </tr>
    </table>
</div>
<canvas id="canvas" width="500px" height="500px"></canvas>
<div class="canvas">
    <%--<div class="score" style="top:10px;right: 10px">95.55</div>--%>
</div>


<script type="text/javascript">
    function hidden() {
        $(".removeSelf").css({display: 'none'})
    }

    hidden();

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

    var usernamearr = [];
    $(".button").click(function () {
        usernamearr = [];

        var phoneQuery = $("[name='phoneQuery']").val();
        var passwordQuery = $("[name='passwordQuery']").val();


        if (phoneQuery == "") {
            $(".tips").text("电话号码为空....");
            return;
        }
        if (passwordQuery == "") {
            $(".tips").text("密码为空....");
            return;
        }


        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            url: "/ask/query",
            type: "post",
            data: {
                phoneQuery1: phoneQuery,
                passwordQuery1: passwordQuery,
            },
            error: function () {
                console.log("请求失败...")
            },
            dataType: "json",
            success: function (result) {

                console.log("用户登录=" + result.flag);

                if (result.flag == "fales") {
                    $(".tips").text("电话或密码错误....");
                } else {
                    $(".resultQuery").css({'display': 'block'}).animate({top: '0'}, 350);
                    $(".queryPage").animate({top: '10%', left: '10%', width: '80%', height: '80%'}, 350);
                    console.log('result=' + result.name);
                    $(".uerName").html("姓名:&nbsp;&nbsp;" + result.name).attr('userID', result.id);
                    $(".uerCom").html("公司:&nbsp;&nbsp;" + result.conpany);
                    $(".uerPost").html("职务:&nbsp;&nbsp;" + result.post);
                    $(".uerTell").html("电话:&nbsp;&nbsp;" + result.tell);
                    $(".uerYnzm").html("密码:&nbsp;&nbsp;" + result.yzm);

                    usernamearr.push(result.name);

                }


            }
        })

    })


    $(".startTest").click(function () {


        var phoneQuery = $("[name='phoneQuery']").val();
        $(".page2").css({display: 'none'});


        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            url: "/ask/startTest.do",
            type: "post",
            data: {
                phoneQuery1: phoneQuery,
            },
            error: function () {
                console.log("请求失败...")
            },
            // dataType: "json",
            success: function (result) {

                console.log("用户登录=" + result.flag);

                if (result.flag == "fales") {
                    $(".tips").text("电话或密码错误....");
                } else {
                    $(".resultQuery").css({'display': 'block'}).animate({top: '0'}, 350);
                    $(".queryPage").animate({top: '10%', left: '10%', width: '80%', height: '80%'}, 350);
                    console.log('result=' + result.name);
                    $(".uerName").html("姓名:&nbsp;&nbsp;" + result.name);
                    $(".uerCom").html("公司:&nbsp;&nbsp;" + result.conpany);
                    $(".uerPost").html("职务:&nbsp;&nbsp;" + result.post);
                    $(".uerTell").html("电话:&nbsp;&nbsp;" + result.tell);
                    $(".uerYnzm").html("密码:&nbsp;&nbsp;" + result.yzm);

                }


            }
        })
    })




    /**
     * start button
     */
    $(".start").mousedown(function () {

        getTestPaper();

        $(".start").animate({}, 250).css({'backgroundColor': 'rgb(240,240,240)'});
        setTimeout(function () {
            $(".start").animate({width: '100%', height: '100%', top: '0px'}, 250);
            setTimeout(function () {
                $(".start").animate({'border-radius': '0'}, 50);
                setTimeout(function () {
                    $(".start").css({display: 'none'});
                    $(".success").css({display: 'none'});
                    $(".page1").css({display: 'none'});
                    $(".page2").css({display: 'block'});

                    getPage(0);
                    // $(".dnPabe").click();
                    animationABCD();
                    clock();


                }, 250);
            }, 250);
        }, 250);

    })


    /**
     * 获取试卷
     */
    var testPaper = [];

    function getTestPaper() {

        testPaper = [];

        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: 'post',
            url: '/ask/startTest.do',
            traditional: true,
            error: function () {// 请求失败处理函数
                // showtips("请求失败.....")
            },
            dataType: 'json',
            success: function (json) {
                console.log(json);


                /**
                 * 此处可以直接用 testPaper = json，为了代码的阅读用下面的方法来展示
                 */
                for (var i = 0; i < json.length; i++) {

                    var itemQues = new Object();
                    itemQues.id = json[i].idQus;
                    itemQues.title = json[i].title;
                    itemQues.A = json[i].A;
                    itemQues.B = json[i].B;
                    itemQues.C = json[i].C;
                    itemQues.D = json[i].D;

                    testPaper.push(itemQues);

                }

                console.log("testPaper.length: " + testPaper.length);
                console.log("testPaper[0].title: " + testPaper[0].title);

                for (var i = 0; i < json.length; i++) {

                    console.log("testPaper[x].title: " + testPaper[i].title);

                }


            }
        })
    }

    function animationABCD() {
        setTimeout(function () {
            $(".A").animate({opacity: '1'}, 100);
            setTimeout(function () {
                $(".B").animate({opacity: '1'}, 100);
                setTimeout(function () {
                    $(".C").animate({opacity: '1'}, 100);
                    setTimeout(function () {
                        $(".D").animate({opacity: '1'}, 100);
                    }, 50);
                }, 100);
            }, 150);
        }, 200);
    }

    function clock() {
        var i = 1800 - 1;
        var clock = setInterval(function () {
            var a = parseInt(i / 60);
            var b = i % 60;
            $(".clock").text(a + ":" + b);
            if (i <= 0) {
                clearInterval(clock);
                i = 1800 - 1;
            }
            i--;
        }, 1000);
    }

    /**
     * 上一页
     */
    var currentPger = 0;
    $(".upPage").click(function () {
        currentPger--;
        if (currentPger <= 0) {
            currentPger = 0;
        }
        var id = $(".question").attr("id");
        answer(id);
        getPage(currentPger);
        var NEWid = $(".question").attr("id");
        answerBackShow(NEWid);
        animationABCD();
    })
    /**
     * 下一页
     */
    $(".dnPabe").click(function () {


        console.log("answerArray.length:" + answerArray.length);

        /**
         * 保存当前的状态，添加到数组中
         */
        var id = $(".question").attr("id");
        answer(id);

        /**
         * 加一跳到新的状态
         */
        ++currentPger;
        if (currentPger >= testPaper.length - 1) {
            currentPger = testPaper.length - 1;
            $(".wellDowne").css({display: 'block'});


        }
        getPage(currentPger);

        /**
         *回显示答案状态
         */
        var NEWid = $(".question").attr("id");
        answerBackShow(NEWid);
        animationABCD();


        console.log("answerArray.length2:" + answerArray.length);

    })

    function getPage(currentPger) {
        $(".quesAry").html("");
        $(".quesAry").html("<div class=\"question\"id=\"" + testPaper[currentPger].id + "\">" + (currentPger + 1) + ".&nbsp;" + testPaper[currentPger].title + "</div>\n" +
            "        <div class=\"chose\">\n" +
            "            <div class=\"A\">A." + testPaper[currentPger].A + "</div>\n" +
            "            <div class=\"B\">B." + testPaper[currentPger].B + "</div>\n" +
            "            <div class=\"C\">C." + testPaper[currentPger].C + "</div>\n" +
            "            <div class=\"D\">D." + testPaper[currentPger].D + "</div>\n" +
            "        </div>" +
            "<div class=\"space\"></div>")
    }

    $(".quesAry").click(function (even) {

        var element = $(even.target);
        var color = $(even.target).css('border-color');
        var elName = even.target.className;


        if (elName == 'A' | elName == 'B' | elName == 'C' | elName == 'D') {
            //获取背景颜色攫取颜色值
            if (color.substring(4, 7) == '100') {
                element.css({'border': 'white 5px solid'});
            } else {
                element.css({'border': 'rgb(100,100,100) 5px solid'});
            }
            console.log('你选择了' + elName);
        }
    })

    function answerABCD() {
        var elems = $(".chose").children();
        var answer = new Object();


        var color = $(elems[0]).css('border-color');
        if (color == 'rgb(100, 100, 100)') {
            answer.A = 'A';
        } else {
            answer.A = "";
        }
        var color1 = $(elems[1]).css('border-color');
        if (color1 == 'rgb(100, 100, 100)') {
            answer.B = 'B';
        } else {
            answer.B = "";
        }
        var color2 = $(elems[2]).css('border-color');
        if (color2 == 'rgb(100, 100, 100)') {
            answer.C = 'C';
        } else {
            answer.C = "";
        }
        var color3 = $(elems[3]).css('border-color');
        if (color3 == 'rgb(100, 100, 100)') {
            answer.D = 'D';
        } else {
            answer.D = "";
        }

        console.log(answer);
        return answer;
    }

    /**
     * 用户的答案集合
     * @type {Array}
     */
    var answerArray = [];

    function answer(id) {

        var testItem = new Object();
        testItem.id = id;
        testItem.answer = answerABCD();
        answerArray.push(testItem);

        console.log(answerArray);
    }

    function answerBackShow(NEWid) {


        for (var i = 0; i < answerArray.length; i++) {

            var id = answerArray[i].id;


            if (id == NEWid) {

                var A = answerArray[i].answer.A;
                var B = answerArray[i].answer.B;
                var C = answerArray[i].answer.C;
                var D = answerArray[i].answer.D;

                if (A == 'A') {
                    $(".A").css({'border': 'rgb(100,100,100) 5px solid'});
                }
                if (B == 'B') {
                    $(".B").css({'border': 'rgb(100,100,100) 5px solid'});
                }
                if (C == 'C') {
                    $(".C").css({'border': 'rgb(100,100,100) 5px solid'});
                }
                if (D == 'D') {
                    $(".D").css({'border': 'rgb(100,100,100) 5px solid'});
                }
                /**
                 * 删除旧的状态
                 */
                answerArray.splice(i, 1);
            }

        }
    }


    $(".wellDowne").click(function () {

        var id = $(".question").attr("id");
        answer(id);

        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: "post",
            url: "/ask/wellDowne.do",
            traditional: true,
            data: {
                uerID: $(".uerName").attr('userid'),
                answer: JSON.stringify(answerArray)
            },
            error: function () {
                $(".removeSelf").css({display: 'block'});
            },
            dataType: "json",
            success: function (json) {
                console.log(json);
                showAnswerResult(json);
                $("#canvas").css({display: 'block'});
                $(".canvas").css({display: 'block'});
                $(".page11").css({display: 'none'});
                $(".resultScore").css({display: 'block'});
                $('body').css({'background-color': 'rgb(70,70,70)'});
            }

        })


    })

    $(".removeSelf").click(function (even) {
        $(even.target).css({display: 'none'});
        console.log($(this.target));
    })

    /**
     * {"ok":{score:6,allScore:10,answerArray:[{answer: "B", grade: "1", quID: "143", category: "你好", userID: "102"}
     *                             ,{answer: "B", grade: "1", quID: "143", category: "你好", userID: "102"}]
     * }}
     * 注意js中arry是字典的父类，如果要跟新一个key的值,不能用push().
     * 跟新到方式 a[key] = 5,a[key] = 6。
     * @param array
     */
    function showAnswerResult(array) {

        var categorys = {};

        for (var i = 0; i < array.length; i++) {

            var category = array[i].category;

            var flag = false;
            for (var it in categorys) {
                if (it == category) {
                    flag = true;
                }
            }

            /**
             *如果这个类已经添加到复合字典中
             */
            if (flag == true) {

                if (array[i].grade == '1') {
                    categorys[category].answerArray.push(array[i]);
                    categorys[category].score = categorys[category].score + 1;
                } else {
                    categorys[category].answerArray.push(array[i]);


                }

                /**
                 * 通一个key的值跟新，不能用push();
                 */
                categorys[category].allScore = categorys[category].allScore + 1;

            }
            /**
             *如果这个类没有添加到复合字典中
             */
            else {
                categorys[category] = [];//js字典的写法

                if (array[i].grade == '1') {
                    categorys[category] = ({score: 1, allScore: 1, answerArray: [array[i]]});
                } else {
                    categorys[category] = ({score: 0, allScore: 1, answerArray: [array[i]]});
                }

            }

        }
        console.log("+++++++++++++");
        console.log(categorys);
        console.log("+++++++++++++");

        drawSpider(categorys);

    }

    function drawSpider(categorys) {


        var canvas = document.getElementById("canvas");
        var context = canvas.getContext('2d');
        /**
         解决JS画图模糊的问题
         */
        var width = canvas.width, height = canvas.height;
        if (window.devicePixelRatio) {

            canvas.style.width = width + "px";
            canvas.style.height = height + "px";
            canvas.height = height * window.devicePixelRatio;
            canvas.width = width * window.devicePixelRatio;
            context.scale(window.devicePixelRatio, window.devicePixelRatio);
        }

        var categorysCount = Object.keys(categorys).length;


        context.beginPath();
        context.fillStyle = "rgb(50,50,50)";
        drawPolygon(240, categorysCount, context);
        context.fill();
        /**
         * 底图画完
         */
        context.closePath();


        context.beginPath();
        context.strokeStyle = "rgb(70,80,100)";
        context.lineWidth = 1;

        /**
         * 画多边型
         */
        drawPolygon(192, categorysCount, context);
        drawPolygon(144, categorysCount, context);
        drawPolygon(96, categorysCount, context);
        drawPolygon(48, categorysCount, context);

        var pointArray = drawPolygon(240, categorysCount, context);
        context.stroke();
        context.closePath();


        context.beginPath();
        console.log(pointArray);

        var j = 0;

        var firstPointFlag = 0;
        var firstPoint = [];
        var o1 = $("#canvas").width() / 2;
        var o2 = $("#canvas").height() / 2;

        // context.moveTo(o1,o2);


        var html = "";
        var htmlTr1 = "";
        var htmlTr2 = "";
        var htmlTr3 = "";

        var allScore = 0;
        var allScore1 = 0;
        var allScore2 = 0;
        for (var key in categorys) {


            /**
             * 计算答题对的比例
             */
            var K = categorys[key].score / categorys[key].allScore;

            allScore1 += categorys[key].score;
            allScore2 += categorys[key].allScore;

            var score = (K * 100).toFixed(0);
            console.log("类别:" + key + "  分数:" + score);
            var point = pointArray[j];

            /**
             * 一个类的成绩点
             */
            var x = point.x * K + o1;
            var y = point.y * K + o2;


            var x1 = point.x + o1;
            var y1 = point.y + o2


            /**
             * 第一象限
             */
            if ((500 - x1 - 5 <= 245.5) && ((y1 + 8) <= 277.5)) {
                x1 = x1 + 45;
                y1 = y1 - 45;
            }
            /**
             * 第二象限
             */
            else if ((500 - x1 - 5 <= 245.5) && ((y1 + 8) > 277.5)) {
                x1 = x1 + 40;
                y1 = y1 - 10;
            }

            /**
             * 第三象限
             */
            else if ((500 - x1 - 5 > 245.5) && ((y1 + 8) > 277.5)) {
                x1 = x1 + 0;
                y1 = y1 - 20;
            }
            /**
             * 第四象限
             */
            else if ((500 - x1 - 5 > 245.5) && ((y1 + 8) < 277.5)) {
                x1 = x1 - 5;
                y1 = y1 - 30;
            }


            html += "<div class=\"score\" style=\"top:" + (y1 + 8) + "px;right: " + (500 - x1 - 5) + "px\">" + key + "<br>" + score + "</div>\n" +
                "</div>"


            htmlTr1 += "<th>" + key + "</th>";
            htmlTr2 += "<th>" + categorys[key].score + "</th>";
            htmlTr3 += "<th>" + categorys[key].allScore + "</th>";


            console.log("x:" + (500 - x1 - 5) + ",y:" + (y1 + 20 + 8));


            context.lineTo(x, y);
            context.arc(x, y, 2.5, 0, 2 * Math.PI);


            /**
             * 保存第一个点，画封口线
             */
            if (firstPointFlag == 0) {
                firstPointFlag = 1;
                firstPoint.push(x);
                firstPoint.push(y);
            }
            /**
             * 画封口线
             */
            if (j == Object.keys(categorys).length - 1) {
                context.lineTo(firstPoint[0], firstPoint[1]);
            }
            j++;
        }
        $(".canvas").html(html);
        $(".tr1").html(htmlTr1);
        $(".tr2").html(htmlTr2);
        $(".tr3").html(htmlTr3);


        allScore = (allScore1/allScore2)*100;
        if (allScore == 100) {
            $(".mark").html("A<sup>+</sup>");
            $(".scoreName").html(usernamearr[0] + "&nbsp;" + allScore.toFixed(1)+"分");
        } else if (allScore < 100 && allScore >= 95) {
            $(".mark").html("A<sup>-</sup>");
            $(".scoreName").html(usernamearr[0]+ "&nbsp;" + allScore.toFixed(1)+"分");
        }else if (allScore < 95 && allScore >= 90) {
            $(".mark").html("B<sup>+</sup>");
            $(".scoreName").html(usernamearr[0] + "&nbsp;" + allScore.toFixed(1)+"分");
        }else if (allScore < 90 && allScore >= 85) {
            $(".mark").html("B<sup>-</sup>");
            $(".scoreName").html(usernamearr[0] + "&nbsp;" + allScore.toFixed(1)+"分");
        }else if (allScore < 85 && allScore >= 80) {
            $(".mark").html("C<sup>+</sup>");
            $(".scoreName").html(usernamearr[0] + "&nbsp;" + allScore.toFixed(1)+"分");
        }else if (allScore < 80 && allScore >= 75) {
            $(".mark").html("C<sup>-</sup>");
            $(".scoreName").html(usernamearr[0] + "&nbsp;" + allScore.toFixed(1)+"分");
        }else if (allScore < 75 && allScore >= 70) {
            $(".mark").html("D<sup>+</sup>");
            $(".scoreName").html(usernamearr[0] + "&nbsp;" + allScore.toFixed(1)+"分");
        }else if (allScore < 70 && allScore >= 65) {
            $(".mark").html("D<sup>-</sup>");
            $(".scoreName").html(usernamearr[0] + "&nbsp;" + allScore.toFixed(1)+"分");
        }else if (allScore < 65 && allScore >= 60) {
            $(".mark").html("E<sup>+</sup>");
            $(".scoreName").html(usernamearr[0] + "&nbsp;" + allScore.toFixed(1)+"分");
        }else {
            $(".mark").html("E<sup>-</sup>");
            $(".scoreName").html(usernamearr[0] + "&nbsp;" + allScore.toFixed(1)+"分");
        }


        context.lineWidth = 2;
        context.lineCap = "round";
        context.fillStyle = "rgba(230,200,30,0.3)";
        context.strokeStyle = 'rgb(255,200,90)';


        context.shadowBlur = 6;
        context.shadowColor = "black";
        context.shadowOffsetX = 0;
        context.shadowOffsetY = 0;


        context.fill();
        context.stroke();
        context.closePath();


    }

    function drawPolygon(r, n, context) {


        var angle = Math.PI / (n / 2);
        console.log("angle:" + angle);
        var o1 = $("#canvas").width() / 2;
        var o2 = $("#canvas").height() / 2;


        // 创建渐变
        var gradient = context.createLinearGradient(0, 0, 500, 0);
        gradient.addColorStop("0", "magenta");
        gradient.addColorStop("0.5", "blue");
        gradient.addColorStop("1.0", "red");
        //context.strokeStyle = gradient;
        context.lineCap = "round";


        context.moveTo((r * Math.sin(angle * 1)) + o1, (r * Math.cos(angle * 1)) + o2);


        var pointArray = [];

        for (var i = 0; i <= n; i++) {

            x = (r * Math.sin(angle * i)) + o1;
            y = (r * Math.cos(angle * i)) + o2;

            context.lineTo(x, y);
            context.moveTo(o1, o2);
            context.lineTo((r * Math.sin(angle * i)) + o1, (r * Math.cos(angle * i)) + o2);
            if (r == 240) {
                /**
                 * 保存最外部多边形的点
                 */
                pointArray.push({"x": (r * Math.sin(angle * i)), "y": (r * Math.cos(angle * i))});
            }

        }
        return pointArray;
    }


</script>
</body>
</html>
