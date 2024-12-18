package com.zhao.commonservice.oss;

public class AliOSSConfig {

    public static String BUCKET = null;
    public static String ACCESS_KEY = null;
    public static String ACCESS_SECRET = null;
    public final static String STS_SERVER = "sts.aliyuncs.com";
    public static String END_POINT = null;
    public static String REGION = null;
    /**oss域名*/
    public static String OSS_URL = null;
    /**STS有效时长，单位：秒*/
    public final static int STS_EXPIRED = 3600;
    public static String ROLE_ARN = null;

    public static void initConfig(String bucket, String key, String secret, String endPoint, String region, String roleArn){
        BUCKET = bucket;
        ACCESS_KEY = key;
        ACCESS_SECRET = secret;
        END_POINT = endPoint;
        if (END_POINT != null)
            OSS_URL = END_POINT.replace("https://", "https://" + BUCKET + ".");

        REGION = region;
        ROLE_ARN = roleArn;
    }

}
