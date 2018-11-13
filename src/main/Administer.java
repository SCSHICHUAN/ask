package main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class Administer {

    public static void landAdmin(HttpServletRequest request, HttpServletResponse response){
        String user = (String)request.getParameter("adminiNum");
        String psd = (String)request.getParameter("adminiPassword");

        if (Objects.equals(user,"1") && Objects.equals(psd,"1")){
               request.getSession().setAttribute("dfldfjaldfjalsdfj","true");
               try {
                   request.getRequestDispatcher("/views/admin1.jsp").forward(request,response);
               }catch (Exception e){
                   e.printStackTrace();
               }

        }else {
            try {
                request.setAttribute("flag", "false");
                request.getRequestDispatcher("/views/administer.jsp").forward(request,response);
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

}
