package Servlet;

import com.google.gson.Gson;
import javabean.Room;
import main.DAOProxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 123
 * Created by test on 2014/8/1.
 */
public class Search extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Room> list;
        String type = request.getParameter("type");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        Date start_date;
        Date end_date;
        Date today;
        try {
            today = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        PrintWriter out = response.getWriter();

        try {
            start_date = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            end_date = new SimpleDateFormat("yyyy-MM-dd").parse(end);
        } catch (ParseException e) {
            e.printStackTrace(System.out);
            out.print("error");
            out.close();
            return;
        }

        if (start_date != null && end_date != null && !today.after(start_date) && !start_date.after(end_date)) {
            try {
                list = new DAOProxy().findRoom(type, start_date, end_date);
                String s = new Gson().toJson(list);
                out = response.getWriter();
                out.print(s);
                out.flush();

            } catch (SQLException e) {
                e.printStackTrace(System.out);
            } catch (ClassNotFoundException e) {
                e.printStackTrace(System.out);
            }
        } else {
            out.print("error");
            out.flush();
        }

        out.close();


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
