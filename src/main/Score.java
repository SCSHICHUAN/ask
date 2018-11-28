package main;

import Models.TestIteam;
import Models.User;
import jdbc.JDBC;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;



public class Score {
    public static void getUserAnswer(HttpServletRequest request, HttpServletResponse respons){

        String userID = (String)request.getParameter("uerID");
        System.out.println("userID: "+userID);
        String answerArray = (String)request.getParameter("answer");
        JSONArray jsonArray = new JSONArray(answerArray);

        System.out.println("jsonArray:"+jsonArray);


        getCategoryListMap(jsonArray);
        matchAnswer(jsonArray,userID);





        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flat","true");
        QuestionBack.responesToCline(respons,jsonObject.toString());
    }

    /**
     * 将答案按类分成数组
     * @param jsonArray
     * @return 分类好的数组答案
     */
    public static  Map<String,List<TestIteam>> getCategoryListMap(JSONArray jsonArray){

        String category1 ="";
        Map<String,List<TestIteam>> map = new HashMap<>();

        for (int i = 0;i<jsonArray.length();i++){

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            System.out.println("id: "+id);

            TestIteam testIteam = queryQuesForQrestionID(id);
            String category = testIteam.category;
            category1 = category;

            boolean flag = false;

            for(String itme:map.keySet()){
                if(Objects.equals(testIteam.category,itme)){
                    flag = true;
                }
            }

            if(flag){
                List<TestIteam> testIteams = map.get(category);
                testIteams.add(testIteam);
            }else {
                List<TestIteam> testIteams = new ArrayList<>();
                testIteams.add(testIteam);
                map.put(category,testIteams);
            }

        }


        List<TestIteam> t =  map.get(category1);
        for(TestIteam testIteam:t){
            testIteam.showString();
        }

        return map;
    }

    /***
     * 通过ID查询到题目的数据
     * @param id
     * @return
     */
    public static TestIteam queryQuesForQrestionID(String id) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select * from questions where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TestIteam testIteam = new TestIteam(
                        resultSet.getString("id"),
                        resultSet.getString("category"),
                        resultSet.getString("title"),
                        resultSet.getString("A"),
                        resultSet.getString("B"),
                        resultSet.getString("C"),
                        resultSet.getString("D"),
                        resultSet.getString("answer"));
                return testIteam;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement, resultSet);
        }

      return null;
    }



    public static void matchAnswer(JSONArray jsonArray,String userID){

        delectAnswer(userID);

        for (int i = 0;i<jsonArray.length();i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            JSONObject jsonObject1 = jsonObject.getJSONObject("answer");
            String answer = jsonObject1.getString("A")
                    +jsonObject1.getString("B")
                    +jsonObject1.getString("C")
                    +jsonObject1.getString("D");

            TestIteam testIteam = queryQuesForQrestionID(id);
            String category = testIteam.category;
            String correct = testIteam.answer;



            Map<String,String> map = new HashMap<>();
            map.put("userID",userID);
            map.put("category",category);
            map.put("answer",answer);
            map.put("correct",correct);


            System.out.println(answer+","+correct);


            if(Objects.equals(answer,correct)){
                map.put("grade","1");
            }else {
                map.put("grade","0");
            }

            AddAnswer(map);
        }
    }

    /**
     * 添加答案
     * @param user
     * @return
     */
    public static boolean AddAnswer(Map<String,String> answer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "insert answer(id,userID,category,answer,correct,grade) values(null,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, answer.get("userID"));
            preparedStatement.setObject(2, answer.get("category"));
            preparedStatement.setObject(3, answer.get("answer"));
            preparedStatement.setObject(4, answer.get("correct"));
            preparedStatement.setObject(5, answer.get("grade"));

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
    public static boolean delectAnswer(String userID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = JDBC.GetConnection();
            String sql = "delete from  answer where userID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, userID);
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
