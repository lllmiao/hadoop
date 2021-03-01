package com.lm.hadoop.Vo;
//定义网盘返回类
public class UploadImageResult {
    public String image;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public UploadImageResult() {
    }
    public UploadImageResult(String image) {
        this.image = image;
    }
}
