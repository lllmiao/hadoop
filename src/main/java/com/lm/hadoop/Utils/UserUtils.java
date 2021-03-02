package com.lm.hadoop.Utils;

import com.lm.hadoop.Vo.NetDiskUser;
import com.lm.hadoop.Vo.Operate;

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

    public static Operate subtract(Double allspace, long filesize) {
        Operate operate = new Operate();
        operate.subtract(allspace, filesize);
        return operate;
    }

    public static Operate add(Double usedspace, long filesize) {
        Operate operate = new Operate();
        operate.add(usedspace, filesize);
        return operate;
    }

}
