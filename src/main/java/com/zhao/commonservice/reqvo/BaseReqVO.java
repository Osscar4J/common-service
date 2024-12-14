package com.zhao.commonservice.reqvo;

import java.io.Serializable;

public class BaseReqVO implements Serializable {
    private static final long serialVersionUID = -3275669491086709021L;
    protected int current = 1;
    protected int size = 10;
    protected int pageable = 1;
    protected Integer status;
    protected Integer id;
    protected String name;

    public int getPageable() {
        return pageable;
    }

    public BaseReqVO setPageable(int pageable) {
        this.pageable = pageable;
        return this;
    }

    public int getCurrent() {
        return current;
    }

    public BaseReqVO setCurrent(int current) {
        this.current = current;
        return this;
    }

    public int getSize() {
        return size;
    }

    public BaseReqVO setSize(int size) {
        this.size = size;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public BaseReqVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public BaseReqVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public long getPosition() {
        return (current - 1) * size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
