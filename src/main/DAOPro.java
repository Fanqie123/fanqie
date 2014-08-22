package main;

import javabean.Conn;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射DAO 完成javabean 基本 增删查改
 * Created by test on 2014/8/20.
 */
public class DAOPro<E> {
    private E e;

    public List<E> findAll() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<E> list = new ArrayList<E>();
        Field[] fileds = e.getClass().getDeclaredFields();
        Connection connection = new Conn().getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from " + e.getClass().getSimpleName());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            E temp = (E) e.getClass().newInstance();
            for (Field field : fileds) {
                field.setAccessible(true);
                field.set(temp, rs.getObject(field.getName()));
            }
            list.add(temp);
        }
        ps.close();
        connection.close();
        return list;
    }

    public List<E> find(E e) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<E> list = new ArrayList<E>();
        Field[] fields = e.getClass().getDeclaredFields();
        Connection connection = new Conn().getConnection();
        StringBuilder sql = new StringBuilder("select * from " + e.getClass().getSimpleName() + " where ");
        int count = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(e) != null) {
                sql.append(field.getName());
                sql.append("=? and ");
                count++;
            }
        }
        sql.delete(sql.length() - 5, sql.length());
        System.out.print(sql.toString());
        PreparedStatement ps = connection.prepareStatement(sql.toString());

        for (Field field : fields) {
            if (field.get(e) != null) {
                ps.setObject(count++, field.get(e));
            }
        }
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            E temp = (E) e.getClass().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(temp, rs.getObject(field.getName()));
            }
            list.add(temp);
        }
        ps.close();
        connection.close();
        return list;
    }

    public boolean create(E e) throws SQLException, ClassNotFoundException, IllegalAccessException {
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

        Connection connection = new Conn().getConnection();
        PreparedStatement ps = connection.prepareStatement(sql1.toString());

        for (int i = 0; i < fields.length; i++) {
            ps.setObject(i + 1, fields[i].get(e));
        }
        int count = ps.executeUpdate();
        ps.close();
        return count == 1;
    }

    public boolean delete(E e) throws IllegalAccessException, SQLException, ClassNotFoundException {
        Field[] fields = e.getClass().getDeclaredFields();
        Connection connection = new Conn().getConnection();
        StringBuilder sql = new StringBuilder("delete from " + e.getClass().getSimpleName() + " where ");
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(e) != null) {
                sql.append(field.getName());
                sql.append("=? and ");
            }
        }
        sql.delete(sql.length() - 5, sql.length());
        System.out.print(sql.toString());
        PreparedStatement ps = connection.prepareStatement(sql.toString());
        int index = 1;
        for (Field field : fields) {
            if (field.get(e) != null) {
                ps.setObject(index++, field.get(e));
            }
        }
        int count = ps.executeUpdate();
        ps.close();
        connection.close();
        return count == 1;
    }

}
