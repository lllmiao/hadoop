package com.lm.hadoop.Utils;

import com.lm.hadoop.Vo.NetDiskUser;

//工具类
public class UserUtils {

    public static NetDiskUser createNetDisk(String userId) {
        NetDiskUser netDiskVO = new NetDiskUser();
        netDiskVO.setUserId(userId);
        netDiskVO.setAllSpace(5120.00);
        netDiskVO.setUsedSpace(0.00);
        netDiskVO.setFreeSpace(5120.00);
        return netDiskVO;
    }
}
