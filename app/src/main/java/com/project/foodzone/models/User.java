package com.project.foodzone.models;

public class User {
    private String name;
    private String phone;
    private String uid;
    private String isUser;

    public User() {

    }

    public User(String name, String phone, String uid, String isUser) {
        this.name = name;
        this.phone = phone;
        this.uid = uid;
        this.isUser = isUser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
