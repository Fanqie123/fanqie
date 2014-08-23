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

/**serching user info
 * return a json
 * Created by test on 2014/8/14.
 */
public class UserInfo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /* String account = (String)request.getSession().getAttribute("account");
        if(account==null) return;

        try {
            User user = new DAOProxy().findUser(account);
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(user));
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
