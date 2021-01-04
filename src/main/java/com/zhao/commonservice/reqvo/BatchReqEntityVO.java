package com.zhao.commonservice.reqvo;

import java.util.List;

public class BatchReqEntityVO<T> {

    private Integer id;

    private List<T> records;

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
