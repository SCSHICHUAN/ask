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

    <div class="question">1、对定位的描述下列哪个选项正确?</div>


    <div class="chose">
        <div class="A">A.是要将品牌留在客户心目中</div>
        <div class="B">B.客户熟知品牌</div>
        <div class="C">C.给客户传递的关键信息</div>
        <div class="D">D.与竞品区隔, 以实现公司的潜在利益最大化</div>
    </div>

</div>


<script type="text/javascript">

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
                    $(".uerName").html("姓名:&nbsp;&nbsp;" + result.name);
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

                    setTimeout(function () {
                        $(".A").animate({opacity: '1'}, 100);
                        setTimeout(function () {
                            $(".B").animate({opacity: '1'}, 100);
                            setTimeout(function () {
                                $(".C").animate({opacity: '1'}, 100);
                                setTimeout(function () {
                                    $(".D").animate({opacity: '1'}, 100);
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

                                }, 50);
                            }, 100);
                        }, 150);
                    }, 200);


                }, 250);
            }, 250);
        }, 250);

    })

    $(".chose").click(function (even) {

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


</script>
</body>
</html>
