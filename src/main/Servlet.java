package main;

import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ok = "++++Servlet++++";
        getPar(request);

       response.getOutputStream().write(ok.getBytes("utf-8"));

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       doPost(request,response);
    }

    private void getPar(HttpServletRequest request){

        String name = (String) request.getParameter("name");
        String tell = (String) request.getParameter("tell");
        String yzm = (String) request.getParameter("yzm");
        String company = (String) request.getParameter("company");
        String post = (String) request.getParameter("post");


        User user = new User(name,tell,yzm,company,post);

        System.out.println(user.toString());


    }








}
