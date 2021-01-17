package com.zhao.commonservice.service;

import com.zhao.commonservice.entity.MyFile;

import java.io.File;

public interface MyFileService extends BaseService<MyFile> {

    /**
     * 文件状态
     */
    interface Status {
        /** 上传完成 */
        int SUCCESS = 1;
        /** 正在上传 */
        int UPLOADING = 2;
    }

    boolean updateByMd5(MyFile file);

    /**
     * 获取图片的主色调信息，并更新数据
     * @param file
     */
    void updateRGBInfo(MyFile file, File imageFile);

}
