package com.bnuz.controller;

import com.bnuz.pojo.Admin;
import com.bnuz.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminAction {

    //在所以控制层中，一定会有业务逻辑层对象
    @Autowired//自动装配
    AdminService adminService;

    @RequestMapping(value = "/login")
    public String login(String name, String pwd, HttpServletRequest request){
        Admin admin = adminService.login(name, pwd);
        if (admin != null){
            request.setAttribute("errmsg", "用户名或密码不正常");
            return "login";
        }
        request.setAttribute("admin", admin);
        return "main";
    }

}
