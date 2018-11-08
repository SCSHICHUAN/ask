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
import java.util.Random;

@WebServlet(name = "Servletyzm")
public class Servletyzm extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String phone = (String) request.getParameter("phone");

        try {
            /**
             * 获取验证码
             */
            String flag = Verification.sentMessPhone(phone,randomString());

            Map<String, String> pop = new HashMap<>();
            pop.put("flag", flag);
            JSONObject jsonarr = null;
            jsonarr = new JSONObject(pop);
            response.getOutputStream().write(jsonarr.toString().getBytes("utf-8"));


        } catch (ClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("rum+++==="+(int)(1+Math.random()*(100000-1+1)));
        System.out.println("++++Servlet-yzm++++" + phone);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);

    }

    private static  String randomString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i<5;i++){
            stringBuilder.append(randomChar());
        }
        return stringBuilder.toString();
    }

    private static char randomChar(){
        String string = "abcdefghjkmnopqrstuvwxyz0123456789";
        Random random = new Random();
        return string.charAt(random.nextInt(string.length()));
    }



}
