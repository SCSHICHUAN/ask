package main;

import Models.TestIteam;
import Models.User;
import jdbc.JDBC;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class Questions {



   public static int oldPge;

    /**
     * 从前端获取题目
     * @param request
     * @param response
     */
    public static void getPar(HttpServletRequest request, HttpServletResponse response) {



        String category = (String) request.getParameter("category");
        String title = (String) request.getParameter("title");
        String A = (String) request.getParameter("tA");
        String B = (String) request.getParameter("tB");
        String C = (String) request.getParameter("tC");
        String D = (String) request.getParameter("tD");
        String answer = (String) request.getParameter("answer");


        TestIteam testIteam = new TestIteam(null,category, title, A, B, C, D, answer);
        testIteam.showString();


        try {

            if (addTestItem(testIteam)) {
                response.getOutputStream().write("true".getBytes("utf8"));
            } else {
                response.getOutputStream().write("false".getBytes("utf8"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**管理员导入excle文件
     * @param request
     * @param response
     */
    public static  void uplodExcleFile(HttpServletRequest request, HttpServletResponse response){
        System.out.println("+++++++++++uplodExcleFile++++++++");
        showExcleFile(request,response);
    }



    /**
     * 响应给前端一页的题目
     * @param request
     * @param response
     */
    public static void querItems(HttpServletRequest request, HttpServletResponse response) {



        int currentPage = Integer.parseInt((String) request.getParameter("currentPage"));
        int pageContent = 10;
        int totalRows = TestItemCoun();
        /**
         * 判断有几页，如果不能整除就加一
         */
        int pages = totalRows % pageContent == 0 ? totalRows / pageContent
                : totalRows / pageContent + 1;
        if (currentPage > pages) {
            currentPage = pages;
        }else if(currentPage <= 0){
            currentPage = 1;
        }else if (currentPage == oldPge){
//            return;
        }



        int start = (currentPage - 1) * pageContent;
        int end = currentPage * pageContent;


        List<TestIteam> testIteams = queryTest(start, end);
        JSONArray jsonArray = ListArrayToJSONArray(testIteams);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pages",pages);
        jsonArray.put(jsonObject);

        System.out.println(jsonArray.toString());


        try {
            response.getOutputStream().write(jsonArray.toString().getBytes("utf8"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        oldPge = currentPage;
        System.out.println("pages = " + pages + "  currentPage = " + currentPage);


    }

    /**
     * 数据库插入题目
     * @param iteam
     * @return
     */
    private static Boolean addTestItem(TestIteam iteam) {


        TestItemCoun();

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "insert into questions value (null,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, iteam.category);
            preparedStatement.setObject(2, iteam.title);
            preparedStatement.setObject(3, iteam.A);
            preparedStatement.setObject(4, iteam.B);
            preparedStatement.setObject(5, iteam.C);
            preparedStatement.setObject(6, iteam.D);
            preparedStatement.setObject(7, iteam.answer);

            int row = preparedStatement.executeUpdate();


            if (row > 0) {
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
     * 获取题目的总数
     * @return
     */
    private static int TestItemCoun() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select count(*) from questions";
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

    /**
     * 数据库查询一页的题目
     * @param start
     * @param end
     * @return
     */
    private static List<TestIteam> queryTest(int start, int end) {


        List<TestIteam> testIteams = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "select * from questions order by id desc limit ?,?  ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, start);
            preparedStatement.setObject(2, end-start);
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
                testIteam.showString();
                testIteams.add(testIteam);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.close(connection, preparedStatement, resultSet);
        }


        return testIteams;
    }


    public static JSONArray ListArrayToJSONArray(List<TestIteam> list) {

        JSONArray jsonArray = new JSONArray();

        for (TestIteam testIteam : list) {

            /**
             * 把 java-Object 转换为 json-Object
             */
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


        return jsonArray;
    }


    /**
     * 解析excle文件
     * @param request
     */
    public static void showExcleFile(HttpServletRequest request,HttpServletResponse response){

        System.out.println("+++++++++++showExcleFile++++++++");
        ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());
        fileUpload.setHeaderEncoding("utf-8");

        try {
            List<FileItem> fileItemList = fileUpload.parseRequest(request);
            for (FileItem fileItem : fileItemList) {

                if (!fileItem.isFormField()) {
                    System.out.println(fileItem.getFieldName());
                    /**
                     * Excle解析开始导入POI相关的jar
                     */
                    Workbook workbook = null;
                    try {
                        workbook = WorkbookFactory.create(fileItem.getInputStream());
                        Sheet sheet = workbook.getSheetAt(0);
                        /**
                         * 获取sheet总行数
                         */
                        int alloRow = sheet.getLastRowNum();

                        for (int i = 1; i <= alloRow; i++) {
                            /**
                             * 获一行中的cell数
                             */
                            Row row = sheet.getRow(i);

                            int coloumNum = row.getPhysicalNumberOfCells();

                            for (int j = 0; j < coloumNum; j++) {
                                String cellValue = row.getCell(j).getStringCellValue();
                                System.out.println(cellValue);
                            }


                            String category = row.getCell(0).getStringCellValue();
                            String title = row.getCell(3).getStringCellValue();
                            String A = row.getCell(4).getStringCellValue();
                            String B = row.getCell(5).getStringCellValue();
                            String C = row.getCell(6).getStringCellValue();
                            String D = row.getCell(7).getStringCellValue();
                            String answer = row.getCell(8).getStringCellValue();


                            TestIteam testIteam = new TestIteam(null,category,title,A,B,C,D,answer);

                            addTestItem(testIteam);


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("解析Excel失败！");
                    } finally {
                        if (workbook != null) {
                            try {
                                workbook.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
            }

            request.getRequestDispatcher("/views/admin1.jsp").forward(request,response);


        } catch (Exception e) {
            e.printStackTrace();
        }




    }


}
