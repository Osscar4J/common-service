package com.zhao.commonservice.oss.modal;

import java.io.Serializable;

/**
 * 云存储OSSmodel
 * @Author: zhaolianqi
 * @Date: 2020/11/12 15:31
 * @Version: v1.0
 */
public class OSSModel implements Serializable {

    private static final long serialVersionUID = 6296789854976723762L;
    private String accessKey;
    private String accessKeySecret;
    private String expiration;
    private String securityToken;
    private String region;
    private String bucket;
    private String endPoint;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
