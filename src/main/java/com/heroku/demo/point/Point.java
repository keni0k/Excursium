package com.heroku.demo.point;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Point {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotEmpty
    private long id;

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public Integer getId_of_guide() {
        return id_of_guide;
    }

    public void setId_of_guide(Integer id_of_guide) {
        this.id_of_guide = id_of_guide;
    }

    @NotEmpty
    private String p1 = "";
    @NotEmpty
    private String p2 = "";
    @NotEmpty
    private Integer id_of_guide = 0;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    @NotEmpty
    private String data = "";

}