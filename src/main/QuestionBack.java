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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionBack {
    public static void getPar(HttpServletRequest request, HttpServletResponse response) {

        String flag = "";



        String title = (String)request.getParameter("title");
        String ids = (String)request.getParameter("ids");

        for (String table :showTables()){
            if(Objects.equals(table,title)){
                flag = "试卷名称已经存在.....";
                responesToCline(response,flag);
                return;
            }
        }


        System.out.println("title ="+title);


        JSONArray array = new JSONArray(ids);


        if(Objects.equals("",title) || title == null){
             flag = "问卷名字为空....";
            responesToCline(response,flag);
            System.out.println(flag);
             return;
        }else {
            if(createTableForQuestios(title)){
                flag = "问卷创建成功....";
                System.out.println(flag);
            }else {
                flag = "创建问卷失败....";
                responesToCline(response,flag);
                System.out.println(flag);
                return;
            }

        }


        if (array.length()<=0){
            flag = "没有添加题目到试卷....";
            responesToCline(response,flag);
            System.out.println(flag);
            return;
        }

        int j = 0;
        for(int i = 0;i<array.length();i++){
            j++;
            String id = array.get(i).toString();
            addQuertion(title,id);
        }

        flag = "试卷添加成功,共&nbsp"+String.valueOf(j)+"&nbsp题。";
        responesToCline(response,flag);
        System.out.println(flag);




    }

    public static void getTables(HttpServletRequest request, HttpServletResponse response) {
        JSONArray jsonArray = new JSONArray();
        for (String table : showTables()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("table",table);
            jsonArray.put(jsonObject);
        }
        responesToCline(response,jsonArray.toString());


    }

    public static void getQuestions(HttpServletRequest request,HttpServletResponse  respons){

        String table = (String)request.getParameter("table");

        System.out.println("table="+table);

        if(Objects.equals("",table) || table == null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("flag","false");
            responesToCline(respons,jsonObject.toString());
            return;
        }else {
           List<String>  tableIDs = getQuestionsIdForTableName(table);
          List<TestIteam> testIteams = new ArrayList<>();
           for (String id : tableIDs){
               System.out.println("id="+id);
               testIteams.add(Preview.getTestItemForId(id));
           }
           JSONArray jsonArray = Questions.ListArrayToJSONArray(testIteams);
           responesToCline(respons,jsonArray.toString());
        }







    }
    public static List<String> getQuestionsIdForTableName(String table){

        List<String> ids = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select * from "+table;
            statement = connection.createStatement();
            resultSet =  statement.executeQuery(sql);

            while (resultSet.next()){
                ids.add(resultSet.getString("questionID"));
                System.out.println("id="+resultSet.getString("questionID"));
            }
            return ids;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBC.close(connection,statement,resultSet);
        }

        return null;
    }



    public static void responesToCline(HttpServletResponse response,String flag){
        try {
            response.getOutputStream().write(flag.getBytes("utf8"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public static List<String> showTables(){


        List<String> tables = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;


        try {
            connection = JDBC.GetConnection();
            String sql = "show tables";

            statement = connection.createStatement();
            resultSet = statement.executeQuery("show tables");
            while(resultSet.next()) {
                //获取字段
                String tableName = resultSet.getString("Tables_in_ask");
                tables.add(tableName);
                System.out.println(tableName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement, resultSet);
        }

        return  tables;

    }


    public static boolean createTableForQuestios(String table){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = JDBC.GetConnection();
            String sql = "create table if not exists "+table+" (\n" +
                    "id int unsigned auto_increment key,\n" +
                    "questionID varchar(20)\n" +
                    ")ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;\n";
            preparedStatement = connection.prepareStatement(sql);

            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
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

    public static boolean addQuertion(String table,String  id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = JDBC.GetConnection();
            String sql = "insert into "+table+" values(null,?)";
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
