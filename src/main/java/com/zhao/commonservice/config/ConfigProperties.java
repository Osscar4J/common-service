package com.zhao.commonservice.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigProperties {

    private static Map<String, Object> propertyMap = new HashMap<>(8);

    private ConfigProperties(){}

    static {
        Properties properties = new Properties();
        try {
            InputStream in = new FileInputStream(new File("config.properties"));
            // 使用properties对象加载输入流
            properties.load(in);
            //获取key对应的value值
            properties.getProperty("String key");
            Enumeration<Object> enumeration = properties.keys();
            while (enumeration.hasMoreElements()){
                String key = (String) enumeration.nextElement();
                propertyMap.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object getProperty(String key){
        return propertyMap.get(key);
    }

}
