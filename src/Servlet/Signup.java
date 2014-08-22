package Servlet;

import com.google.gson.Gson;
import javabean.User;
import main.DAOProxy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Signup extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String s = request.getParameter("info");
        User user = new Gson().fromJson(s, User.class);
        PrintWriter out;
        try {
            out=response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if(user.getAccount()==null||user.getName()==null||user.getId()==null||user.getPassword()==null) return;

        try {
            if(new DAOProxy().accountCheck(user.getAccount())){
                out.print("exist");
                out.close();
                return;
            }
            if(new DAOProxy().signUp(user)){
                out.print("succeed");
                out.flush();
            }else {
                out.print("failed");
                out.flush();
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
