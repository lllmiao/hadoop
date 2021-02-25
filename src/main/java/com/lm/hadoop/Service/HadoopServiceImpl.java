package com.lm.hadoop.Service;

import com.lm.hadoop.Utils.UserUtils;
import com.lm.hadoop.Vo.NetDiskVO;
import com.lm.hadoop.Vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HadoopServiceImpl implements HadoopService{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Result get(String openId) {
     // todo  这里的表名需要加一下
        String sql = "insert into netdisk_table('id', 'userid', 'space') values (?,?,?)";
        NetDiskVO netDiskVO = UserUtils.createNetDisk(openId);
        int successCount = jdbcTemplate.update(sql, netDiskVO.getId(), netDiskVO.getUserId(), netDiskVO.getSpace());
        if (1 == successCount) {
            return new Result(0, "success", "插入成功");
        }
        return new Result(-1, "success", "插入失败");
    }
}
