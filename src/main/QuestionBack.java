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


        String title = (String) request.getParameter("title");
        String ids = (String) request.getParameter("ids");

        for (String table : showTables()) {
            if (Objects.equals(table, title)) {
                flag = "试卷名称已经存在.....";
                responesToCline(response, flag);
                return;
            }
        }


        System.out.println("title =" + title);


        JSONArray array = new JSONArray(ids);


        if (Objects.equals("", title) || title == null) {
            flag = "问卷名字为空....";
            responesToCline(response, flag);
            System.out.println(flag);
            return;
        } else {
            if (createTableForQuestios(title)) {
                flag = "问卷创建成功....";
                System.out.println(flag);
            } else {
                flag = "创建问卷失败....";
                responesToCline(response, flag);
                System.out.println(flag);
                return;
            }

        }


        if (array.length() <= 0) {
            flag = "没有添加题目到试卷....";
            responesToCline(response, flag);
            System.out.println(flag);
            return;
        }

        int j = 0;
        for (int i = 0; i < array.length(); i++) {
            j++;
            String id = array.get(i).toString();
            addQuertion(title, id);
        }

        flag = "试卷添加成功,共&nbsp" + String.valueOf(j) + "&nbsp题。";
        responesToCline(response, flag);
        System.out.println(flag);


    }

    public static void getTables(HttpServletRequest request, HttpServletResponse response) {
        JSONArray jsonArray = new JSONArray();

        for (String table : showTables()) {

            if (! (Objects.equals("questions", table) || Objects.equals("user", table))) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("table", table);
                jsonArray.put(jsonObject);
            }
        }
        responesToCline(response, jsonArray.toString());


    }

    public static void deleteTableGetPar(HttpServletRequest request, HttpServletResponse response) {

        String ids = (String) request.getParameter("ids");

        JSONArray array = new JSONArray(ids);
        var j = 0;
        for (int i = 0; i < array.length(); i++) {
            String table = array.get(i).toString();
            if (deleteTable(table)) {

                if(Objects.equals(querReleaseLast(),table)){
                    delReleaseLast();
                }


                j++;
            }
        }

        responesToCline(response, String.valueOf(j));
    }




    public static void getQuestions(HttpServletRequest request, HttpServletResponse respons) {

        String table = (String) request.getParameter("table");

        System.out.println("table=" + table);

        if (Objects.equals("", table) || table == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("flag", "false");
            responesToCline(respons, jsonObject.toString());
            return;
        } else {
            List<String> tableIDs = getQuestionsIdForTableName(table);
            List<TestIteam> testIteams = new ArrayList<>();
            for (String id : tableIDs) {
                System.out.println("id=" + id);
                testIteams.add(Preview.getTestItemForId(id));
            }
            JSONArray jsonArray = Questions.ListArrayToJSONArray(testIteams);
            responesToCline(respons, jsonArray.toString());
        }


    }

    public static void releaseQuestions(HttpServletRequest request, HttpServletResponse respons) {

        String table = (String) request.getParameter("table");

        if(Objects.equals(table,"")){
            responesToCline(respons, "没有试卷".toString());
        }else {

            String lastTable = querReleaseLast();
            if (!(Objects.equals(lastTable, "") | (Objects.equals(lastTable, null)))) {
                delReleaseLast();
            }
            addRelease(table);

            responesToCline(respons, table.toString());
        }
    }
    public static void gtreleasesQuestions(HttpServletRequest request, HttpServletResponse respons) {


        String lastTable = querReleaseLast();
        if(!(Objects.equals(lastTable,"")|(Objects.equals(lastTable,null)))){
            responesToCline(respons,lastTable.toString());
        }else {
            responesToCline(respons,"无".toString());
        }


    }


    public  static  boolean  addRelease(String table){
        Connection connection = null;
        PreparedStatement preparedStatement = null;


        try {
            connection = JDBC.GetConnection();
            String sql = "insert into releases values(null ,?) ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,table);
            int row = preparedStatement.executeUpdate();
            if(row>0){
                return true;
            }else {
                return false;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement);
        }
        return false;

    }

    public  static  String  querReleaseLast(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select tableNam from releases";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getString("tableNam");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement, resultSet);
        }
        return null;

    }

    public  static  boolean  delReleaseLast(){

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = JDBC.GetConnection();
            String sql = "delete from releases where id > 0";
            preparedStatement = connection.prepareStatement(sql);
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


    public static List<String> getQuestionsIdForTableName(String table) {

        List<String> ids = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select * from " + table;
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                ids.add(resultSet.getString("questionID"));
                System.out.println("id=" + resultSet.getString("questionID"));
            }
            return ids;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement, resultSet);
        }

        return null;
    }


    public static void responesToCline(HttpServletResponse response, String flag) {
        try {
            response.getOutputStream().write(flag.getBytes("utf8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public static List<String> showTables() {
//
//
//        List<String> tables = new ArrayList<>();
//
//        Connection connection = null;
//        Statement statement = null;
//        ResultSet resultSet = null;
//
//
//        try {
//            connection = JDBC.GetConnection();
//            String sql = "show tables";
//
//            statement = connection.createStatement();
//            resultSet = statement.executeQuery("show tables");
//            while (resultSet.next()) {
//                //获取字段
//                String tableName = resultSet.getString("Tables_in_ask");
//                tables.add(tableName);
//                System.out.println(tableName);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            JDBC.close(connection, statement, resultSet);
//        }
//
//        return tables;
//
//    }


    public static List<String> showTables() {


        List<String> tables = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select * from tableNames order by id desc";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //获取字段
                String tableName = resultSet.getString("tableNam");
                tables.add(tableName);
                System.out.println(tableName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement, resultSet);
        }

        return tables;

    }

    /**
     * 动态的创建table
     * @param table
     * @return
     */

    public static boolean createTableForQuestios(String table) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = JDBC.GetConnection();
            String sql = "create table if not exists " + table + " (\n" +
                    "id int unsigned auto_increment key,\n" +
                    "questionID varchar(20)\n" +
                    ")ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;\n";
            preparedStatement = connection.prepareStatement(sql);

            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                addTableName(table);
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
     * 把table的名字级联添加到tableNames的表中保存下来
     * @param table
     * @return
     */
    public  static  boolean addTableName(String table){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = JDBC.GetConnection();
            String sql = "insert into tableNames values(null,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, table);

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
     * 动态的删除table
     * @param table
     * @return
     */
    public static boolean deleteTable(String table) {

        System.out.println("deleteTable=" + table);
        boolean flag = false;


        Connection connection = null;
        Statement statement = null;
        try {
            connection = JDBC.GetConnection();
            String sql = "drop table " + table;
            statement = connection.createStatement();
            int row = statement.executeUpdate(sql);
            if (row == 0) {
                flag = true;
                deleteTableName(table);
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement);
        }
        return flag;
    }
    /**
     * 级联把table的名字重tableNames的表中删除
     * @param table
     * @return
     */
    public  static  boolean deleteTableName(String table){

            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {

                connection = JDBC.GetConnection();
                String sql = "delete from tableNames where tableNam = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, table);
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




    public static boolean addQuertion(String table, String id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            connection = JDBC.GetConnection();
            String sql = "insert into " + table + " values(null,?)";
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
