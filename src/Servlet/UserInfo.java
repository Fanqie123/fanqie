package Servlet;

import com.google.gson.Gson;
import javabean.User;
import main.DAOProxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户资料的查询和修改
 * Created by test on 2014/8/24.
 */
public class UserInfo extends HttpServlet {
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String account = (String) request.getSession().getAttribute("account");
        String userInfo = request.getParameter("user");
        PrintWriter out = response.getWriter();

        if (userInfo == null) {
            User user = new User();
            user.setAccount(account);
            try {
                List<User> list = new DAOProxy<User>(user).find();
                if (list.isEmpty()) {
                    out.print("not_found");
                    out.close();
                    return;
                }
                User user1 = list.get(0);
                out.print(new Gson().toJson(user1));
                System.out.print(new Gson().toJson(user1));
                out.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return;
        }

        User user = new Gson().fromJson(userInfo, User.class);
        user.setAccount(account);
        try {
            boolean bool = new DAOProxy<User>(user).updateUser();
            if (bool) {
                out.print("success");
            } else {
                out.print("fail");
            }
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
