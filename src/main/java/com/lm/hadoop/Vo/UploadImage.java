package com.lm.hadoop.Vo;

// netdisk类
public class UploadImage {
    public String userId;

    public String getUserId() {
        NetDiskUser netDiskUser = new NetDiskUser();
        return netDiskUser.getUserId();
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
