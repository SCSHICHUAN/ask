package main;


import Models.User;
import jdbc.JDBC;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;



public class UserManger {

    public static void getPar(HttpServletRequest request, HttpServletResponse response) {

        String name = (String) request.getParameter("name");
        String tell = (String) request.getParameter("tell");
        String yzm = (String) request.getParameter("yzm");
        String company = (String) request.getParameter("company");
        String post = (String) request.getParameter("post");


        User user = new User(null, name, tell, yzm, company, post,"0");

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
                User user1 = query(phone);

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


    public static void  allUsers(HttpServletRequest request, HttpServletResponse response){

//        int currentPage = 1;
//        int pageContent = 20;
//        int totalRows = usersCoun();
//        /**
//         * 判断有几页，如果不能整除就加一
//         */
//        int pages = totalRows % pageContent == 0 ? totalRows / pageContent
//                : totalRows / pageContent + 1;
//        if (currentPage > pages) {
//            currentPage = pages;
//        }else if(currentPage <= 0){
//            currentPage = 1;
//        }
//
//
//        int start = (currentPage - 1) * pageContent;
//        int end = currentPage * pageContent;
//
//        List<User> users = usersPages(start, end);
//        JSONArray jsonArray = ListArrayToJSONArray(users);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("pages",pages);
//        jsonArray.put(jsonObject);



        request.setAttribute("userArray",userArrayToMapArray(usersPages(0, 100)));
        try {
            request.getRequestDispatcher("/views/userScore.jsp?currentPage=1").forward(request,response);
        }catch (Exception e){
            e.printStackTrace();
        }



    }


    /**
     * 添加用户
     * @param user
     * @return
     */
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

    /**
     * 手机号查询用户
     *
     * @param phoneNumber
     * @return
     */
    public static User query(String phoneNumber) {


        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            connection = JDBC.GetConnection();
            String sql = "select * from user U, userScore S where S.userID = U.id and U.tell = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, phoneNumber);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("id"));
                String name = resultSet.getString("name");
                String tell = resultSet.getString("tell");
                String yzm = resultSet.getString("yzm");
                String company = resultSet.getString("company");
                String post = resultSet.getString("post");
                String score = resultSet.getString("scoreA1");

                System.out.println("+++query+++tell:" + tell);

                return new User(id, name, tell, yzm, company, post,score);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement, resultSet);
        }
        return null;
    }


    /**
     * 用户登录
     *
     * @param request
     * @param response
     */
    public static void querTestResult(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("+++++++querTestResult++++++");


        String phone = request.getParameter("phoneQuery1");
        String password = request.getParameter("passwordQuery1");

        User user = query(phone);
        if (user == null) {
            JSONObject jsonObject = new JSONObject("{flag:fales}");
            QuestionBack.responesToCline(response, jsonObject.toString());
            return;
        }


        System.out.println("user=" + user);


        try {
            if (Objects.equals(phone, user.tell) && Objects.equals(password, user.yzm)) {

                Map<String, String> userMap = new HashMap<>();
                userMap.put("id", user.id);
                userMap.put("name", user.name);
                userMap.put("tell", user.tell);
                userMap.put("yzm", user.yzm);
                userMap.put("conpany", user.conpany);
                userMap.put("post", user.post);

                JSONObject json = new JSONObject(userMap);
                response.getOutputStream().write(json.toString().getBytes("utf8"));


                request.getSession().setAttribute("USER_SUCCESS_LOGIN", "true");


            } else {
                JSONObject jsonObject = new JSONObject("{flag:fales}");
                QuestionBack.responesToCline(response, jsonObject.toString());
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 根据当前页返回users
     * @param start
     * @param end
     * @return
     */
    public static List<User> usersPages(int start, int end) {


        System.out.println(start+","+end);

        List<User> users = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
//            String sql = "select * from user order by id desc limit  ?,?";
//            String sql = "select * from user order by id desc";
            String sql = "select * from user U, userScore S where S.userID = U.id  order by U.id desc";
            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setObject(1, start);
//            preparedStatement.setObject(2, end-start);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("tell"),
                        resultSet.getString("yzm"),
                        resultSet.getString("company"),
                        resultSet.getString("post"),
                        resultSet.getString("scoreA1"));
                System.out.println(user.toString());
                users.add(user);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement, resultSet);
        }

       return users;
    }



    /**
     * 获取user的总数
     * @return
     */
    private static int usersCoun() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select count(*) from user";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            int total = 0;
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }

            System.out.println("total row = " + total);
            return total;


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement, resultSet);
        }


        return -1;
    }


    public static JSONArray ListArrayToJSONArray(List<User> list) {

        JSONArray jsonArray = new JSONArray();

        for (User user : list) {
            /**
             * 把 java-Object 转换为 json-Object
             */
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", user.id);
            jsonObject.put("name", user.name);
            jsonObject.put("tell", user.tell);
            jsonObject.put("yzm", user.yzm);
            jsonObject.put("company", user.conpany);
            jsonObject.put("post", user.post);

            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }


    public static List<Map<String,String>> userArrayToMapArray(List<User> list) {

        List<Map<String,String>> list1 = new ArrayList<>();

        for (User user : list) {
            /**
             * 把 java-Object 转换为 json-Object
             */
            Map<String,String> map = new HashMap();
            map.put("id", user.id);
            map.put("name", user.name);
            map.put("tell", user.tell);
            map.put("yzm", user.yzm);
            map.put("company", user.conpany);
            map.put("post", user.post);
            map.put("score", user.score);

            list1.add(map);
        }

        return list1;
    }


}
