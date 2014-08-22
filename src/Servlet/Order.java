package Servlet;


import javabean.OrderList;
import main.DAOProxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 预定房间
 * Created by test on 2014/8/3.
 */
public class Order extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String account= (String) request.getSession().getAttribute("account");
        String room_no = request.getParameter("room_no");
        String start_date = request.getParameter("start_date");
        String end_date = request.getParameter("end_date");
        String order_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        PrintWriter out = response.getWriter();

        try {
            if (room_no != null && !new DAOProxy().checkRoomNo(room_no)) {
                out.print("illegal_room_no");
                out.close();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (room_no == null || start_date == null || end_date == null || account == null) return;

        OrderList order = new OrderList(room_no, account, start_date, end_date, order_date);
        boolean bool = false;
        try {
            bool = new DAOProxy().doCreateOrderList(order);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (bool) out.print("succeed");
        else out.print("failed");
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
