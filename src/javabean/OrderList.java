package javabean;

import java.sql.Date;

/**
 * Created by test on 2014/8/7.
 */
public class OrderList {
    private Integer order_no;
    private String room_no;
    private String account;
    private String start_date;
    private String end_date;
    private String order_date;

    public OrderList() {}

    public OrderList(String room_no, String account, String start_date, String end_date, String order_date) {
        this.room_no = room_no;
        this.account = account;
        this.start_date = start_date;
        this.end_date = end_date;
        this.order_date = order_date;
    }

    public int getOrder_no() {
        return order_no;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setOrder_no(Integer order_no) { this.order_no = order_no; }

    public String getStart_date() { return start_date; }

    public void setStart_date(String start_date) { this.start_date = start_date; }

    public String getEnd_date() { return end_date; }

    public void setEnd_date(String end_date) { this.end_date = end_date; }

    public String getOrder_date() { return order_date; }

    public void setOrder_date(String order_date) { this.order_date = order_date; }
}
