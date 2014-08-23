package Servlet;

import com.google.gson.Gson;
import javabean.User;
import main.DAOProxy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Signup extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String s = request.getParameter("info");
        User user = new Gson().fromJson(s, User.class);

        if(user.getAccount()==null||user.getName()==null||user.getId()==null||user.getPassword()==null) return;

        PrintWriter out;
        try {
            out=response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            if (! new DAOProxy<User>(user).find().isEmpty()) {
                out.print("exist");
                out.close();
                return;
            }

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(user.getPassword().getBytes());
            user.setPassword(new BigInteger(md5.digest()).toString(16));

            if (new DAOProxy<User>(user).create()) {
                out.print("succeed");
                out.flush();
            } else {
                out.print("failed");
                out.flush();
            }
        } catch (Exception e){
            out.print("failed");
            out.flush();
            e.printStackTrace();
        }
        out.close();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doPost(request, response);
    }
}
