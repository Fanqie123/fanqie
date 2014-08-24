package main;

import javabean.OrderList;
import javabean.Room;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**bli
 * Created by test on 2014/8/22.
 */
public class DAOProxy<E>{
    E e;
    private Conn conn=null;
    private DAOImpl dao=null;
    @SuppressWarnings("unchecked")
    private static ConcurrentHashMap<String, Lock> map = new ConcurrentHashMap<String, Lock>();
    public DAOProxy(E e) throws SQLException, ClassNotFoundException {
        this.e=e;
        this.conn = new Conn();
        dao = new DAOImpl<E>(e,this.conn.getConnection());
    }

    public List findAll() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        List list = dao.findAll();
        conn.close();
        return list;
    }

    public List find() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        List list = dao.find();
        conn.close();
        return list;
    }

    public boolean create() throws IllegalAccessException, SQLException, ClassNotFoundException {
        boolean bool = dao.create();
        conn.close();
        return bool;
    }

    public boolean remove() throws IllegalAccessException, SQLException, ClassNotFoundException {
        boolean bool = dao.remove();
        conn.close();
        return bool;
    }
    @SuppressWarnings("unchecked")
    public List<Room> findRoom(String start_date, String end_date) throws SQLException {
        List list;
        list = dao.findRoom(start_date, end_date);
        conn.close();
        return list;
    }

    public boolean createOrder() throws SQLException, IllegalAccessException, ClassNotFoundException {
        String room_no = ((OrderList)e).getRoom_no();
        Lock lock;
        boolean bool;
        if(map.containsKey(room_no)) {
            lock = map.get(room_no);
        }else {
            lock = new ReentrantLock();
            map.put(room_no, lock);
        }
        lock.lock();
        try {
            if (dao.checkOrder())
                return false;
            bool=dao.create();
        } finally {
            lock.unlock();
            conn.close();
        }
        return bool;
    }

    public boolean updateUser() throws SQLException {
        boolean bool=dao.updateUser();
        conn.close();
        return bool;
    }

}
