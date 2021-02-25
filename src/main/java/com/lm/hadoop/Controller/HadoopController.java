package com.lm.hadoop.Controller;

import com.lm.hadoop.Service.HadoopServiceImpl;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Driver;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
//默认返回JSON格式
@ResponseBody
public class HadoopController {
    @Autowired
    HadoopServiceImpl hadoopService;
    JdbcTemplate jdbcTemplate;
    //创建 Spring 的 IOC 容器
    //ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    public void getConnection() throws IOException, ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        String driverClass = null;
        String jdbcUrl = null;
        String user = null;
        String password = null;
// 读取类路径下的jdbc.propertites 文件
        InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(in);
        driverClass = properties.getProperty("spring.datasource.driver-class-name");
        jdbcUrl = properties.getProperty("spring.datasource.url");
        user = properties.getProperty("spring.datasource.username");
        password = properties.getProperty("spring.datasource.password");
        Driver driver = (Driver) Class.forName(driverClass).newInstance();
        Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);
        Connection connection = driver.connect(jdbcUrl, info);
        System.out.println(connection + "成功");
    }

    //提供hadoop中的配置信息
    static Configuration conf = new Configuration();
    //根据conf获取具体的文件系统对象
    static FileSystem fs;

    //DataSource dataSource;
    //设置用户总分配内存空间
    //static int max = 10;


    //创建用户库,produces需补上不然前端获取数据乱码
    @PostMapping(value = "/mkdir", produces = "text/html;charset=UTF-8")
    public String mkdirDirectory(@RequestBody String data) throws JSONException {

        System.out.println("------创建用户库中------");

        //获取客户端传送数据
        JSONObject jsonObject = JSONObject.fromObject(data);
        String userId = jsonObject.getString("openid"); //解析前端传来的json数据并获取值的的基本语句，得到后就可以直接使用了
        if (jsonObject == null || userId.equals("")) {
            return "{\"flag\":\"false\"}";
        }
        return "ss";

//        if (null == jdbcTemplate) {
//            //从 IOC 容器中获取 bean 的实例
//            jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
//        }
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        try {
//            //查询用户存在(存在就有空间)
//            System.out.println("------查询用户存在------");
//
//            String sql_selectspace = "SELECT space FROM netdisk_table where userid='" + userId + "'";
//            BigDecimal str = jdbcTemplate.queryForObject(sql_selectspace, BigDecimal.class);
//            String a = str.toString();
//            return a;
//        } catch (EmptyResultDataAccessException e){
//            //不存在：用户数据插入数据库
//            System.out.println("------正在将用户数据插入数据库------");
//            String sql = "INSERT INTO netdisk_table(userid,space) VALUES (" + userId + ",10)";
//            String mobile = (String) jdbcTemplate.queryForObject(sql, new Object[]{1}, String.class);
//            System.out.println("------用户库创建完成------");
//            return "------spring：用户库创建成功------";
//        }
    }
    //将文件上传到hdfs
    @PostMapping("/uploadFile")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

        System.out.println("------文件上传------");
        //HttpServletRequest转型为MultipartHttpServletRequest，就能非常方便地得到文件名和文件内容
        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        //获取文件
        MultipartFile file = req.getFile("filename");

        fs = FileSystem.get(conf);
        //getOriginalFilename 获取文件名
        Path dst = new Path("hdfs://localhost:9000/" + file.getOriginalFilename());
        FSDataOutputStream os = fs.create(dst);
        IOUtils.copy(file.getInputStream(),os);
        fs.close();
    }
    //将图片上传到hdfs
    @PostMapping("/uploadImage")
    public void uploadImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        System.out.println("------图片上传------");

        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        MultipartFile file = req.getFile("imageName");

        fs = FileSystem.get(conf);
        //规定路径
        Path dst = new Path("hdfs://localhost:9000/" + file.getOriginalFilename());
        //打开新文件写
        FSDataOutputStream os = fs.create(dst);
        IOUtils.copy(file.getInputStream(),os);
        fs.close();
    }

    //将hdfs上的文件下载
    @PostMapping("/download")
    public  void download() throws IOException {
        fs = FileSystem.get(conf);
        fs.copyToLocalFile(false, new Path("/t1.txt"), new Path("F:/hdfs下载的文件"), true);
        fs.close();
    }

    //创建文件夹
    //@PostMapping("/create")
   /*  public static void create() throws IOException, URISyntaxException, InterruptedException {

        System.out.println("------创建文件夹------");

        fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf, "hadoop1"); //获取文件系统
        Path path = new Path("/cjy");
        fs.mkdirs(path);

    }
    */
    //查看hdfs内的文件
    // @PostMapping("/list")
    /*public static void Getlist() throws IOException {
        fs = FileSystem.get(conf);
        Path path = new Path("/");
        FileStatus[] list = fs.listStatus(path);
        for (FileStatus f : list) {
            System.out.println(f);
        }
    }
    */
    //修改hdfs文件名称
    /* public static void update() throws IOException, URISyntaxException, InterruptedException {
        fs = FileSystem.get(new URI("hdfs://hadoop1:9000"), conf, "hadoop1");
        Path oldName = new Path("/newTest.txt");
        Path newName = new Path("/new-test1.txt");
        fs.rename(oldName, newName);
    }
*/
    //追加内容到文件
    /* public static void look() throws IOException, URISyntaxException, InterruptedException {
        fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf, "hadoop1");
        String countName = "hello hdfs";
        Path path = new Path("/t1.txt");
        byte[] bytes = countName.getBytes();
        FSDataOutputStream put = fs.append(path);
        put.write(bytes);
    }
     */
}
