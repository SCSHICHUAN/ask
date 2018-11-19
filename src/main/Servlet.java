package main;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("+++++++mainServlet++++++");
        String path = request.getServletPath();
        System.out.println("path =" + path);

        if (Objects.equals(path, "/confirm")) {
            UserManger.getPar(request, response);
        } else if (Objects.equals(path, "/query")) {
            UserManger.querTestResult(request, response);
        }else if(Objects.equals(path,"/adm")){
            Administer.landAdmin(request,response);
        }else if(Objects.equals(path,"/ques")){
            Questions.getPar(request,response);
        }else if(Objects.equals(path,"/queryItem")){
            Questions.querItems(request,response);
        }else if(Objects.equals(path,"/preview")){
            Preview.getPar(request,response);
        }else if(Objects.equals(path,"/delectItem")){
            DeleteItem.DeleteItem(request,response);
        }



    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }




}
