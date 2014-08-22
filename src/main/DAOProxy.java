package main;

import javabean.Conn;
import javabean.OrderList;
import javabean.Room;
import javabean.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * String md5(String string) md5加密
 * Created by test on 2014/7/31.
 */
public class DAOProxy {
    private Conn conn = null;
    private DAOImpl dao = null;

    public DAOProxy() throws SQLException, ClassNotFoundException {
        this.conn = new Conn();
        dao = new DAOImpl(this.conn.getConnection());
    }

    public User findUser(String account) throws SQLException {
        User user = dao.findUser(account);
        conn.close();
        return user;
    }
    public String loginCheck(String account,String password) throws SQLException, NoSuchAlgorithmException {
        User user = dao.findUser(account);
        conn.close();
        if(md5(password).equals(user.getPassword()))
            return user.getPassword();
        else
            return null;
    }

    public boolean accountCheck(String account) throws SQLException {
        User user = dao.findUser(account);
        conn.close();
        return user!=null;
    }

    public boolean signUp(User user) throws SQLException, NoSuchAlgorithmException {
        user.setPassword(md5(user.getPassword()));
        boolean bool = dao.doCreateUser(user);
        conn.close();
        return bool;
    }

    public boolean UpdateUser(User user) throws NoSuchAlgorithmException, SQLException {
        boolean bool = dao.UpdateUser(user);
        conn.close();
        return bool;
    }

    public List<Room> findRoom(String room_type, Date start_date, Date end_date) throws SQLException {
        List<Room> list;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String start_date_st = format.format(start_date);
        String end_date_st = format.format(end_date);
        list = dao.findRoom(room_type, start_date_st, end_date_st);
        conn.close();
        return list;
    }

    public boolean checkRoomNo(String room_no) throws SQLException {
        boolean bool = dao.checkRoomNo(room_no);
        conn.close();
        return bool;
    }

    public boolean checkOrder(String room_no, String start_date, String end_date) throws SQLException {
        boolean bool = dao.checkOrder(room_no, start_date, end_date);
        conn.close();
        return bool;

    }

    public List<OrderList> findOrderList(String account) throws SQLException {
        List<OrderList> list = dao.findOrderList(account);
        conn.close();
        return list;
    }

    public boolean doCreateOrderList(OrderList order) throws SQLException {
        boolean bool = dao.doCreateOrderList(order);
        conn.close();
        return bool;
    }

    public int deleteOrderList(int order_no, String account) throws SQLException, ParseException {
        OrderList order = dao.findOrderList(order_no);
        if (order == null) {
            conn.close();
            return 0;
        }
        if (!account.equals(order.getAccount())) {
            conn.close();
            return 1;
        }
        Date start_date = new SimpleDateFormat("yyyy-MM-dd").parse(order.getStart_date());
        if (start_date.before(new Date())) {
            conn.close();
            return 2;
        }
        boolean bool = dao.deleteOrderList(order_no);
        conn.close();
        if (!bool) return 3;
        else return 4;
    }

    public static String md5(String s) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(s.getBytes());
        return new BigInteger(md5.digest()).toString(16);
    }

}

