package main;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebFilter(filterName = "Filter")
public class Filter implements javax.servlet.Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        System.out.println("+++++++Filter+++++++");


        /**
         * 过滤没有登录的管理人员
         */
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        String psdKey = (String) request.getSession().getAttribute("dfldfjaldfjalsdfj");
        System.out.println("++psdKey+++==="+psdKey);

        if(Objects.equals(psdKey,"true")){
            chain.doFilter(req, resp);
        }else {
            request.getRequestDispatcher("/views/administer.jsp").forward(request,response);
        }






    }

    public void init(FilterConfig config) throws ServletException {

    }

}
