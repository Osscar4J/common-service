package com.zhao.commonservice.api;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zhao.common.exception.BusinessException;
import com.zhao.common.respvo.BaseResponse;
import com.zhao.commonservice.annotations.Auth;
import com.zhao.commonservice.annotations.LoginRequired;
import com.zhao.commonservice.annotations.SysLog;
import com.zhao.commonservice.oss.AliOSSConfig;
import com.zhao.commonservice.oss.modal.OSSModel;
import com.zhao.commonservice.reqvo.FileUploadReqVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

/**
 * 文件
 * @Author: zhaolianqi
 * @Date: 2020/11/6 14:20
 * @Version: v1.0
 */
@Auth(id = 10200, name = "文件管理")
@LoginRequired
@RestController
@RequestMapping("/api/file")
public class FileApi {

    private String roleSessionName = "promanager";
    private String tempFilePath = "F:/videos/temp/";

    private Logger logger = LoggerFactory.getLogger(FileApi.class);

//    private String policy = "{\n" +
//            "    \"Statement\": [\n" +
//            "        {\n" +
//            "            \"Action\": \"sts:AssumeRole\",\n" +
//            "            \"Effect\": \"Allow\",\n" +
//            "            \"Resource\": \"*\"\n" +
//            "        }\n" +
//            "    ],\n" +
//            "    \"Version\": \"1\"\n" +
//            "}";

    /**
     * sts临时授权
     * @return
     */
    @SysLog("获取了STS临时授权")
    @GetMapping("/sts")
    public OSSModel sts() {
        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", AliOSSConfig.STS_SERVER);
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", AliOSSConfig.ACCESS_KEY, AliOSSConfig.ACCESS_SECRET);
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(AliOSSConfig.ROLE_ARN);
            request.setRoleSessionName(roleSessionName);
//            request.setPolicy(policy); // 不设置policy，默认拥有指定角色的所有权限
            final AssumeRoleResponse response = client.getAcsResponse(request);
            AssumeRoleResponse.Credentials credentials = response.getCredentials();
            OSSModel ossModel = new OSSModel();
            ossModel.setAccessKey(credentials.getAccessKeyId());
            ossModel.setAccessKeySecret(credentials.getAccessKeySecret());
            ossModel.setSecurityToken(credentials.getSecurityToken());
            ossModel.setBucket(AliOSSConfig.BUCKET);
            ossModel.setEndPoint(AliOSSConfig.END_POINT);
            ossModel.setRegion(AliOSSConfig.REGION);
            ossModel.setExpiration(credentials.getExpiration());
            return ossModel;
        } catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 获取访问文件的60秒授权
     * @param file 文件名（相对于bucket根目录的完整文件名）
     * @param cover 传任意值取视频的第一帧，jpg图片
     * @return
     */
    @SysLog(value="获取了文件的临时授权")
    @GetMapping("/auth")
    public String getSign(String file, boolean cover) {
        OSSClient ossClient = new OSSClient(AliOSSConfig.END_POINT, AliOSSConfig.ACCESS_KEY, AliOSSConfig.ACCESS_SECRET);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 60);
//		URL url = ossClient.generatePresignedUrl(AliOSSConfig.BUCKET, file, c.getTime(), HttpMethod.GET);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(AliOSSConfig.BUCKET, file);
        request.setExpiration(c.getTime());
        // 取视频第一帧
        if (cover) {
            request.setProcess("video/snapshot,t_0,f_jpg");
        }
        URL url = ossClient.generatePresignedUrl(request);
        ossClient.shutdown();
        return url.toString();
    }

    /**
     * 通过文件的MD5去查找该文件是否已上传过，
     * 如果已经上传过，那么检查是否已经上传完成，如果没上传完，则返回
     * @Author zhaolianqi
     *
     * @return
     * @throws
     * @Date 2020/11/20 17:28
     */
    @PostMapping("/checkShard")
    public Integer checkFileShardIndex(@RequestBody FileUploadReqVO reqVO){
        int shardIndex = 0;
        String fileMd5 = reqVO.getMd5();
        // TODO 通过文件的MD5值查该文件是否上传过或是否上传完，
        // TODO 如果没有上传完，则返回最后上传的分片index

        return shardIndex;
    }

    /**
     * 文件分片上传
     * @Author zhaolianqi
     * @Date 2020/11/20 15:30
     */
    @PostMapping("/multipartUpload")
    public BaseResponse<Object> multipartUpload(FileUploadReqVO reqVO, MultipartFile file) throws IOException {
        if (StringUtils.isEmpty(reqVO.getMd5()))
            return null;
        if (StringUtils.isEmpty(reqVO.getFileName()))
            reqVO.setFileName(file.getOriginalFilename());
        String md5 = reqVO.getMd5();
        // 这个目录做成可配置的
        String targetPath = tempFilePath + md5 + "/";
        File destFile = new File(targetPath + reqVO.getFileName() + "-" + reqVO.getCurrShard());
        file.transferTo(destFile);
        // TODO redis保存当前进度

        // 分片已全部上传完成，进行合并
        if (reqVO.getCurrShard() == reqVO.getTotalShard()){
            File newFile = new File(targetPath + reqVO.getFileName());
            if (newFile.exists())
                newFile.delete();
            FileOutputStream outputStream = new FileOutputStream(newFile, true);
            FileInputStream fileInputStream = null;
            byte[] byt = new byte[10 * 1024 * 1024];
            int len;
            try {
                for (int i = 0; i < reqVO.getTotalShard(); i++){
                    fileInputStream = new FileInputStream(new File(targetPath + reqVO.getFileName() + "-" + (i + 1)));
                    while ((len = fileInputStream.read(byt)) != -1) {
                        outputStream.write(byt, 0, len);
                    }
                    fileInputStream.close();
                }
                // 删除临时文件
                for (int i = 0; i < reqVO.getTotalShard(); i++){
                    File tempFile = new File(targetPath + reqVO.getFileName() + "-" + (i + 1));
                    if (tempFile.exists())
                        tempFile.delete();
                }
                fileInputStream.close();
                fileInputStream = null;
                if (newFile.exists())
                    newFile.delete();
                File dir = new File(targetPath);
                if (dir.exists())
                    dir.delete();
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
            }
        }
        return BaseResponse.SUCCESS();
    }

}
