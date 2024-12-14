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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.common.entity.UserInfo;
import com.zhao.common.exception.BusinessException;
import com.zhao.common.respvo.BaseResponse;
import com.zhao.common.respvo.ResponseStatus;
import com.zhao.common.utils.FileUtil;
import com.zhao.commonservice.annotations.Auth;
import com.zhao.commonservice.annotations.CurrentUser;
import com.zhao.commonservice.annotations.LoginRequired;
import com.zhao.commonservice.annotations.SysLog;
import com.zhao.commonservice.config.ConfigProperties;
import com.zhao.commonservice.entity.MyFile;
import com.zhao.commonservice.oss.AliOSSConfig;
import com.zhao.commonservice.oss.modal.OSSModel;
import com.zhao.commonservice.reqvo.FileUploadReqVO;
import com.zhao.commonservice.service.CacheService;
import com.zhao.commonservice.service.MyFileService;
import com.zhao.commonservice.upload.FileUploaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    private String roleSessionName = (String) ConfigProperties.getProperty("oss-role-session-name");
    private String uploadPath = (String) ConfigProperties.getProperty("upload-path");
    @Autowired
    private MyFileService myFileService;
    @Autowired
    private CacheService cacheService;
    // 文件分片上传缓存key
    private final String uploadMapKey = "file-upload";

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
    public BaseResponse<?> checkFileShardIndex(@RequestBody FileUploadReqVO reqVO){
        String fileMd5 = reqVO.getMd5();
        if (StringUtils.isEmpty(fileMd5))
            throw new BusinessException(ResponseStatus.FILE_INVALIDATE);
        // 如果文件已存在，直接返回
        MyFile file = myFileService.getOne(
                new QueryWrapper<MyFile>()
                        .eq("file_md5", fileMd5)
                        .eq("status", MyFileService.Status.SUCCESS)
        );
        if (file != null)
            return BaseResponse.SUCCESS(file);
        Integer shardIndex = (Integer) cacheService.getFromMapCache(uploadMapKey, fileMd5);
        if (shardIndex == null)
            shardIndex = 0;
        return BaseResponse.SUCCESS(shardIndex);
    }

    /**
     * 文件分片上传
     * @Author zhaolianqi
     * @Date 2020/11/20 15:30
     */
    @PostMapping("/multipartUpload")
    public BaseResponse<Object> multipartUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("md5") String md5,
            @RequestParam("filename") String filename,
            @RequestParam("totalShard") Integer totalShard,
            @RequestParam("currShard") Integer currShard,
            @CurrentUser UserInfo user) throws IOException {
        if (StringUtils.isEmpty(md5))
            throw new BusinessException("md5不能为空");
        if (StringUtils.isEmpty(filename))
            filename = file.getOriginalFilename();
        String targetPath = uploadPath + "temp/" + user.getId() + "/" + md5 + "/";
        File targetPathDir = new File(targetPath);
        if (!targetPathDir.exists())
            targetPathDir.mkdirs();
        File destFile = new File(targetPath + file.getOriginalFilename() + "-" + currShard);
        file.transferTo(destFile);
        String res = null;

        // 保存文件
        if (currShard == 1){
            MyFile myFile = new MyFile();
            myFile.setFileMd5(md5);
            myFile.setFormat(filename.substring(filename.lastIndexOf(".")));
            myFile.setName(filename);
            myFile.setUserId(user.getId());
            myFile.setStatus(MyFileService.Status.UPLOADING);
            myFileService.save(myFile);
        }

        // 分片已全部上传完成，进行合并
        if (currShard == totalShard){
            File newFile = new File(targetPath + file.getOriginalFilename());
            if (newFile.exists())
                newFile.delete();
            for (int i = 0; i < totalShard; i++){
                FileUtil.transferTo(new File(targetPath + file.getOriginalFilename() + "-" + (i + 1)), newFile, true, true);
            }
            FileUploaderFactory.getUploader().upload(newFile, uploadPath + filename);
            res = "/images/" + filename;
            // 移除缓存
            cacheService.removeMapCache(uploadMapKey, md5);
            // 更新文件信息
            MyFile myFile = new MyFile();
            myFile.setFileMd5(md5);
            myFile.setStatus(MyFileService.Status.SUCCESS);
            myFile.setUrl(res);
            File target = new File(uploadPath + filename);
            myFile.setFileSize((int) target.length());
            myFile.setUrl(res);
            myFileService.updateByMd5(myFile);
            // 获取图片的主色调、宽高
            if (filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
                myFileService.updateRGBInfo(myFile, target);
        } else {
            // redis保存当前进度+1
            cacheService.incrementNumber(uploadMapKey, md5);
        }
        return BaseResponse.SUCCESS(res);
    }

}
