package com.iflytek.zhyl.valhalla.entity;

import java.io.Serializable;

/**
 * TbTemporary is a Querydsl bean type
 */
public class TbTemporary implements Serializable {

    private Long id;

    private String data;

    private java.util.Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

}

