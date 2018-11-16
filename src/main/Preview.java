package main;

import Models.TestIteam;
import jdbc.JDBC;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class Preview {
    public static void getPar(HttpServletRequest request, HttpServletResponse response) {
        String title = (String)request.getParameter("title");
        String ids = (String)request.getParameter("ids");
        JSONArray array = new JSONArray(ids);

        /**
         * 通过id获取到题目对象，然后转换为JS对象
         * 传递给前端
         */
        JSONArray jsonArray = new JSONArray();

         for(int i = 0;i<array.length();i++){

             String id = array.get(i).toString();

             TestIteam testIteam = getTestItemForId(id);

             JSONObject jsonObject = new JSONObject();

             jsonObject.put("idQus", testIteam.id);
             jsonObject.put("category", testIteam.category);
             jsonObject.put("title", testIteam.title);
             jsonObject.put("A", testIteam.A);
             jsonObject.put("B", testIteam.B);
             jsonObject.put("C", testIteam.C);
             jsonObject.put("D", testIteam.D);
             jsonObject.put("answer", testIteam.answer);

             jsonArray.put(jsonObject);

         }

        try {
            response.getOutputStream().write(jsonArray.toString().getBytes("utf8"));
        }catch (IOException e){
            e.printStackTrace();
        }


   }


    public static TestIteam getTestItemForId(String id){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select * from questions where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,id);
            resultSet =  preparedStatement.executeQuery();

            while (resultSet.next()){

                    TestIteam testIteam = new TestIteam(
                            resultSet.getString("id"),
                            resultSet.getString("category"),
                            resultSet.getString("title"),
                            resultSet.getString("A"),
                            resultSet.getString("B"),
                            resultSet.getString("C"),
                            resultSet.getString("D"),
                            resultSet.getString("answer"));
                    testIteam.showString();
                    return testIteam;

            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBC.close(connection,preparedStatement,resultSet);
        }

        return null;
    }




}
