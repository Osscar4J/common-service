package com.zhao.commonservice.resolver;

import com.alibaba.fastjson.JSONObject;
import com.zhao.common.exception.BusinessException;
import com.zhao.commonservice.annotations.PartBody;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Parameter;

/**
 * 解析请求体中的部分参数
 * @Author: zhaolianqi
 * @Date: 2020/12/24 9:46
 * @Version: v1.0
 */
public class PartBodyResolver implements HandlerMethodArgumentResolver {

    private static final String JSONBODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(PartBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        String jsonBody = (String) nativeWebRequest.getAttribute(JSONBODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        if (jsonBody == null){
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
            StringBuilder buff = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buff.append(line);
            }
            jsonBody = buff.toString();
            nativeWebRequest.setAttribute(JSONBODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
        }
        Parameter parameter = methodParameter.getParameter();
        PartBody partBodyAnn = parameter.getAnnotation(PartBody.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonBody);
        // 获取参数名
        String parameterName;
        if (!StringUtils.isEmpty(partBodyAnn.value())){
            parameterName = partBodyAnn.value();
        } else {
            parameterName = parameter.getName();
        }

        if (jsonObject == null){
            if (partBodyAnn.required())
                throw new BusinessException("Required parameter " + parameterName + " is missing");
            return null;
        }

        Object object = jsonObject.get(parameterName);
        Object res;
        Class<?> claz = parameter.getType();
        if (claz.isPrimitive()){
            res = parsePrimitive(claz.getName(), object);
        } else {
            res = parameter.getType().cast(object);
        }
        // 判断是否是必传参数
        if (partBodyAnn.required() && res == null){
            throw new BusinessException("Required parameter " + parameterName + " is missing");
        }
        return res;
    }

    /**
     * 解析基础数据类型
     * @Author zhaolianqi
     * @Date 2020/12/24 10:49
     */
    private Object parsePrimitive(String primitiveName, Object object){
        if (primitiveName.equalsIgnoreCase("int")){
            return Integer.parseInt(object.toString());
        } else if (primitiveName.equalsIgnoreCase("double")){
            return Double.parseDouble(object.toString());
        } else if (primitiveName.equalsIgnoreCase("float")){
            return Float.parseFloat(object.toString());
        } else if (primitiveName.equalsIgnoreCase("long")){
            return Long.parseLong(object.toString());
        } else if (primitiveName.equalsIgnoreCase("char")){
            return object.toString().charAt(0);
        } else if (primitiveName.equalsIgnoreCase("short")){
            return Short.parseShort(object.toString());
        } else if (primitiveName.equalsIgnoreCase("byte")){
            return Byte.valueOf(object.toString());
        }
        return null;
    }

}
