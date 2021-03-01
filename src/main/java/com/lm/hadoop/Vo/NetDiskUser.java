package com.lm.hadoop.Vo;

// netdisk类
public class NetDiskUser {
    public String userId;
    public Double allSpace;
    public Double usedSpace;
    public Double freeSpace;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAllSpace() {
        return allSpace;
    }
    public void setAllSpace(Double allSpace) {
        this.allSpace = allSpace;
    }

    public Double getUsedSpace() {
        return usedSpace;
    }
    public void setUsedSpace(Double usedSpace) {
        this.usedSpace = usedSpace;
    }

    public Double getFreeSpace() {
        return freeSpace;
    }
    public void setFreeSpace(Double freeSpace) {
        this.freeSpace = freeSpace;
    }
}
