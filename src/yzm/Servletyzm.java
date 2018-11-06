package yzm;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Servlet-yzm")
public class Servletyzm extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        System.out.println("dfdasdfas");




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        doPost(request,response);
    }
}
