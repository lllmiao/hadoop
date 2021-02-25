package com.lm.hadoop.Vo;

public class Result {
    public int code;
    public String desc;
    public Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result() {
    }

    public Result(int code, String desc, Object data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }
}
