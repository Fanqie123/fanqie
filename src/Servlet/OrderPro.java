package Servlet;

import com.google.gson.Gson;
import javabean.OrderList;
import javabean.Room;
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
import java.util.List;

/**
 * 整合订单操作
 * Created by test on 2014/8/22.
 */
public class OrderPro extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String room_no = request.getParameter("room_no");
        String room_type = request.getParameter("type");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String order_no=request.getParameter("order_no");
        PrintWriter out;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (room_type != null && start != null && end != null) {              //房间查询
            List<Room> list;
            String current=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            if(!(start.compareTo(end)>0||current.compareTo(start)>0)){
                try {
                    Room room = new Room();
                    room.setRoom_type(room_type);
                    list = new DAOProxy<Room>(room).findRoom(start, end);
                    String s = new Gson().toJson(list);
                    out.print(s);
                    out.flush();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                out.print("date_error");
                out.flush();
            }
            out.close();
            return;
        }

        String account = (String) request.getSession().getAttribute("account");


        if (account != null && room_no != null && start != null && end != null) {   //房间预订
            String order_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            OrderList order = new OrderList(room_no, account, start, end, order_date);
            try {
                if (new DAOProxy<OrderList>(order).createOrder()) {
                    out.print("succeed");
                } else {
                    out.print("failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.close();
            return;
        }

        if (room_no != null) {
            Room room = new Room();
            room.setRoom_no(room_no);
            List list;
            try {
                list=new DAOProxy<Room>(room).find();
                if(list.isEmpty())
                    out.print("illegal_room_no");
            } catch (Exception e){
                e.printStackTrace();
            }
            out.close();
            return;
        }

        if (order_no != null) {
            OrderList order = new OrderList();
            order.setOrder_no(Integer.parseInt(order_no));
            order.setAccount(account);
            boolean bool = false;
            try {
                bool=new DAOProxy<OrderList>(order).remove();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(bool) {
                out.print("success");
            }else {
                out.print("failed");
            }

            out.close();
            return;
        }

        if(account!=null) {               //查询订单
            OrderList order = new OrderList();
            order.setAccount(account);
            List list;
            try {
                list = new DAOProxy<OrderList>(order).find();
            } catch (Exception e){
                e.printStackTrace();
                return;
            }

            if(!list.isEmpty()){
                String json = new Gson().toJson(list);
                out.print(json);
                out.close();
            }
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
