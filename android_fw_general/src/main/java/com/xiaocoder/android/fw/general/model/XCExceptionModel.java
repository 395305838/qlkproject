package com.xiaocoder.android.fw.general.model;

import java.io.Serializable;

/**
 * @author xiaocoder
 *         异常信息
 *         2014-12-9 下午7:13:53
 */
public class XCExceptionModel implements Serializable {

    private static final long serialVersionUID = 7806487539561621886L;

    /**
     * 异常的详细信息
     */
    String info = "";
    /**
     * 异常发生的时间
     */
    String exceptionTime = "";
    /**
     * 是否上传成功 0 UPLOAD_NO 为没上传或上传失败，1 UPLOAD_YES 为上传成功
     */
    String uploadSuccess = "";
    /**
     * 哪一个用户
     */
    String userId = "";
    /**
     * 唯一标识 = exceptionTime + "_" + uuid
     */
    String uniqueId = "";

    public static final String UPLOAD_YES = "1";

    public static final String UPLOAD_NO = "0";

    @Override
    public String toString() {
        return "XCExceptionModel{" +
                "info='" + info + '\'' +
                ", exceptionTime='" + exceptionTime + '\'' +
                ", uploadSuccess='" + uploadSuccess + '\'' +
                ", userId='" + userId + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                '}';
    }

    public XCExceptionModel(String info, String exceptionTime, String uploadSuccess, String userId, String uniqueId) {
        this.info = info;
        this.exceptionTime = exceptionTime;
        this.uploadSuccess = uploadSuccess;
        this.userId = userId;
        this.uniqueId = uniqueId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getExceptionTime() {
        return exceptionTime;
    }

    public void setExceptionTime(String exceptionTime) {
        this.exceptionTime = exceptionTime;
    }

    public String getUploadSuccess() {
        return uploadSuccess;
    }

    public void setUploadSuccess(String uploadSuccess) {
        this.uploadSuccess = uploadSuccess;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
