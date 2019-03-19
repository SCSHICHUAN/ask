package main;


import Models.TestIteam;
import jdbc.JDBC;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
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

        if (tableName.equals("随机试卷")) {
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
            jsonObject.put("time", GetAskTime());
            System.out.println("time:" + GetAskTime());
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
            jsonObject.put("time", GetAskTime());
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
            if (Integer.valueOf(number) != 0) {
                staticMap2.put(key, staticMap.get(key));
            }
        }


        /**
         * 随机的抽取题目
         */
        for (String key : staticMap2.keySet()) {

            String number = (String) request.getParameter(key);//
            System.out.println("选择的题目数量：" + number);
            /**
             * 把随机获取题目和类别存入数据库
             */
            AutoKindAndQuesNum(key, Integer.parseInt(number));

            List<String> localIDs = staticMap.get(key);
            List<String> localIDs2 = new ArrayList<>();

            if (localIDs.size() == 0) {
                staticMap.remove(key, staticMap.get(key));
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
     *
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


    public static List<String> getRandomQues() {


        /**
         * 要随机的类别题目
         */
        Map<String, Integer> map = GetAutoKindAndQuesNum();


        Map<String, List<String>> staticMap = new HashMap<>();
        staticMap = getAllltest();
        List<String> resultIDs = new ArrayList<>();


        /**
         * 随机的抽取题目
         */
        for (String key : map.keySet()) {

            Integer number = map.get(key);
            System.out.println("选择的题目数量：" + number);

            List<String> localIDs = staticMap.get(key);
            List<String> localIDs2 = new ArrayList<>();

            if (localIDs.size() == 0) {
                staticMap.remove(key, staticMap.get(key));
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
        System.out.println("随机题目的ID：" + resultIDs);

        return resultIDs;


    }

    /**
     * 保存随机设置
     *
     * @param kindName
     * @param quseNum
     * @return
     */

    public static boolean AutoKindAndQuesNum(String kindName, int quseNum) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "insert into randomKind values (null ,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, quseNum);
            preparedStatement.setObject(2, kindName);

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

    /***
     * 清空数据表
     * @return
     */
    public static boolean ClearTableContent(String tableName) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = JDBC.GetConnection();
            statement = connection.createStatement();
            String sql = "truncate table " + tableName;
            int row = statement.executeUpdate(sql);
            if (row > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement);
        }
        return false;
    }

    public static Map<String, Integer> GetAutoKindAndQuesNum() {

        Map<String, Integer> map = new HashMap<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select *  from randomKind";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String key = resultSet.getString("kindName");
                Integer num = resultSet.getInt("qNum");
                map.put(key, num);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement, resultSet);
        }
        System.out.println("随机设置：" + map);
        return map;
    }


    /**
     * 设置答题时间
     *
     * @param request
     * @param response
     * @return
     */
    public static boolean SetTime(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("---------SetTime-------");
        ClearTableContent("timeAsk");
        Integer time = Integer.parseInt(request.getParameter("time"));


        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "insert into timeAsk values(?) ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, time);
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

    /***
     * 回显时间
     * @param request
     * @param response
     */
    public static void GetTime(HttpServletRequest request, HttpServletResponse response) {
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
    public static Integer GetAskTime() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBC.GetConnection();
            String sql = "select * from timeAsk";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Integer time = resultSet.getInt("time");
                return time;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement, resultSet);
        }

        return -1;
    }


    /**
     * 保存用户的分数
     *
     * @param request
     * @param response
     * @return
     */
    public static boolean score(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("---------score-------");
        String uID = request.getParameter("uerID");
        ;
        String score = request.getParameter("score");
        JSONObject jsonObject = new JSONObject(score);


        float score2 = 0;
        float score1 = 0;
        float allScore = 0;
        int nexStr = 1;
        String userHead = "";
        String values = "";


        for (Object key : jsonObject.keySet()) {

            System.out.println(key);
            JSONObject jsonObject1 = jsonObject.getJSONObject(key.toString());

            System.out.println(jsonObject1);
            float A = jsonObject1.getInt("score");
            float B = jsonObject1.getInt("allScore");
            score1 += A;
            allScore += B;
            nexStr++;
            userHead += "," + "str" + nexStr + ",scoreA" + nexStr + ",scoreB" + nexStr;
            values += ",'" + key.toString() + "'," + score1 + "," + allScore;


            System.out.println("score:" + score1 + "  allScore:" + allScore);
        }
        score2 = score1/allScore*100;
        DecimalFormat fnum  =   new   DecimalFormat("####0.00");
        String score22 = fnum.format(score2);
        System.out.println("score2:"+score2);
        deleteRow(uID);

        String sql = "insert into userScore(id,userID,str1,scoreA1,scoreB1" + userHead + ") values(null," + uID + ",'总分'," + score22 + ",100" + values + ")";
        System.out.println("sql:" + sql);


        Connection connection = null;
        Statement statement = null;

        try {
            connection = JDBC.GetConnection();
            statement = connection.createStatement();
            int row = statement.executeUpdate(sql);
            if (row > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement);
        }

        return false;
    }

    /**
     * 删除用户的成绩
     *
     * @param userID
     */
    private static void deleteRow(String userID) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = JDBC.GetConnection();
            statement = connection.createStatement();
            int row = statement.executeUpdate("delete from userScore where userID = " + userID);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement);
        }

    }

    /**
     * 获取用户的成绩
     */
    public static void getUsersScore(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("---------getUsersScore-------");
        String json = request.getParameter("score");
        JSONArray jsonArray = new JSONArray(json);
        getUserScoreFromDatabase(jsonArray, response);

    }

    /**
     * 从数据库查寻用户的成绩
     */
    private static void getUserScoreFromDatabase(JSONArray jsonArray, HttpServletResponse response) {


        List<List<String>> result = new ArrayList<>();
        List<String> row = new ArrayList<>();
        result.add(row);
        row.add("姓名");
        row.add("总分");
        row.add("电话");
        row.add("公司");
        row.add("职位");

        row.add("分类1");
        row.add("类别得分");
        row.add("类别总分");

        row.add("分类2");
        row.add("类别得分");
        row.add("类别总分");
        ;

        row.add("分类3");
        row.add("类别得分");
        row.add("类别总分");

        row.add("分类4");
        row.add("类别得分");
        row.add("类别总分");

        row.add("分类5");
        row.add("类别得分");
        row.add("类别总分");

        row.add("分类6");
        row.add("类别得分");
        row.add("类别总分");

        row.add("分类7");
        row.add("类别得分");
        row.add("类别总分");

        row.add("分类8");
        row.add("类别得分");
        row.add("类别总分");

        row.add("分类9");
        row.add("类别得分");
        row.add("类别总分");

        row.add("分类10");
        row.add("类别得分");
        row.add("类别总分");


        String ids = "";
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
            String uid = jsonObject.get("Uid").toString();
            ids += "U.id = " + uid + " or ";
        }
        ids = ids.substring(0, ids.length() - 3);
        System.out.println("ids:" + ids);


        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql = "select * from user U,userScore S  where U.id = S.userID and (" + ids + ")";
        try {
            connection = JDBC.GetConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {

                row = new ArrayList<>();
                result.add(row);

                String name = resultSet.getString("name");
                String tell = resultSet.getString("tell");
                String company = resultSet.getString("company");
                String post = resultSet.getString("post");

                String str1 = resultSet.getString("str1");
                float scoreA1 = resultSet.getFloat("scoreA1");
                float scoreB1 = resultSet.getFloat("scoreB1");


                row.add(name);
                row.add(String.valueOf(scoreA1));
                row.add(tell);
                row.add(company);
                row.add(post);


                String str2 = resultSet.getString("str2");
                float scoreA2 = resultSet.getFloat("scoreA2");
                float scoreB2 = resultSet.getFloat("scoreB2");
                row.add(str2);
                row.add(String.valueOf(scoreA2));
                row.add(String.valueOf(scoreB2));

                String str3 = resultSet.getString("str3");
                row.add(str3);
                float scoreA3 = resultSet.getFloat("scoreA3");
                float scoreB3 = resultSet.getFloat("scoreB3");
                row.add(String.valueOf(scoreA3));
                row.add(String.valueOf(scoreB3));

                String str4 = resultSet.getString("str4");
                float scoreA4 = resultSet.getFloat("scoreA4");
                float scoreB4 = resultSet.getFloat("scoreB4");
                row.add(str4);
                row.add(String.valueOf(scoreA4));
                row.add(String.valueOf(scoreB4));

                String str5 = resultSet.getString("str5");
                float scoreA5 = resultSet.getFloat("scoreA5");
                float scoreB5 = resultSet.getFloat("scoreB5");
                row.add(str5);
                row.add(String.valueOf(scoreA5));
                row.add(String.valueOf(scoreB5));

                String str6 = resultSet.getString("str6");
                float scoreA6 = resultSet.getFloat("scoreA6");
                float scoreB6 = resultSet.getFloat("scoreB6");
                row.add(str6);
                row.add(String.valueOf(scoreA6));
                row.add(String.valueOf(scoreB6));

                String str7 = resultSet.getString("str7");
                float scoreA7 = resultSet.getFloat("scoreA7");
                float scoreB7 = resultSet.getFloat("scoreB7");
                row.add(str7);
                row.add(String.valueOf(scoreA7));
                row.add(String.valueOf(scoreB7));

                String str8 = resultSet.getString("str8");
                float scoreA8 = resultSet.getFloat("scoreA8");
                float scoreB8 = resultSet.getFloat("scoreB8");
                row.add(str8);
                row.add(String.valueOf(scoreA8));
                row.add(String.valueOf(scoreB8));

                String str9 = resultSet.getString("str9");
                float scoreA9 = resultSet.getFloat("scoreA9");
                float scoreB9 = resultSet.getFloat("scoreB9");
                row.add(str9);
                row.add(String.valueOf(scoreA9));
                row.add(String.valueOf(scoreB9));

                String str10 = resultSet.getString("str10");
                float scoreA10 = resultSet.getFloat("scoreA10");
                float scoreB10 = resultSet.getFloat("scoreB10");
                row.add(str10);
                row.add(String.valueOf(scoreA10));
                row.add(String.valueOf(scoreB10));


                System.out.println("name:" + name + " scoreA1:" + scoreA1);

            }


            Workbook workbook = export(true, result);
            response.setHeader("Content-Disposition", "attachment;filename=export.xlsx");
            ServletOutputStream outputStream = response.getOutputStream();


            // 直接将文件输出提供下载导出
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, statement, resultSet);
        }


    }

    public static Workbook export(boolean isXlsx, List<List<String>> lists) {
        Workbook workbook;
        if (isXlsx) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
        Sheet sheet = workbook.createSheet("My Sheet");
        List<List<String>> content = lists;
        for (int i = 0; i < content.size(); i++) {
            Row row = sheet.createRow(i);
            List<String> rowData = content.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                row.createCell(j).setCellValue(rowData.get(j));
            }
        }
        return workbook;
    }

//    private static List<List<String>> getContent() {
//        List<List<String>> result = new ArrayList<>();
//        List<String> row = new ArrayList<>();
//        result.add(row);
//        row.add("序号");
//        row.add("姓名");
//        row.add("年龄");
//        row.add("时间");
//
//        row = new ArrayList<>();
//        result.add(row);
//        row.add("1");
//        row.add("路人甲");
//        row.add("18");
//        row.add("2010-01-01");
//
//        row = new ArrayList<>();
//        result.add(row);
//        row.add("2");
//        row.add("路人乙");
//        row.add("19");
//        row.add("2010-01-02");
//
//        row = new ArrayList<>();
//        result.add(row);
//        row.add("3");
//        row.add("路人丙");
//        row.add("20");
//        row.add("2010-01-03");
//        return result;
//    }


}
