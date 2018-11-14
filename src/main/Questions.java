package main;

import Models.TestIteam;
import jdbc.JDBC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Questions {

    public static void getPar(HttpServletRequest request, HttpServletResponse response) {

        String category = (String) request.getParameter("category");
        String title = (String) request.getParameter("title");
        String A = (String) request.getParameter("tA");
        String B = (String) request.getParameter("tB");
        String C = (String) request.getParameter("tC");
        String D = (String) request.getParameter("tD");
        String answer = (String) request.getParameter("answer");


        TestIteam testIteam = new TestIteam(category,title,A,B,C,D,answer);
        testIteam.showString();


        try {

            if (addTestItem(testIteam)){
                response.getOutputStream().write("true".getBytes("utf8"));
            }else {
                response.getOutputStream().write("false".getBytes("utf8"));
            }

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private static Boolean addTestItem(TestIteam iteam){

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBC.GetConnection();
            String sql = "insert into questions value (null,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,iteam.category);
            preparedStatement.setObject(2,iteam.title);
            preparedStatement.setObject(3,iteam.A);
            preparedStatement.setObject(4,iteam.B);
            preparedStatement.setObject(5,iteam.C);
            preparedStatement.setObject(6,iteam.D);
            preparedStatement.setObject(7,iteam.answer);

            int row = preparedStatement.executeUpdate();


            if (row>0){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            JDBC.close(connection,preparedStatement);
        }

    }






}
