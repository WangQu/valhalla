package com.iflytek.zhyl.valhalla.entity;

import java.io.Serializable;

/**
 * TbTemplate is a Querydsl bean type
 */
public class TbTemplate implements Serializable {

    private Long id;

    private String name;

    private byte[] syntax;

    private byte[] args;

    private Long datasourceId;

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

    public byte[] getSyntax() {
        return syntax;
    }

    public void setSyntax(byte[] syntax) {
        this.syntax = syntax;
    }

    public byte[] getArgs() {
        return args;
    }

    public void setArgs(byte[] args) {
        this.args = args;
    }

    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
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

