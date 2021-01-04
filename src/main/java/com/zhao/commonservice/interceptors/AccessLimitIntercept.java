package com.zhao.commonservice.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.zhao.common.exception.BusinessException;
import com.zhao.common.respvo.BaseResponse;
import com.zhao.common.respvo.ResponseStatus;
import com.zhao.common.utils.CommonUtils;
import com.zhao.commonservice.annotations.AccessLimit;
import com.zhao.commonservice.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@Component
public class AccessLimitIntercept implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AccessLimitIntercept.class);

    @Autowired
    private CacheService cacheService;

    /**
     * 接口调用前检查对方ip是否频繁调用接口
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // handler是否为 HandleMethod 实例
        if (handler instanceof HandlerMethod) {
            // 强转
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法
            Method method = handlerMethod.getMethod();
            // 判断方式是否有AccessLimit注解，有的才需要做限流
            if (!method.isAnnotationPresent(AccessLimit.class)) {
                return true;
            }

            // 获取注解上的内容
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            // 获取方法注解上的请求次数
            int times = accessLimit.times();
            // 获取方法注解上的请求时间
            int second = accessLimit.second();

            // 拼接redis key = IP + Api限流
            String key = CommonUtils.getIpAddr(request) + request.getRequestURI();
            // 获取redis的value
            Integer maxTimes = null;
            String value = (String) cacheService.get(key);
            if (!StringUtils.isEmpty(value)) {
                maxTimes = Integer.valueOf(value);
            }
            if (maxTimes == null) {
                // 如果redis中没有该ip对应的时间则表示第一次调用，保存key到redis
                cacheService.put(key, "1", second);
            } else if (maxTimes < times) {
                // 如果redis中的时间比注解上的时间小则表示可以允许访问,这是修改redis的value时间
                cacheService.put(key, maxTimes + 1 + "", second);
            } else {
                logger.info(key + " 操作太频繁");
                throw new BusinessException(ResponseStatus.OPERATION_TOO_FAST);
            }
        }
        return true;
    }

    private boolean setResponse(BaseResponse results, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = null;
        try {
            response.setHeader("Content-type", "application/json; charset=utf-8");
            outputStream = response.getOutputStream();
            outputStream.write(JSONObject.toJSONString(results).getBytes("UTF-8"));
            return false;
        } catch (Exception e) {
            logger.error("setResponse方法报错", e);
            return false;
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }
}
