package com.zhao.commonservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.commonservice.reqvo.BaseReqVO;

import java.util.List;

public interface BaseService<T> extends IService<T> {

    IPage<T> getPage(BaseReqVO reqVO);
    IPage<T> getPage();
    T getDetail(BaseReqVO reqvo);
    List<String> getCreateTableSql();

}
