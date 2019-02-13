package main;


import Models.TestIteam;
import jdbc.JDBC;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


public class QuestionBack {



    /**
     * 生层试卷
     *
     * @param request
     * @param response
     */
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

    /**
     * 响应给前端，试卷的名称目录
     *
     * @param response
     */
    public static void getTables(HttpServletResponse response) {
        JSONArray jsonArray = new JSONArray();

        for (String table : showTables()) {

            if (!(Objects.equals("questions", table) || Objects.equals("user", table))) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("table", table);
                jsonArray.put(jsonObject);
            }
        }
        responesToCline(response, jsonArray.toString());


    }

    /**
     * 删除选中的所有试卷
     */
    public static void deleteTableGetPar(HttpServletRequest request, HttpServletResponse response) {

        String ids = (String) request.getParameter("ids");

        JSONArray array = new JSONArray(ids);
        var j = 0;
        for (int i = 0; i < array.length(); i++) {
            String table = array.get(i).toString();
            if (deleteTable(table)) {

                if (Objects.equals(querReleaseLast(), table)) {
                    delReleaseLast();
                }


                j++;
            }
        }

        responesToCline(response, String.valueOf(j));
    }


    /**
     * 获取试卷，响应给前端
     *
     * @param request
     * @param respons
     */
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

    /**
     * 获取试卷，响应给前端,答题
     *
     * @param request
     * @param respons
     */
    public static void getQuestions(HttpServletRequest request, HttpServletResponse respons, String tableName) {

        String table = tableName;

        if(tableName.equals("随机试卷")){
            System.out.println("-----------随机试卷---------");

            List<String> tableIDs = getRandomQues();
            List<TestIteam> testIteams = new ArrayList<>();
            for (String id : tableIDs) {
                System.out.println("id=" + id);
                testIteams.add(Preview.getTestItemForId(id));
            }
            JSONArray jsonArray = Questions.ListArrayToJSONArray(testIteams);
            /***
             * 答题时间
             */
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("time",GetAskTime());
            System.out.println("time:"+GetAskTime());
            jsonArray.put(jsonObject);

            responesToCline(respons, jsonArray.toString());
            return;
        }

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
            /***
             * 答题时间
             */
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("time",GetAskTime());
            jsonArray.put(jsonObject);

            responesToCline(respons, jsonArray.toString());
        }


    }


    /**
     * 发布试卷，并且删除老的发布，响应给前端
     *
     * @param request
     * @param respons
     */
    public static void releaseQuestions(HttpServletRequest request, HttpServletResponse respons) {

        String table = (String) request.getParameter("table");

        if (Objects.equals(table, "")) {
            responesToCline(respons, "没有试卷".toString());
        } else {

            String lastTable = querReleaseLast();
            if (!(Objects.equals(lastTable, "") | (Objects.equals(lastTable, null)))) {
                delReleaseLast();
            }
            addRelease(table);

            responesToCline(respons, table.toString());
        }
    }

    /**
     * 获取发布的试卷，响应给前端
     *
     * @param request
     * @param respons
     */
    public static void gtreleasesQuestions(HttpServletRequest request, HttpServletResponse respons) {


        String lastTable = querReleaseLast();
        if (!(Objects.equals(lastTable, "") | (Objects.equals(lastTable, null)))) {
            responesToCline(respons, lastTable.toString());
        } else {
            responesToCline(respons, "无".toString());
        }


    }


    /**
     * 发布试卷
     *
     * @param table
     * @return
     */
    public static boolean addRelease(String table) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;


        try {
            connection = JDBC.GetConnection();
            String sql = "insert into releases values(null ,?) ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, table);
            int row = preparedStatement.executeUpdate();
            if (row > 0) {
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

    /**
     * 查询已经发布的试卷
     *
     * @return
     */
    public static String querReleaseLast() {
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

    /**
     * 删除已经发布的试卷
     *
     * @return
     */
    public static boolean delReleaseLast() {

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


    /**
     * 获取table中的题目id
     *
     * @param table
     * @return
     */
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


    public static void getCategorys(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("----------------getCategorys-----------");
        Map<String, List<String>> map = getAllltest();
        Map<String, String> map1 = new HashMap<>();

        for (String key : map.keySet()) {
            map1.put(key, String.valueOf(map.get(key).size()));
            System.out.println(key);
        }

        request.setAttribute("keys", map1);

        try {
            try {
                request.getRequestDispatcher("/views/autoSelect.jsp").forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void autoCategorys(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("----------------autoCategorys-----------");


        /**
         * 清空老的随机设置
         */
        ClearTableContent("randomKind");

        Map<String, List<String>> staticMap = new HashMap<>();
        staticMap = getAllltest();

        Map<String, List<String>> staticMap2 = new HashMap<>();


        List<String> resultIDs = new ArrayList<>();


        /**
         * 选择0到题目
         */
        for (String key : staticMap.keySet()) {
            String number = (String) request.getParameter(key);
            if (Integer.valueOf(number) !=0){
                staticMap2.put(key,staticMap.get(key));
            }
        }


        /**
         * 随机的抽取题目
         */
        for (String key : staticMap2.keySet()) {

            String number = (String) request.getParameter(key);//
            System.out.println("选择的题目数量："+number);
            /**
             * 把随机获取题目和类别存入数据库
             */
            AutoKindAndQuesNum(key,Integer.parseInt(number));

            List<String> localIDs = staticMap.get(key);
            List<String> localIDs2 = new ArrayList<>();

            if (localIDs.size() == 0){
                staticMap.remove(key,staticMap.get(key));
                continue;
            }

            Random random = new Random();
            Integer item = Integer.valueOf(number);


            /**
             * 1.循环抽取题目，
             */
            while (true) {

                /**
                 * 2.从一个集合随机抽取元素
                 */
                String quesID = localIDs.get(random.nextInt(localIDs.size()));
                /**
                 * 3.题目抽取出来，就从集合中删除
                 */
                localIDs.remove(quesID);
                localIDs2.add(quesID);

                System.out.println(quesID);

                /**
                 * 4.当抽取到指定数量break、
                 */
                if (localIDs2.size() == item) {
                    resultIDs.addAll(localIDs2);
                    System.out.println(key);
                    System.out.println("---------");
                    break;

                }
            }


        }

        /**
         * 添加到数据库中
         */
        deleteTable("随机试卷");
        createTableForQuestios("随机试卷");
        for (int i = 0; i < resultIDs.size(); i++) {
            addQuertion("随机试卷", resultIDs.get(i));
        }


        try {
            try {
                request.getRequestDispatcher("/views/admin1.jsp?auto=auto").forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 按照类别把题目分类
     * @return
     */
    public static Map<String, List<String>> getAllltest() {
        Map<String, List<String>> map = new HashMap<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            statement = connection.createStatement();
            String sql = "select * from questions";
            resultSet = statement.executeQuery(sql);


            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String category = resultSet.getString("category");

                boolean flag = false;
                for (String key : map.keySet()) {
                    if (Objects.equals(key, category)) {
                        map.get(key).add(String.valueOf(id));
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    List<String> list = new ArrayList<>();
                    list.add(String.valueOf(id));
                    map.put(category, list);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement, resultSet);
        }
        return map;
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

    /**
     * 显示试卷
     *
     * @return
     */
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
     * 动态的创建table，生层试卷
     *
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
     * 把table的名字级联添加到tableNames的表中保存下来，tableNames表是保存试卷名称目录
     *
     * @param table
     * @return
     */
    public static boolean addTableName(String table) {
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
     *
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
     *
     * @param table
     * @return
     */
    public static boolean deleteTableName(String table) {

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


    /**
     * 试卷中添加题目的id
     *
     * @param table
     * @param id
     * @return
     */
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



    public static  List<String> getRandomQues(){


        /**
         * 要随机的类别题目
         */
        Map<String,Integer> map =  GetAutoKindAndQuesNum();


        Map<String, List<String>> staticMap = new HashMap<>();
        staticMap = getAllltest();
        List<String> resultIDs = new ArrayList<>();



        /**
         * 随机的抽取题目
         */
        for (String key : map.keySet()) {

            Integer number = map.get(key);
            System.out.println("选择的题目数量："+number);

            List<String> localIDs = staticMap.get(key);
            List<String> localIDs2 = new ArrayList<>();

            if (localIDs.size() == 0){
                staticMap.remove(key,staticMap.get(key));
                continue;
            }

            Random random = new Random();
            Integer item = number;


            /**
             * 1.循环抽取题目，
             */
            while (true) {

                /**
                 * 2.从一个集合随机抽取元素
                 */
                String quesID = localIDs.get(random.nextInt(localIDs.size()));
                /**
                 * 3.题目抽取出来，就从集合中删除
                 */
                localIDs.remove(quesID);
                localIDs2.add(quesID);

                System.out.println(quesID);

                /**
                 * 4.当抽取到指定数量break、
                 */
                if (localIDs2.size() == item) {
                    resultIDs.addAll(localIDs2);
                    System.out.println(key);
                    System.out.println("----------");
                    break;

                }
            }


        }
        System.out.println("随机题目的ID："+resultIDs);

        return resultIDs;


    }

    /**
     * 保存随机设置
     * @param kindName
     * @param quseNum
     * @return
     */

    public static boolean AutoKindAndQuesNum(String kindName,int quseNum){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "insert into randomKind values (null ,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,quseNum);
            preparedStatement.setObject(2,kindName);

            int row = preparedStatement.executeUpdate();
            if(row>0){
                return true;
            }else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection,preparedStatement);
        }

        return false;
    }

    /***
     * 清空数据表
     * @return
     */
    public static boolean ClearTableContent(String tableName){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = JDBC.GetConnection();
            statement = connection.createStatement();
            String sql = "truncate table " + tableName;
           int row =  statement.executeUpdate(sql);
           if(row>0){
               return true;
           }else {
               return false;
           }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection,statement);
        }
        return false;
    }
    public static Map<String,Integer> GetAutoKindAndQuesNum(){

        Map<String,Integer> map = new HashMap<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select *  from randomKind";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String key = resultSet.getString("kindName");
                Integer num = resultSet.getInt("qNum");
                map.put(key,num);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection,statement,resultSet);
        }
        System.out.println("随机设置："+map);
        return map;
    }

    /**
     * 设置答题时间
     * @param request
     * @param response
     * @return
     */
    public static boolean SetTime(HttpServletRequest request,HttpServletResponse response){
        System.out.println("---------SetTime-------");
         ClearTableContent("timeAsk");
        Integer time = Integer.parseInt(request.getParameter("time"));


        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "insert into timeAsk values(?) ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,time);
           int row =  preparedStatement.executeUpdate();
           if (row>0){
               return true;
           }else {
               return false;
           }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection,preparedStatement);
        }

        return false;
    }

    /***
     * 回显时间
     * @param request
     * @param response
     */
    public static void GetTime(HttpServletRequest request,HttpServletResponse response){
        System.out.println("--------GetTime----------");
        try {
            response.getOutputStream().write(String.valueOf(GetAskTime()).getBytes("utf8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /***
     * 获取答题时间
     * @return
     */
    public static Integer GetAskTime(){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBC.GetConnection();
            String sql = "select * from timeAsk";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Integer time = resultSet.getInt("time");
                return time;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection,statement,resultSet);
        }

        return -1;
    }


}
