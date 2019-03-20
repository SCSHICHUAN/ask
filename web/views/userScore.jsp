<%--
  Created by IntelliJ IDEA.
  User: SHICHUAN
  Date: 2018/12/4
  Time: 上午10:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <link rel="stylesheet" href="/ask/css/userScore1.css">
    <script type="text/javascript" src="/ask/js/jquery-1.4.2.js"></script>
</head>
<body>
<button class="scoreUser">导出成绩</button>
<div class="left">
    <c:forEach items="${userArray}" var="user">
        <div class="user" data="${user.id}" name="${user.name}">
            <div class="name">${user.name}</div>
            <div class="details">电话:&nbsp;${user.tell}&nbsp;&nbsp;</div>
            <div class="details">公司:&nbsp;${user.company}&nbsp;&nbsp;职务:&nbsp;${user.post}</div>
            <div class="details">分数:&nbsp;${user.score}</div>
            <input type="checkbox" data={Uid:${user.id},Uscord:${user.score}} class="chick">
        </div>
    </c:forEach>
</div>


<div class="removeSelf" style="background-color: rgb(45,45,45);
border-radius: 5px;box-shadow: 0 0 50px rgba(0,0,0,0.2);
width: 400px;height: 200px;position: fixed;margin: auto;top: 0;
bottom: 0;right: 0;left: 0;color: white;text-align: center;line-height: 200px;
z-index: 999;border: 1px solid rgb(245, 150, 40)">请求失败...
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

    var scoreArry = [];
    $('.scoreUser').click(function () {
        scoreArry = [];
        var chicks = $(".left .chick");
        for (var i = 0;i<chicks.length;i++) {
            var chickbox = chicks[i];
            if (chickbox.checked == true){
                scoreArry.push($(chickbox).attr("data"))
            }
        }
        if(scoreArry.length<=0){
            alert("你还没有选择要导出那些人的成绩！")
            return;
        }
        console.log(scoreArry)
        // $.ajax({
        //     contentType: "application/x-www-form-urlencoded; charset=utf-8",
        //     type: "post",
        //     url: "/ask/scoreAll.do",
        //     traditional: true,
        //     data: {
        //         score: JSON.stringify(scoreArry)
        //     },
        //     error: function () {
        //         $(".removeSelf").css({display: 'block'});
        //     },
        //     dataType: {'fileName': "testAjaxDownload.xlsx"},
        //     success: function (data, status, xhr) {
        //         console.log("导出excle文件成功");
        //         downloadFileByForm();
        //     }
        //
        // })
        downloadFileByForm(scoreArry);
    })

    function downloadFileByForm(scoreArry) {
        var url = "/ask/download.do";
        var form = $("<form></form>").attr("action", url).attr("method", "post");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "score").attr("value", JSON.stringify(scoreArry)));
        form.appendTo('body').submit().remove();
    }


    function yy() {
        $(".removeSelf").css({display: "none"});
    }

    yy();

    var neamElem = null;
    $(".left").click(function (even) {


        var elem = $(even.target);
        if (elem.attr("data") == undefined) {
            elem = elem.parent(".user")
        }else if(elem.attr('type') == 'checkbox'){
            return;
        }
        neamElem = elem;
        console.log(elem.attr("data"));
        console.log('elem'+elem.attr('type'));

        $.ajax({
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            type: 'post',
            url: "/ask/queryScore.do",
            data: {
                uerID: elem.attr("data"),
            },
            error: (function () {
                $(".removeSelf").css({display: 'block'});
            }),
            dataType: 'json',
            success: (function (json) {
                console.log(json);
                showAnswerResult(json);
                /**
                 * 透明
                 */
                $("#canvas").css({display: 'block'}).css({opacity: '0.0'});
                $(".canvas").css({display: 'block'}).css({opacity: '0.0'})
                $(".resultScore").css({display: 'block'}).css({opacity: '0.0'});

                /**
                 * 不透明
                 */
                $("#canvas").animate({opacity: '1.0'});
                $(".canvas").animate({opacity: '1.0'});
                $(".resultScore").animate({opacity: '1.0'});


            })

        })
    })


    $(".removeSelf").click(function (even) {
        $(even.target).css({display: 'none'});
        console.log($(this.target));
    })

    /**
     * 显示用户的成绩图表
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
        var width = 500, height = 500;
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


        allScore = (allScore1 / allScore2) * 100;
        /**
         * 分数评级
         */

        var useName = neamElem.attr("name");

        if (allScore == 100) {
            $(".mark").html("A<sup>+</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
        } else if (allScore < 100 && allScore >= 95) {
            $(".mark").html("A<sup>-</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
        } else if (allScore < 95 && allScore >= 90) {
            $(".mark").html("B<sup>+</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
        } else if (allScore < 90 && allScore >= 85) {
            $(".mark").html("B<sup>-</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
        } else if (allScore < 85 && allScore >= 80) {
            $(".mark").html("C<sup>+</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
        } else if (allScore < 80 && allScore >= 75) {
            $(".mark").html("C<sup>-</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
        } else if (allScore < 75 && allScore >= 70) {
            $(".mark").html("D<sup>+</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
        } else if (allScore < 70 && allScore >= 65) {
            $(".mark").html("D<sup>-</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
        } else if (allScore < 65 && allScore >= 60) {
            $(".mark").html("E<sup>+</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
        } else if (isNaN(allScore)) {
            $(".mark").html("没有成绩");
            $(".scoreName").html("");
        } else {
            $(".mark").html("E<sup>-</sup>");
            $(".scoreName").html(useName + "&nbsp;" + allScore.toFixed(1) + "分");
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
