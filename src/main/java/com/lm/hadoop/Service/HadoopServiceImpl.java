package com.lm.hadoop.Service;

import com.lm.hadoop.Utils.UserUtils;
import com.lm.hadoop.Vo.NetDiskUser;
import com.lm.hadoop.Vo.NetDiskUserResult;
import com.lm.hadoop.Vo.Operate;
import com.lm.hadoop.Vo.UploadImageResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    Double allspace,freespace,usedspace;

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
        allspace = netDiskVO.getAllSpace();
        freespace = netDiskVO.getFreeSpace();
        usedspace = netDiskVO.getUsedSpace();

        //判断sql中是否有该id
        String idsql = "SELECT count(*) FROM netdisk_table where userid = ?";
        Integer count = jdbcTemplate.queryForObject(idsql,new Object[]{netDiskVO.getUserId()},Integer.class);
        //用户存在就获取
        if (1 == count){
            //后续需要在这里设置减去文件大小后，获取到的空间变化大小
            String sql = "SELECT allspace,freespace,usedspace FROM netdisk_table where userid = ?";
            NetDiskUser netDiskUser = jdbcTemplate.queryForObject(sql,new Object[]{userid},NetDiskUser.class);
            //获取就赋值
            allspace = netDiskUser.getAllSpace();
            freespace = netDiskUser.getFreeSpace();
            usedspace = netDiskUser.getUsedSpace();

            return new NetDiskUserResult(allspace, freespace, usedspace);
        }
        //用户不存在
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
    public UploadImageResult image(MultipartFile[] imagePath, HttpServletRequest request) throws Exception {
        MultipartFile multipartFile = imagePath[0];
        if (null == multipartFile) {
            throw new NullPointerException("文件为空
        String originalFilename = multipartFile.getOriginalFilename();
        //获取文件大小（kb?）
        long fileSize = multipartFile.getSize();
        System.out.println("文件大小为：" + fileSize + "KB")/**/;

        //加上文件后的已用空间(used)
        Operate usedoperate=UserUtils.add(usedspace,fileSize);
        //减去文件后的剩余空间(free)
        Operate freeoperate = UserUtils.subtract(allspace,usedspace);

        // 这里需要算一下,目前的空间是否已经大于上限(5g)。也需要判断一下 fileSize + 原来的空间是否大于5g 是的话抛出异常
        if (  usedoperate.add < 0.00 ||  fileSize + freeoperate.subtract > 5120.00) {
            throw new Exception();
        }

        //提供hadoop中的配置信息
        Configuration conf = new Configuration();
        //根据conf获取具体的文件系统对象
        FileSystem fs;
        fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
        //规定路径
        Path dst = new Path("/" + userid + "/images/" + originalFilename);
        System.out.println("图片路径：" + dst);

        //打开新文件写
        FSDataOutputStream os = fs.create(dst);
        IOUtils.copy(multipartFile.getInputStream(), os);

        //更新数据库空间
        String sql = "upload netdisk_table set allspace=?,freespace=?,usedspace=? where userid=?";
        // 通过update修改数据,参数第一是sql语句，第二是对应sql？的内容
        int successCount = jdbcTemplate.update(sql,allspace,freeoperate.subtract, usedoperate.add,userid);
        if (1 == successCount){
            System.out.println("文件上传并更新成功");
        }else{
            System.out.println("文件上传但更新失败");
        }
        fs.close();
        return new UploadImageResult("还好");
    }
}
