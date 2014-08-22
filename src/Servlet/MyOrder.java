package Servlet;

import com.google.gson.Gson;
import javabean.OrderList;
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
 * 我的订单查询
 * Created by test on 2014/8/6.
 */
public class MyOrder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<OrderList> list = null;
        PrintWriter out = null;
//        String account=request.getParameter("account");
        try {
            list = new DAOProxy().findOrderList("admin1");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert out != null;
        out.print(new Gson().toJson(list));
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
