package main;



import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static main.QuestionBack.querReleaseLast;


@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("+++++++mainServlet++++++");
        String path = request.getServletPath();
        System.out.println("path =" + path);

        if (Objects.equals(path, "/confirm")) {//用户注册
            UserManger.getPar(request, response);
        }
        else if (Objects.equals(path, "/query")) {//用户登录
            UserManger.querTestResult(request, response);
        }
        else if(Objects.equals(path,"/adm")){//管理人员登录
            Administer.landAdmin(request,response);
        }
        else if(Objects.equals(path,"/ques")){//添加题目
            Questions.getPar(request,response);
        }
        else if(Objects.equals(path,"/queryItem")){//查询一页的题目
            Questions.querItems(request,response);
        }
        else if(Objects.equals(path,"/preview")){//预览试卷
            Preview.getPar(request,response);
        }
        else if(Objects.equals(path,"/delectItem")){//删除题目数组
            DeleteItem.DeleteItem(request,response);
        }
        else if(Objects.equals(path,"/QuestionBack")){//生层试卷
            QuestionBack.getPar(request,response);
        }
        else if(Objects.equals(path,"/showTables")){//试卷的名称目录
            QuestionBack.getTables(response);
        }
        else if(Objects.equals(path,"/showQuestions")){//显示一张试卷的题目
            QuestionBack.getQuestions(request,response);
        }
        else if(Objects.equals(path,"/deleteTable")){//删除试卷数组
            QuestionBack.deleteTableGetPar(request,response);
        }
        else if(Objects.equals(path,"/releases")){//发布试卷
            QuestionBack.releaseQuestions(request,response);
        }
        else if(Objects.equals(path,"/gtreleases")){//获取发布的试卷题目
            QuestionBack.gtreleasesQuestions(request,response);
        }
        else if(Objects.equals(path,"/startTest.do")){//获取一张试卷
            QuestionBack.getQuestions(request,response,QuestionBack.querReleaseLast());
        }
        else if(Objects.equals(path,"/wellDowne.do")){//提交试卷
            Score.getUserAnswer(request,response);
        }else if(Objects.equals(path,"/queryScore.do")){//查询成绩
            JSONArray jsonArray =  Score.getTestRecord((String) request.getParameter("uerID"));
            QuestionBack.responesToCline(response,jsonArray.toString());
        }



    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }




}
