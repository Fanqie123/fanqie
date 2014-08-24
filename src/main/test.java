package main;

import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


public class test {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.print((0 + 15) / 2);
    }

    public static int gcd(int p, int q) {
        if(q==0) return p;
        int r = p % q;
        return gcd(q,r);
    }

}


