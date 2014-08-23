package javabean;

import java.io.Serializable;

public class User implements Serializable {
    private String account;
    private String id;
    private String password;
    private String name;
    private String sex;

    public User() {}

    public User(String account, String id, String password, String name, String sex) {
        this.account = account;
        this.id = id;
        this.password = password;
        this.name = name;
        this.sex = sex;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
