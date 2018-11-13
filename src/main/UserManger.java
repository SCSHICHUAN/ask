package main;

import Models.User;
import jdbc.JDBC;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class UserManger {

    public  static void getPar(HttpServletRequest request, HttpServletResponse response) {

        String name = (String) request.getParameter("name");
        String tell = (String) request.getParameter("tell");
        String yzm = (String) request.getParameter("yzm");
        String company = (String) request.getParameter("company");
        String post = (String) request.getParameter("post");


        User user = new User(name, tell, yzm, company, post);

        System.out.println(user.toString());


        String phone = (String) request.getSession().getAttribute("phone");
        /**
         * 获取系统生层到验证码
         */
        String password = (String) request.getSession().getAttribute("password");

        System.out.println("phone:" + phone);
        System.out.println("password:" + password);

        System.out.println("tell:" + tell);
        System.out.println("yzm:" + yzm);

        String responseStr = "无信息";
        /**
         * Objects.equals(password, yzm)
         * 判断用户输入的验证码
         */
        if (Objects.equals(phone, tell) && Objects.equals(password, yzm)) {

            if (AddUser(user)) {

                responseStr = "0";
                System.out.println("增加user成功");

            } else {

                System.out.println("增加user失败");
                List<User> users = query(phone);
                User user1 = users.get(0);
                responseStr = "你已有信息,密码:&nbsp;&nbsp;" + user1.yzm;

            }

        } else {
            responseStr = "2";
            System.out.println("手机号或验证码错误.....");
        }

        try {
            response.getOutputStream().write(responseStr.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static boolean AddUser(User user) {
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
            return false;
        } finally {
            JDBC.close(connection, preparedStatement);
        }

    }


    public static List<User> query(String phoneNumber) {

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


    public  static void querTestResult(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("+++++++querTestResult++++++");


        String phone = request.getParameter("phoneQuery1");
        String password = request.getParameter("passwordQuery1");

        List<User> users = query(phone);
        User user = users.get(0);

        System.out.println("user=" + user);


        try {
            if (Objects.equals(phone, user.tell) && Objects.equals(password, user.yzm)) {

                Map<String, String> userMap = new HashMap<>();
                userMap.put("name", user.name);
                userMap.put("tell", user.tell);
                userMap.put("yzm", user.yzm);
                userMap.put("conpany", user.conpany);
                userMap.put("post", user.post);

                JSONObject json = new JSONObject(userMap);
                response.getOutputStream().write(json.toString().getBytes("utf8"));


            } else {
                response.getOutputStream().write("fales".getBytes("utf8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}