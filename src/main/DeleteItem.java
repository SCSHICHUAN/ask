package main;

import Models.TestIteam;
import Models.User;
import jdbc.JDBC;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteItem {

    public static void DeleteItem(HttpServletRequest request, HttpServletResponse response) {
        String ids = (String)request.getParameter("ids");
        JSONArray array = new JSONArray(ids);

        /**
         * 通过id获取到题目对象，然后转换为JS对象
         * 传递给前端
         */
        JSONArray jsonArray = new JSONArray();

        int j = 0;
        for(int i = 0;i<array.length();i++){
            String id = array.get(i).toString();
            if (delect(id)){
                j++;
            }
        }

        try {
            response.getOutputStream().write(String.valueOf(j).getBytes("utf8"));
        }catch (IOException e){
            e.printStackTrace();
        }


    }


    public static boolean delect(String id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = JDBC.GetConnection();
            String sql = "delete from questions where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);
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





}
