package main;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebFilter(filterName = "Filter2")
public class Filter2 implements Filter {
    public void destroy() {
    }

    /**
     * 过滤没有登录的用户
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        System.out.println("+++++++Filter2+++++++");

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        String userLogin = (String)request.getSession().getAttribute("USER_SUCCESS_LOGIN");
        String admin = (String)request.getSession().getAttribute("dfldfjaldfjalsdfj");
        System.out.println("++userLogin+++==="+userLogin);

        if(Objects.equals(userLogin,"true") || Objects.equals(admin,"true")){
            chain.doFilter(req, resp);
        }else {
            request.getRequestDispatcher("/views/query.jsp").forward(request,response);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
