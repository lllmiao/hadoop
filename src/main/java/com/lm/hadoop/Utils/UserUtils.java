package com.lm.hadoop.Utils;

import com.lm.hadoop.Vo.NetDiskVO;

import java.util.UUID;

public class UserUtils {

    public static NetDiskVO createNetDisk(String userId) {
        NetDiskVO netDiskVO = new NetDiskVO();
        netDiskVO.setId(UUID.randomUUID().toString().replace("-", ""));
        netDiskVO.setUserId(userId);
        netDiskVO.setSpace(10240D);

        return netDiskVO;
    }
}
