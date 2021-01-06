package com.exemple.stage.modele;

/**
 * Created By GHOUADN AYOUB
 */


public class User {


    private String IDUsers;
    public String name;
    public String gmail;
    public String Phone;
    public String image;
    public String Address;
    public int Days;
    private String status;

    public User() { }

    public User(String IDUsers, String name, String gmail, String phone) {
        this.IDUsers = IDUsers;
        this.name = name;
        this.gmail = gmail;
        Phone = phone;
    }

    public String getIDUsers() {
        return IDUsers;
    }

    public void setIDUsers(String IDUsers) {
        this.IDUsers = IDUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getDays() {
        return Days;
    }

    public void setDays(int days) {
        Days = days;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
