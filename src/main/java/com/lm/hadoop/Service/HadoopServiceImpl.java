package com.lm.hadoop.Service;

import com.lm.hadoop.Utils.UserUtils;
import com.lm.hadoop.Vo.NetDiskUser;
import com.lm.hadoop.Vo.NetDiskUserResult;
import com.lm.hadoop.Vo.UploadImageResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// 业务层接口实现类:处理制层传过来的数据
@Service
public class HadoopServiceImpl implements HadoopService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    //存储用户openid，用于搞路径
    String userid = "";

    //插入每个用户网盘
    @Override
    public NetDiskUserResult get(String openId) throws URISyntaxException, IOException {
        //提供hadoop中的配置信息
        Configuration conf = new Configuration();
        //根据conf获取具体的文件系统对象
        FileSystem fs, fsconf;

        //new 了一个用户的网盘id和space
        NetDiskUser netDiskVO = UserUtils.createNetDisk(openId);
        userid = netDiskVO.getUserId();

        //判断sql中是否有该id
        String idsql = "SELECT count(*) FROM netdisk_table where userid = ?";
        Integer count = jdbcTemplate.queryForObject(idsql,new Object[]{netDiskVO.getUserId()},Integer.class);
        if (1 == count){
            //后续需要在这里设置减去文件大小后，获取到的空间变化大小
            System.out.println(count);
        }
        //用户id不存在
        else
        {
            String sql = "insert into netdisk_table(userid, allspace,usedspace,freespace) values (?,?,?,?)";
            // 通过update修改数据,参数第一是sql语句，第二是对应sql？的内容
            int successCount = jdbcTemplate.update(sql, netDiskVO.getUserId(), netDiskVO.getAllSpace(), netDiskVO.getUsedSpace(), netDiskVO.getFreeSpace());
            //成功返回1
            if (1 == successCount) {
                //获取文件系统
                fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
                fsconf = FileSystem.get(conf);
                Path path = new Path("/" + netDiskVO.getUserId());
                //判断用户文件是否存在
                if (fsconf.exists(path)) {
                    System.out.println("用户已存在");
                } else {
                    fs.mkdirs(path);
                }
                fs.close();
                return new NetDiskUserResult(netDiskVO.getAllSpace(), netDiskVO.getUsedSpace(), netDiskVO.getFreeSpace());
            }
        }
        return new NetDiskUserResult(null, null, null);
    }

    //上传图片
    @Override
    public UploadImageResult image(MultipartFile[] imagePath, HttpServletRequest request) throws URISyntaxException, IOException {
        //MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        //MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //MultipartHttpServletRequest req = resolver.resolveMultipart(request);
        //MultipartFile file = req.getFile("imageName");

        //提供hadoop中的配置信息
        Configuration conf = new Configuration();
        //根据conf获取具体的文件系统对象
        FileSystem fs;

        fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
        //规定路径
        Path dst = new Path("/" + userid +"/images/" + imagePath[0].getOriginalFilename());
        System.out.println("图片路径："+dst);
        //获取文件大小
        Long size = fs.getContentSummary(new Path("hdfs://172.0.0.1:9000" +dst)).getLength();

        //判断用户文件是否存在(?)
        if (fs.exists(dst)) {
            System.out.println("图片已存在");
        } else {
            //打开新文件写
            FSDataOutputStream os = fs.create(dst);
            IOUtils.copy(imagePath[0].getInputStream(), os);

//            NetDiskUser netDiskUser = new NetDiskUser();
//            BigDecimal b1 = new BigDecimal(netDiskUser.getAllSpace().toString());
//            BigDecimal b2 = new BigDecimal(v2.toString());
//            return new Double(b1.subtract(b2).doubleValue());
//            netDiskUser.setAllSpace(().subtract());

            System.out.println("上传成功");
        }
        fs.close();
        return new UploadImageResult(String.valueOf(size));
    }
}
