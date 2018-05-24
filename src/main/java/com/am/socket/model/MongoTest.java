package com.am.socket.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class MongoTest {

    @Id
    private String id;

    private String key1;
    private String key2;
    private Integer key3;
    private Date key4;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public Integer getKey3() {
        return key3;
    }

    public void setKey3(Integer key3) {
        this.key3 = key3;
    }

    public Date getKey4() {
        return key4;
    }

    public void setKey4(Date key4) {
        this.key4 = key4;
    }
}
