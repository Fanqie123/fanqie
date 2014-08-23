package Servlet;

import javabean.User;
import main.DAOProxy;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;

public class Login extends HttpServlet {
    /**
     * 用于登录验证和验证对应账号是否被注册过
     */
    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher rd;
        User user = new User();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Map<String, String> map = new HashMap<String, String>();
            for (Cookie cookie : cookies) {
                map.put(cookie.getName(), cookie.getValue());
            }

            if (map.containsKey("account")) {
                String cookieAccount = map.get("account");
                String cookiePassword = map.get("password");
                user = new User();
                user.setAccount(cookieAccount);
                user.setPassword(cookiePassword);

                try {
                    if (!new DAOProxy<User>(user).find().isEmpty()) {
                        request.getSession().setAttribute("account",cookieAccount);
                        rd = request.getRequestDispatcher("/WEB-INF/main.html");
                        rd.forward(request,response);
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String[] check = request.getParameterValues("check");
        if (account == null || password == null) {
            rd = request.getRequestDispatcher("/login.html");
            rd.forward(request, response);
            return;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(password.getBytes());
            String md5Password = new BigInteger(md5.digest()).toString(16);

            user.setAccount(account);
            user.setPassword(md5Password);

            if (!new DAOProxy<User>(user).find().isEmpty()) {
                if (check != null) {
                    Cookie cookie1 = new Cookie("account", account);
                    Cookie cookie2 = new Cookie("password", md5Password);
                    cookie1.setMaxAge(600);
                    cookie2.setMaxAge(600);
                    cookie1.setHttpOnly(true);
                    cookie2.setHttpOnly(true);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                }
                request.getSession().setAttribute("account", account);
                rd = request.getRequestDispatcher("/WEB-INF/main.html");
                try {
                    rd.forward(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                PrintWriter out;
                try {
                    out = response.getWriter();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                request.removeAttribute("account");
                request.removeAttribute("password");
                response.setHeader("refresh", "3,/fanqie/login.html");
                out.print("<p>账号密码错误..</p>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }
}
