package com.zhao.commonservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.commonservice.reqvo.BaseReqVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyBaseMapper<T> extends BaseMapper<T> {

    IPage<T> selectPage(Page page, @Param("reqvo") BaseReqVO reqvo);

    List<T> selectPage(@Param("reqvo") BaseReqVO reqvo);

    long selectTotalCount(@Param("reqvo") BaseReqVO reqvo);

    T selectDetail(@Param("reqvo") BaseReqVO reqvo);
}
