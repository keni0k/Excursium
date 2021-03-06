package com.heroku.demo.photo;

import com.heroku.demo.utils.Consts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Photo {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long eventId = 0;

    private int number = 0;

    public Photo(long eventId, String data, int number) {
        setEventId(eventId);
        setData(data);
        setNumber(number);
    }

    public Photo(){}

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getData() {
        return Consts.URL_PATH + data;
    }

    public String getToken() {
        return data.substring(0,data.length()-4);
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data = "";

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":\"" + id + "\",\n" +
                "\t\"event_id\":\"" + eventId + "\",\n" +
                "\t\"data\":\"" + data + "\",\n" +
                "}";
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
