package com.lm.hadoop.Vo;
//定义网盘返回类
public class NetDiskUserResult {
    public Double allSpace;
    public Double usedSpace;
    public Double freeSpace;

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
    public NetDiskUserResult() {
    }
    public NetDiskUserResult(Double allSpace, Double freeSpace, Double usedSpace) {
        this.allSpace = allSpace;
        this.usedSpace = usedSpace;
        this.freeSpace = freeSpace;
    }
}
