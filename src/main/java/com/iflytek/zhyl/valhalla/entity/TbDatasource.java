package com.iflytek.zhyl.valhalla.entity;

import java.io.Serializable;

/**
 * TbDatasource is a Querydsl bean type
 */
public class TbDatasource implements Serializable {

    private Long id;

    private String name;

    private String config;

    private Integer enabled;

    private String createOperator;

    private java.util.Date createTime;

    private String modifyOperator;

    private java.util.Date modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyOperator() {
        return modifyOperator;
    }

    public void setModifyOperator(String modifyOperator) {
        this.modifyOperator = modifyOperator;
    }

    public java.util.Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(java.util.Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}

