package com.lm.hadoop.Service;

import com.lm.hadoop.Vo.NetDiskUserResult;
import com.lm.hadoop.Vo.UploadImageResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

// 业务层接口: 处理控制层传过来的数据
public interface HadoopService {
    NetDiskUserResult get(String openId) throws URISyntaxException, IOException;
    UploadImageResult image(MultipartFile[] imagePath, HttpServletRequest request) throws URISyntaxException, IOException;
}
