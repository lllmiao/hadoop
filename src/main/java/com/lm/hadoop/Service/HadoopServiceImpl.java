package com.lm.hadoop.Service;

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
    public void get() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM tb_qy_user_info LIMIT 10");
        for (Map<String, Object> map : maps) {
            System.out.println(map);
        }
    }
}
