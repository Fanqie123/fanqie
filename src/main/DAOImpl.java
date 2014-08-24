package main;

import javabean.OrderList;
import javabean.Room;
import javabean.User;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射DAO 完成javabean 基本 增删查改
 * Created by test on 2014/8/20.
 */

public class DAOImpl<E> {
    E e;
    Class<E> className;
    Field[] fields;
    Connection connection;
    PreparedStatement ps;
    @SuppressWarnings("unchecked")
    public DAOImpl(E e,Connection connection){
        this.e=e;
        this.className= (Class<E>) e.getClass();
        this.fields = className.getDeclaredFields();
        this.connection=connection;
    }

    public List findAll() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<E> list = new ArrayList<E>();
        Connection connection = new Conn().getConnection();
        ps = connection.prepareStatement("select * from " + className.getSimpleName());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            E temp = className.newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(temp, rs.getObject(field.getName()));
            }
            list.add(temp);
        }
        ps.close();
        return list;
    }

    public List find() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<E> list = new ArrayList<E>();
        StringBuilder sql = new StringBuilder("select * from " + e.getClass().getSimpleName() + " where ");
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(e) != null) {
                sql.append(field.getName());
                sql.append("=? and ");
            }
        }
        sql.delete(sql.length() - 5, sql.length());
        ps = connection.prepareStatement(sql.toString());
        int count=1;
        for (Field field : fields) {
            if (field.get(e) != null) {
                ps.setObject(count++, field.get(e));
            }
        }
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            E temp = className.newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(temp, rs.getObject(field.getName()));
            }
            list.add(temp);
        }
        ps.close();
        return list;
    }

    public boolean create() throws SQLException, ClassNotFoundException, IllegalAccessException {
        Field[] fields = e.getClass().getDeclaredFields();

        StringBuilder sql1 = new StringBuilder("insert into " + e.getClass().getSimpleName() + "(");
        StringBuilder sql2 = new StringBuilder("value(");
        for (Field field : fields) {
            field.setAccessible(true);
            sql1.append(field.getName());
            sql1.append(",");
            sql2.append("?,");
        }
        sql1.deleteCharAt(sql1.length() - 1);
        sql1.append(")");
        sql2.deleteCharAt(sql2.length() - 1);
        sql2.append(")");
        sql1.append(sql2);
        ps = connection.prepareStatement(sql1.toString());

        for (int i = 0; i < fields.length; i++) {
            ps.setObject(i + 1, fields[i].get(e));
        }
        int count = ps.executeUpdate();
        ps.close();
        return count == 1;
    }

    public boolean remove() throws IllegalAccessException, SQLException, ClassNotFoundException {
        Field[] fields = e.getClass().getDeclaredFields();
        StringBuilder sql = new StringBuilder("delete from " + e.getClass().getSimpleName() + " where ");
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(e) != null) {
                sql.append(field.getName());
                sql.append("=? and ");
            }
        }
        sql.delete(sql.length() - 5, sql.length());
        ps = connection.prepareStatement(sql.toString());
        int index = 1;
        for (Field field : fields) {
            if (field.get(e) != null) {
                ps.setObject(index++, field.get(e));
            }
        }
        int count = ps.executeUpdate();
        ps.close();
        return count == 1;
    }

    public List<Room> findRoom(String start_date, String end_date) throws SQLException {
        List<Room> list = new ArrayList<Room>();
        String room_type = ((Room) e).getRoom_type();
        String sql;
        if (room_type.equals("0")) {
            sql = "select a.room_no,a.room_type,a.room_price,a.room_info from room a where not exists(select 1 from orderlist b where a.room_no=b.room_no and " +
                    "(? between start_date and end_date or ? between start_date and end_date))";
        } else {
            sql = "select a.room_no,a.room_type,a.room_price,a.room_info from room a where not exists(select 1 from orderlist b where a.room_no=b.room_no and " +
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
        ps.close();
        return list;
    }

    public boolean checkOrder() throws SQLException {
        OrderList order=(OrderList)e;
        String room_no=order.getRoom_no();
        String start_date=order.getStart_date();
        String end_date=order.getEnd_date();
        ps = connection.prepareStatement("select 1 from orderlist  where room_no = ? and " +
                "(? between start_date and end_date or ? between start_date and end_date)");
        ps.setString(1, room_no);
        ps.setString(2, start_date);
        ps.setString(3, end_date);
        ResultSet rs = ps.executeQuery();
        boolean bool = rs.next();
        ps.close();
        return bool;
    }

    public boolean updateUser() throws SQLException {
        User user = (User) e;
        ps = connection.prepareStatement("update user set password=?,name=?,sex=?,id=? where account=?");
        ps.setString(1, user.getPassword());
        ps.setString(2, user.getName());
        ps.setString(3, user.getSex());
        ps.setString(4, user.getId());
        ps.setString(5, user.getAccount());
        int count = ps.executeUpdate();
        System.out.println(user.getAccount());
        System.out.println(user.getName());
        System.out.println(user.getSex());
        System.out.println(user.getId());
        System.out.println(user.getPassword());
        ps.close();
        return count == 1;
    }
}
