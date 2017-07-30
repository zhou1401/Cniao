package com.kaige.cniao.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/30.
 */

public class Campaign implements Serializable {
    private Long id;
    private String title;
    private String imgUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
