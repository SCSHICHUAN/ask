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


    $(".button").click(function () {

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
        if (currentPger >= testPaper.length-1) {
            currentPger = testPaper.length-1;
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

            },
            dataType: "json",
            success: function (json) {

            }


        })


    })


</script>
</body>
</html>
