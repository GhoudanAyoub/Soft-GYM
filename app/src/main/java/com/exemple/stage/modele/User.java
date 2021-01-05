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

    public User(String city, String phone, String address, String name,
                String country, String zip, String image, String gmail,
                int days, String IDUsers, String status) {
        City = city;
        Phone = phone;
        Address = address;
        this.name = name;
        Country = country;
        Zip = zip;
        this.image = image;
        this.gmail = gmail;
        Days = days;
        this.IDUsers = IDUsers;
        this.status = status;
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

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public int getDays() {
        return Days;
    }

    public void setDays(int days) {
        Days = days;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
