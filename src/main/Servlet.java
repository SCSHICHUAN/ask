package main;

import Models.User;
import jdbc.JDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;


@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ok = "++++Servlet++++";
        getPar(request);
        response.getOutputStream().write(ok.getBytes("utf-8"));

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void getPar(HttpServletRequest request) {

        String name = (String) request.getParameter("name");
        String tell = (String) request.getParameter("tell");
        String yzm = (String) request.getParameter("yzm");
        String company = (String) request.getParameter("company");
        String post = (String) request.getParameter("post");


        User user = new User(name, tell, yzm, company, post);

        System.out.println(user.toString());

        if(AddUser(user)){
            System.out.println("增加user成功");
        }else {
            System.out.println("增加user失败");
        }

    }

    public boolean AddUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = JDBC.GetConnection();
            String sql = "insert user(id,name,tell,yzm,company,post) values(null,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, user.name);
            preparedStatement.setObject(2, user.tell);
            preparedStatement.setObject(3, user.yzm);
            preparedStatement.setObject(4, user.conpany);
            preparedStatement.setObject(5, user.post);

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement);
        }

        return false;
    }


}
