package com.login_signup_screendesign_demo.Mode;

public class User {
    private String Name;
    private String Password;
    private int water;
    private int limit;

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public User(int limit) {
        this.limit = limit;
    }

    public String tap;

    public String getTap() {
        return tap;
    }

    public void setTap(String tap) {
        this.tap = tap;
    }

    public User() {

    }
    public int getWater()
    {

        return water;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
    }
    public User(String tap)
    {
        this.tap = tap;
    }
    public User(String name, String password, int water) {
        Name = name;
        Password = password;
        this.water = water;
    }
}
