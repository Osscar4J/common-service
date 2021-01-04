package com.zhao.commonservice.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.commonservice.dao.MyBaseMapper;
import com.zhao.commonservice.entity.SuperBaseEntity;
import com.zhao.commonservice.reqvo.BaseReqVO;
import com.zhao.commonservice.service.BaseService;
import com.zhao.commonservice.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

public abstract class MyBaseService<M extends MyBaseMapper, T> extends ServiceImpl<MyBaseMapper<T>, T> implements BaseService<T> {

    @Autowired
    private M myBaseMapper;

    @Override
    public IPage<T> getPage() {
        return getPage(null);
    }

    @Override
    public T getDetail(BaseReqVO reqvo) {
        return (T) myBaseMapper.selectDetail(reqvo);
    }

    @Override
    public IPage<T> getPage(BaseReqVO reqVO) {
        if (reqVO == null)
            reqVO = new BaseReqVO();
        Page<T> page;
        if (reqVO.getPageable() == 1) {
            page = new Page<>(reqVO.getCurrent(), reqVO.getSize());
            page.setRecords(myBaseMapper.selectPage(reqVO));
            page.setTotal(myBaseMapper.selectTotalCount(reqVO));
//            return myBaseMapper.selectPage(page, reqVO);
        } else {
            page = new Page<>();
            page.setRecords(myBaseMapper.selectPage(reqVO));
        }
        return page;
    }

    @Override
    public boolean save(T entity) {
        autoSetUpdateTime(entity.getClass(), entity, "updateTime");
        autoSetUpdateTime(entity.getClass(), entity, "createTime");
        return super.save(entity);
    }

    private T autoSetUpdateTime(Class<?> claz, T entity, String fieldName){
        try {
            Field createTimefield = claz.getDeclaredField(fieldName);
            createTimefield.setAccessible(true);
            createTimefield.set(entity, Calendar.getInstance().getTime());
        } catch (Exception e) {
            if (claz.getSuperclass() != Object.class)
                return autoSetUpdateTime(claz.getSuperclass(), entity, fieldName);
        }
        return entity;
    }

    @Override
    public boolean updateById(T entity) {
        autoSetUpdateTime(entity.getClass(), entity, "updateTime");
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        if (!this.update(new UpdateWrapper<T>().set("is_delete", 1).eq("id", id)))
            return super.removeById(id);
        return true;
    }

    @Override
    public List<String> getCreateTableSql(){
        return null;
    }

    public void autoCreateTable(DataSourceService dataSourceService, Class<? extends SuperBaseEntity> claz){
        TableName tableName = claz.getAnnotation(TableName.class);
        if (this.getCreateTableSql() != null){
            if (!dataSourceService.tableIsExist(tableName.value())){
                dataSourceService.executeSql(this.getCreateTableSql());
            }
        }
    }

}
