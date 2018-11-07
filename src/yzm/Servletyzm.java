package yzm;

import com.aliyuncs.exceptions.ClientException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Servletyzm")
public class Servletyzm extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {



        String phone = (String) request.getParameter("phone");
        System.out.println(phone+"+++++++");

        try {
            Verification.sendSms(phone);
        }catch (ClientException e){
            e.printStackTrace();
        }


        Map<String, String> pop = new HashMap<>();
        pop.put("a", "稻香");
        pop.put("b", "晴天");
        pop.put("c", "告白气球");
        JSONObject jsonarr = null;
        jsonarr = new JSONObject(pop);
        response.getOutputStream().write(jsonarr.toString().getBytes("utf-8"));
        System.out.println("++++Servlet-yzm++++"+phone+"0123");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
       doPost(request,response);

    }
}
