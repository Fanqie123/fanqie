package main;

import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


public class test {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String a = "2013-8-13";
        String b = "2013-8-14";
        System.out.print(a.compareTo(b));
    }

}


