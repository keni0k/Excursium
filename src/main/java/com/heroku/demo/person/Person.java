package com.heroku.demo.person;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login = "";
    private String pass = "";
    private String lastName = "";
    private int type = 0;
    private String email = "";
    private String firstName = "";
    private int rate = 0;
    private String phoneNumber = "";
    private String about = "";
    private String city = "";
    private String date = "";
    private String imageUrl = "";


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Person() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Person(String login, String pass, String lastName, int type, String email, String firstName, int rate, String phoneNumber, String about, String city, String date, String imageUrl) {
        this.login = login;
        this.pass = pass;
        this.lastName = lastName;
        this.type = type;
        this.email = email;
        this.firstName = firstName;
        this.rate = rate;
        this.phoneNumber = phoneNumber;
        this.about = about;
        this.city = city;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {

        return "{\n" +

                "\t\"id\":\"" + id + "\",\n" +
                "\t\"login\":\"" + login + "\",\n" +
                "\t\"pass\":\"" + pass + "\",\n" +
                "\t\"lastName\":\"" + lastName + "\",\n" +
                "\t\"firstName\":\"" + firstName + "\",\n" +
                "\t\"type\":\"" + type + "\",\n" +
                "\t\"email\":\"" + email + "\",\n" +
                "\t\"phoneNumber\":\"" + phoneNumber + "\",\n" +
                "\t\"rate\":\"" + rate + "\",\n" +
                "\t\"about\":\"" + about + "\",\n" +
                "\t\"city\":\"" + city + "\",\n" +
                "\t\"token\":\"" + date + "\",\n" +
                "\t\"imageUrl\":\"" + imageUrl + "\"\n" +
                "}";
    }

}
