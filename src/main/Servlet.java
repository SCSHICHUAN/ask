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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("+++++++Servlet++++++");
        getPar(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void getPar(HttpServletRequest request, HttpServletResponse response) {

        String name = (String) request.getParameter("name");
        String tell = (String) request.getParameter("tell");
        String yzm = (String) request.getParameter("yzm");
        String company = (String) request.getParameter("company");
        String post = (String) request.getParameter("post");


        User user = new User(name, tell, yzm, company, post);

        System.out.println(user.toString());


        String phone = (String) request.getSession().getAttribute("phone");
        String password = (String) request.getSession().getAttribute("password");

        System.out.println("phone:" + phone);
        System.out.println("password:" + yzm);

        String responseStr = "";
        if (Objects.equals(phone, tell) && Objects.equals(password, yzm)) {
            if (AddUser(user)) {
                responseStr = "0";
                System.out.println("增加user成功");
            } else {

                System.out.println("增加user失败");
                List<User> users = query(phone);
                User user1 = users.get(0);
                responseStr = "你已有信息,密码:&nbsp;"+user1.yzm;

            }
        } else {
            responseStr = "2";
            System.out.println("手机号或者验证码错误.....");
        }

        try {
            response.getOutputStream().write(responseStr.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
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


    public List<User> query(String phoneNumber) {

        List<User> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            connection = JDBC.GetConnection();
            String sql = "select * from user   where tell  = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, phoneNumber);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String tell = resultSet.getString("tell");
                String yzm = resultSet.getString("yzm");
                String company = resultSet.getString("company");
                String post = resultSet.getString("post");
                User user = new User(name, tell, yzm, company, post);
                users.add(user);

                System.out.println("+++query+++tell:" + tell);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement, resultSet);
        }
        return users;
    }


}
