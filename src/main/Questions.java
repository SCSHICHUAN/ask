package main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Questions {

    public static void getPar(HttpServletRequest request, HttpServletResponse response) {

        String category = (String) request.getParameter("category");
        String title = (String) request.getParameter("title");
        String A = (String) request.getParameter("tA");
        String B = (String) request.getParameter("tB");
        String C = (String) request.getParameter("tC");
        String D = (String) request.getParameter("tD");
        String answer = (String) request.getParameter("answer");


        System.out.println(
                "category:" + category
                        + "  title:" + title
                        + " A:" + A
                        + " B:" + B
                        + " C:" + C
                        + " D:" + D
                        + " answer:" + answer
        );


    }


}
