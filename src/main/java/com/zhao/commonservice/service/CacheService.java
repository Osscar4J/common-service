package com.zhao.commonservice.service;

import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * 缓存服务
 * @Author: zhaolianqi
 * @Date: 2020/9/4 16:42
 * @Version: v1.0
 */
public interface CacheService {

    /**
     * 获取缓存值
     * @Author zhaolianqi
     * @param key key
     * @return 查不到则返回null
     * @Date 2020/9/4 17:19
     */
    Object get(String key);

    /**
     * 设置缓存信息
     * @Author zhaolianqi
     * @param key key
     * @param object 缓存对象
     * @param seconds 有效时长，单位：秒
     * @return
     * @Date 2020/9/4 17:20
     */
    void put(String key, Object object, int seconds);

    /**
     * 从RMapCache里获取数据
     * @Author zhaolianqi
     * @Date 2020/11/16 17:21
     */
    Object getFromMapCache(String map, String key);

    void removeMapCache(String map);
    void removeMapCache(String map, String key);

    /**
     * 存到RMapCache
     * @param map RMap key
     * @param key key
     * @param object 数据
     * @param seconds 时长，单位：秒
     * @Author zhaolianqi
     * @Date 2020/11/16 17:22
     */
    void putMapCache(String map, String key, Object object, int seconds);

    /**
     * 将指定key的值自增1，并且返回自增后的值
     * @param map RMap key
     * @param key key
     * @Author zhaolianqi
     * @Date 2021/1/14 17:10
     */
    int incrementNumber(String map, String key);

    /**
     * 获取key列表
     * @param pattern 支持正则
     * @return
     */
    List<String> getKeysByPattern(String pattern);

    RedissonClient getRedissonClient();

}
