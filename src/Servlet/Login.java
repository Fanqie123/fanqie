package Servlet;

import main.DAOProxy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Login extends HttpServlet {
    /**
     * 用于登录验证和验证对应账号是否被注册过
     */
    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String[] check = request.getParameterValues("check");
        PrintWriter out;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (account == null || password == null) {
            response.setHeader("refresh", "3,/fanqie");
            out.print("<p>账号密码不能为空...</p>");
            out.close();
            return;
        }

        try {
            String flag = new DAOProxy().loginCheck(account, password);
            if (flag != null) {
                if (check != null) {
                    Cookie cookie1 = new Cookie("account", account);
                    Cookie cookie2 = new Cookie("password", flag);
                    cookie1.setMaxAge(600);
                    cookie2.setMaxAge(600);
                    cookie1.setHttpOnly(true);
                    cookie2.setHttpOnly(true);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                    System.out.print("1");

                }

                request.getSession().setAttribute("account", account);
                response.setHeader("refresh", "3;url=/fanqie/main.html");
                out.print("<p>登录成功，3s后跳转到...</p>");
            } else {
                response.setHeader("refresh", "3,/fanqie");
                out.print("<p>账号密码错误..</p>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        out.close();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }
}
