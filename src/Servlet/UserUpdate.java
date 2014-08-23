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
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**123
 * Created by test on 2014/8/14.
 */
public class UserUpdate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /* String json = request.getParameter("user");
        String account = (String) request.getSession().getAttribute("account");
        PrintWriter out = response.getWriter();
        boolean bool = false;

        if (account == null) return;
        User user = new Gson().fromJson(json, User.class);
        user.setAccount(account);

        try {
            if (!new DAOProxy().accountCheck(account)) return;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            bool = new DAOProxy().UpdateUser(user);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (bool)
            out.print("success");

        out.close();*/
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
