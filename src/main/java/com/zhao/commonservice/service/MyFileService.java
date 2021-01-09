package com.zhao.commonservice.service;

import com.zhao.commonservice.entity.MyFile;

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

}
