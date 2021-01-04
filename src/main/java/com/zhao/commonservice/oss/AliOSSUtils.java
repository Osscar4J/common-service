package com.zhao.commonservice.oss;

import com.aliyun.oss.OSSClient;
import com.zhao.common.exception.BusinessException;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

public class AliOSSUtils {

	public static String stsEncoder(String params) {
		try {
			return URLEncoder.encode(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(500, e.getMessage());
		}
	}
	
	/**
	 * 获取文件的临时授权访问
	 * @param filePath 文件路径，相对于bucket的根路径
	 * @param expiredate 过期时间
	 * @return
	 */
	public static URL sign(String filePath, Date expiredate) {
		OSSClient client = new OSSClient(AliOSSConfig.END_POINT, AliOSSConfig.ACCESS_KEY, AliOSSConfig.ACCESS_SECRET);
		return client.generatePresignedUrl(AliOSSConfig.BUCKET, filePath, expiredate);
	}
	
	/**
	 * 获取60秒临时授权
	 * @param filePath 文件路径
	 * @return
	 */
	public static URL sign(String filePath) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 60);
		return sign(filePath, c.getTime());
	}
	
}
