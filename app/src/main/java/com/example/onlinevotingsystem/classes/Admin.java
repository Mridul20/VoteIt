package com.example.onlinevotingsystem.classes;

public class Admin {

    private String Username;
    private String Name;
    private String PhotoURL;
    private String PhoneNum;

    public Admin(String username, String name, String phoneNum) {
        Username = username;
        Name = name;
        PhoneNum = phoneNum;
    }

    public Admin(String username, String name, String photoURL, String phoneNum) {
        Username = username;
        Name = name;
        PhotoURL = photoURL;
        PhoneNum=phoneNum;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }
}
