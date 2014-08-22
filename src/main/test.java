package main;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by test on 2014/8/11.
 */
public class test {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String plaintext = "admin";
/*        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt =new BigInteger(digest);
        System.out.print(bigInt.toString(16));*/

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(plaintext.getBytes());
        System.out.print(new BigInteger(md5.digest()).toString(16));

    }
}
