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
    <title>录入信息</title>
    <link rel="stylesheet" href="/ask/css/main.css">
    <script type="text/javascript" src="/ask/js/jquery-1.4.2.js"></script>
</head>
<body>
<div class="page1">
    <img src="/ask/img/timg.jpeg" class="img1">
    <img src="/ask/img/le.png" class="img2">
    <img src="/ask/img/logo.png" class="img3">

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

</div>

<div class="success">
    <div class="t3">上海壹萌商务咨询有限公司</div>
    <div class="t4">姓名：<span class="name1"></span></div>
    <div class="t4">公司：<span class="company1"></span></div>
    <div class="t4">职位：<span class="post1"></span></div>
    <div class="t4">电话：<span class="tell1"></span></div>
    <div class="t4">密码：<span class="yzm1"></span></div>
    <div class="tipes"><span class="tipes1">注意事项:</span>你的查询密码是你的验证码，请妥善保管。
        <%--<br>本次测评一共50题，整个测评时间 为30分，点击开始后即开始计时，30分钟到了 后，系统会自动交卷。--%>
    </div>
    <form action="/ask/views/query.jsp">
        <button class="goLogin">去登录</button>
    </form>

</div>


<div class="page3">
    <div class="backView">
        <div class="t5"></div>
    </div>
    <%--</div>--%>
    <%--<div class="log">--%>
    <%--</div>--%>

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


        /**
         * 获取验证 按钮
         */
        $(".buttonYzm").click(
            function () {

                var tell = $(".tell").val();
                if (tell == '') {
                    showtips("请输入手机号...");
                    return;
                }


                $.ajax({
                    url: "/ask/yzm",
                    type: "post",
                    data: {
                        phone: tell
                    },
                    dataType: "json",
                    success: function (json) {
                        var elema = json.flag;


                        if (elema == "true") {
                            showtips('验证码发送成功...');

                            var i = 59;
                            var colok = setInterval(function () {
                                $(".buttonYzm").text(i + '秒后获取');
                                $(".buttonYzm").css({pointerEvents: 'none', color: 'red'});
                                i--;
                                console.log("++")
                                if (i < 0) {
                                    $(".buttonYzm").text('重新获取验证码');
                                    $(".buttonYzm").css({pointerEvents: 'visible', color: 'white'});
                                    clearInterval(colok);
                                }
                            }, 1000);

                        } else if (elema != '') {
                            showtips('验证码发送失败...');
                        }

                    }
                });

            });


        /**
         *  confirm button
         */
        $(".button").click(
            function () {
                var name = $("[name='name']").val();
                var tell = $("[name='tell']").val();
                var yzm = $("[name='yzm']").val();
                var company = $("[name='company']").val();
                var post = $("[name='post']").val();


                console.log(name + "--" + tell + "--" + yzm + "---" + company + "----")


                if (tell == '') {
                    showtips("请输入手机号...");
                    return;
                } else if (yzm == '') {
                    showtips('请输入验证码...');
                    return;
                }


                $.ajax({
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    url: "/ask/confirm",
                    type: "post",
                    data: {
                        name: name,
                        tell: tell,
                        yzm: yzm,
                        company: company,
                        post: post
                    },
                    dataType: "text",
                    success: function (result) {
                        console.log('result=' + result);
                        if (result == '0') {
                            $(".page1").css({display: 'none'});
                            $(".success").css({display: 'block'}).animate({top: '0px'}, 500);
                            $(".start").animate({top: '70%'}, 500);
                            $(".name1").text($(".name").val());
                            $(".tell1").text($(".tell").val());
                            $(".company1").text($(".company").val());
                            $(".post1").text($(".post").val());
                            $(".yzm1").text($(".yzm").val());
                        } else if (result == '2') {
                            showtips('手机号或验证码错误.....');
                        } else {
                            showtips(result);
                        }


                    }
                })

            }
        )


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
