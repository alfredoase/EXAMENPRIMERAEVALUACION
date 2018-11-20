package com.example.user.quicktrade.model;

/**
 * Class to construct the user
 */
public class UserRegistration {
    String userId, user_name, user_surnames, user_email, user_nickname, user_pass, user_birthday, user_phone;

    public UserRegistration() {

    }

    @Override
    public String toString() {
        return "UserRegistration{" +
                "user_id='" + userId + '\'' +
                "user_name='" + user_name + '\'' +
                ", user_surnames='" + user_surnames + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", user_pass='" + user_pass + '\'' +
                ", user_birthday='" + user_birthday + '\'' +
                ", user_phone='" + user_phone + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surnames() {
        return user_surnames;
    }

    public void setUser_surnames(String user_surnames) {
        this.user_surnames = user_surnames;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public UserRegistration(String userId, String user_name, String user_surnames, String user_email, String user_nickname, String user_pass, String user_birthday, String user_phone) {

        this.userId = userId;
        this.user_name = user_name;
        this.user_surnames = user_surnames;
        this.user_email = user_email;
        this.user_nickname = user_nickname;
        this.user_pass = user_pass;
        this.user_birthday = user_birthday;
        this.user_phone = user_phone;
    }
}
