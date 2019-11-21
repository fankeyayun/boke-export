package com.example.export.modu.controller;


import com.example.export.modu.dao.UserDao;
import com.example.export.modu.entity.User;
import com.example.export.utils.ExcelUtil;
import com.example.export.utils.ObjectValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/user/exp")
public class UserExcelController {

    @Autowired
    UserDao userDao;



    @GetMapping(value="/get")
    public User user() throws Exception {
        User user1 = new User();
        System.out.println("类的路径:"+User.class.toString()+"/////:"+User.class.getAnnotations());

        Object r = ObjectValueUtil.getObjectValue(user1);
        System.out.println("打印object>>>>>"+r.toString());


       User user= userDao.findById(1);
       return user;
    }

    @GetMapping(value = "/exportUser")
    public void export(HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        ExcelUtil.Excel();
        //原理就是将所有的数据一起写入，然后再关闭输入流。
        setResponseHeader(response, "个人基础信息模板.xls");
    }
    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
