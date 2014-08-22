package main;


import javabean.OrderList;
import javabean.Room;
import javabean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * dao
 * Created by test on 2014/7/31.
 */
class DAOImpl {
    private Connection connection = null;
    private PreparedStatement ps;

    public DAOImpl(Connection connection) {
        this.connection = connection;
    }

    public User findUser(String account) throws SQLException {
        User user = new User();
        ps = connection.prepareStatement("select password,name,sex,id from User where account=?");
        ps.setString(1, account);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            user.setAccount(account);
            user.setPassword(rs.getString(1));
            user.setName(rs.getString(2));
            user.setSex(rs.getString(3));
            user.setId(rs.getString(4));
            ps.close();
            return user;
        }
        ps.close();
        return null;
    }

    public boolean doCreateUser(User user) throws SQLException {
        ps = connection.prepareStatement("insert into user values(?,?,?,?,?)");
        ps.setString(1, user.getAccount());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getName());
        ps.setString(4, user.getSex());
        ps.setString(5, user.getId());
        int count = ps.executeUpdate();
        ps.close();
        return count==1;
    }

    public boolean UpdateUser(User user) throws SQLException {
        ps=connection.prepareStatement("update user set name=?,sex=?,id=? where account=?");
        ps.setString(1, user.getName());
        ps.setString(2, user.getSex());
        ps.setString(3, user.getId());
        ps.setString(4, user.getAccount());
        int count = ps.executeUpdate();
        ps.close();
        return count == 1;
    }

    public List<Room> findRoom(String room_type, String start_date, String end_date) throws SQLException {
        List<Room> list = new ArrayList<Room>();
        String sql;
        if (room_type.equals("0")) {
            sql = "select a.room_no,a.room_type,a.room_price,a.room_info from room a where not exists(select 1 from order_list b where a.room_no=b.room_no and " +
                    "(? between start_date and end_date or ? between start_date and end_date))";
        } else {
            sql = "select a.room_no,a.room_type,a.room_price,a.room_info from room a where not exists(select 1 from order_list b where a.room_no=b.room_no and " +
                    "(? between start_date and end_date or ? between start_date and end_date)) and a.room_type='" +
                    room_type+"'";
        }
        ps = connection.prepareStatement(sql);
        ps.setString(1, start_date);
        ps.setString(2, end_date);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Room room = new Room();
            room.setRoom_no(rs.getString(1));
            room.setRoom_type(rs.getString(2));
            room.setRoom_price(rs.getInt(3));
            room.setRoom_info(rs.getString(4));
            list.add(room);
        }
        return list;
    }

    public boolean checkRoomNo(String room_no) throws SQLException {
        ps = connection.prepareStatement("select 1 from room where room_no=?");
        ps.setString(1, room_no);
        ResultSet rs = ps.executeQuery();
        boolean bool = rs.next();
        ps.close();
        return bool;
    }

    public boolean checkOrder(String room_no, String start_date, String end_date) throws SQLException {
        ps = connection.prepareStatement("select 1 from order_list  where room_no = ? and " +
                "(? between start_date and end_date or ? between start_date and end_date)");
        ps.setString(1, room_no);
        ps.setString(2, start_date);
        ps.setString(3, end_date);
        ResultSet rs = ps.executeQuery();
        boolean bool = rs.next();
        ps.close();
        return bool;
    }

    public List<OrderList> findOrderList(String account) throws SQLException {
        List<OrderList> list = new ArrayList<OrderList>();
        ps = connection.prepareStatement("select order_no,room_no,start_date,end_date,order_date from order_list where account=?");
        ps.setString(1, account);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            OrderList order = new OrderList();
            order.setOrder_no(rs.getInt(1));
            order.setRoom_no(rs.getString(2));
            order.setStart_date(rs.getString(3));
            order.setEnd_date(rs.getString(4));
            order.setOrder_date(rs.getString(5));
            list.add(order);
        }
        ps.close();
        return list;
    }

    public OrderList findOrderList(int order_no) throws SQLException {
        OrderList order;
        ps = connection.prepareStatement("select order_no,room_no,start_date,end_date,order_date from order_list where order_no=?");
        ps.setInt(1, order_no);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            order = new OrderList();
            order.setOrder_no(rs.getInt(1));
            order.setRoom_no(rs.getString(2));
            order.setStart_date(rs.getString(3));
            order.setEnd_date(rs.getString(4));
            order.setOrder_date(rs.getString(5));
        } else {
            order = null;
        }
        ps.close();
        return order;

    }

    public boolean doCreateOrderList(OrderList order) throws SQLException {
        ps = connection.prepareStatement("insert into order_list value(null,?,?,?,?,?)");
        ps.setString(1, order.getRoom_no());
        ps.setString(2, order.getAccount());
        ps.setString(3, order.getStart_date());
        ps.setString(4, order.getEnd_date());
        ps.setString(5, order.getOrder_date());
        int count = ps.executeUpdate();
        ps.close();
        return count == 1;
    }

    public boolean deleteOrderList(int order_no) throws SQLException {
        ps = connection.prepareStatement("delete from order_list where order_no=?");
        ps.setInt(1, order_no);
        int count = ps.executeUpdate();
        ps.close();
        return count == 1;
    }
}
