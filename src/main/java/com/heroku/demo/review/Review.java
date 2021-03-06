package com.heroku.demo.review;

import com.heroku.demo.utils.Consts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Review {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String data = "";

    private String imageUrl = "";

    private String time = "";

    private long userId = 0;

    private int likesCount = 0;

    public String userFullName = "";

    private String pathToUserPhoto = "";

    private String advantages = "";
    private String disadvantages = "";

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    private int rate = -1;

    private long eventId = -1;

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getPathToUserPhoto() {
        return Consts.URL_PATH + pathToUserPhoto;
    }

    public void setPathToUserPhoto(String pathToUserPhoto) {
        this.pathToUserPhoto = pathToUserPhoto;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAdvantages() {
        return advantages;
    }

    public void setAdvantages(String advantages) {
        this.advantages = advantages;
    }

    public String getDisadvantages() {
        return disadvantages;
    }

    public void setDisadvantages(String disadvantages) {
        this.disadvantages = disadvantages;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Review() {
    }

    public Review(String data, String imageUrl
            , String time, int userId, int rate, int eventId) {
//            , String surname, int userId, String email, String phoneNumber, int rate, String about, String city) {

        this.data = data;

        this.imageUrl = imageUrl;

        this.userId = userId;

        this.time = time;
        this.rate = rate;
        this.eventId = eventId;
    }

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":\"" + id + "\",\n" +
                "\t\"data\":\"" + data + "\",\n" +
                "\t\"imageUrl\":\"" + imageUrl + "\",\n" +
                "\t\"time\":\"" + time + "\",\n" +
                "\t\"rate\":" + rate + ",\n" +
                "\t\"userId\":\"" + userId + "\",\n" +
                "\t\"eventId\":" + eventId + ",\n" +
                "}";
    }

}
