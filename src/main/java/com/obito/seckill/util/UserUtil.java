package com.obito.seckill.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obito.seckill.pojo.User;
import com.obito.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {

    private static void createUser(int count) throws SQLException, ClassNotFoundException, IOException {
        List<User> users = new ArrayList<User>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(1300000000L + i);
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            user.setLoginCount(1);
            user.setRegisterData(new Date());
            users.add(user);
        }
        //插入数据库
        Connection connection = getConnection();
        String sql = "insert into t_user(login_count, nickname, register_date, salt, password, id) values(?, ? ,?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            preparedStatement.setInt(1, user.getLoginCount());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setTimestamp(3, new Timestamp(user.getRegisterData().getTime()));
            preparedStatement.setString(4, user.getSalt());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getId());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.clearParameters();
        connection.close();
        //登录 获取令牌
        String urlString = "http://localhost:8080/login/toLogin";
        File file = new File("C:\\Users\\Administrator\\config.txt");
        if (file.exists()) {
            file.delete();
        } else {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(0);
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                URL url = new URL(urlString);
                HttpURLConnection co = (HttpURLConnection) url.openConnection();
                co.setRequestMethod("POST");
                co.setDoOutput(true);
                OutputStream outputStream = co.getOutputStream();
                String params = "mobiles" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
                outputStream.write(params.getBytes());
                outputStream.flush();
                InputStream inputStream = co.getInputStream();
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buff)) >= 0) {
                    bout.write(buff, 0, len);
                }
                inputStream.close();
                bout.close();
                String response = new String(bout.toByteArray());
                ObjectMapper mapper = new ObjectMapper();
                RespBean respBean = mapper.readValue(response, RespBean.class);
                String userTicket = (String) respBean.getObj();
                System.out.println("create userTicket :" + user.getId());
                String row = user.getId() + "," + userTicket;
                raf.seek(raf.length());
                raf.write(row.getBytes());
                raf.write("\r\n".getBytes());
            }
            raf.close();
        }
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String username = "root";
        String password = "root";
        String driver = "com.mysql.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);

    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        createUser(5000);
    }
}
