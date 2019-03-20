package jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.*;

public class JDBC{



    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类，加载驱动失败。");
            e.printStackTrace();
        }
    }

    public static Connection GetConnection() throws Exception {

        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ask?useUnicode=true&amp;characterEncoding=utf-8",
                "root",
                "Sc!123456");
    }
    /**
     * 释放资源
     */
    public static void close(Connection connection, Statement statement) {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            statement = null;
        }
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            statement = null;
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resultSet = null;
        }


    }

}


///**
// * 使用链接池，同时有多个链接查询
// */
//public class JDBC{
//
//    private static final ComboPooledDataSource dataSource = new ComboPooledDataSource();
//
//    /**
//     * 重链接池中获得链接
//     */
//    public static Connection GetConnection() throws Exception {
//        Connection connection = dataSource.getConnection();
//        return connection;
//    }
//
//    /**
//     * 释放资源
//     */
//    public static void close(Connection connection, Statement statement) {
//
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            connection = null;
//        }
//        if (statement != null) {
//            try {
//                statement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            statement = null;
//        }
//    }
//
//    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            connection = null;
//        }
//        if (statement != null) {
//            try {
//                statement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            statement = null;
//        }
//        if (resultSet != null) {
//            try {
//                resultSet.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            resultSet = null;
//        }
//
//
//    }
//
//
//}
