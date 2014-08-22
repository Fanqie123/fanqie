package Servlet;

import main.DAOProxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by test on 2014/8/7.
 */
public class OrderDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String room_no = request.getParameter("order_no");
        String account = request.getParameter("account");
        if (room_no == null || account == null) {
            return;
        }
        try {
            int flag = new DAOProxy().deleteOrderList(Integer.parseInt(room_no), account);
            PrintWriter out = response.getWriter();
            if (flag == 4) {
                out.print("succceed");
            } else {
                out.print("failed");
            }
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
