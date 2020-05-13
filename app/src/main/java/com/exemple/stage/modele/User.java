package com.exemple.stage.modele;

/**
 * Created By GHOUADN AYOUB
 */


public class User {


    public String City;
    public String Phone;
    public String Address;
    public String name;
    public String Country;
    public String Zip;
    public String image;
    public String gmail;
    public int Days;
    private String IDUsers;
    private String status;
    private String password;

    public User() {
    }

    public User(String gmail, String password) {
        this.gmail = gmail;
        this.password = password;
    }

    public User(String name, String city, String phone, String address, String country, String Zip, String image, String gmail) {
        City = city;
        Phone = phone;
        Address = address;
        this.name = name;
        Country = country;
        this.Zip = Zip;
        this.image = image;
        this.gmail = gmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIDUsers() {
        return IDUsers;
    }

    public void setIDUsers(String IDUsers) {
        this.IDUsers = IDUsers;
    }
}
