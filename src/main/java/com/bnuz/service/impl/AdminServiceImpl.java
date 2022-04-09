package com.bnuz.service.impl;

import com.bnuz.mapper.AdminMapper;
import com.bnuz.pojo.Admin;
import com.bnuz.pojo.AdminExample;
import com.bnuz.service.AdminService;
import com.bnuz.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    //在业务逻辑层，一定会有数据访问层对象
    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {
        AdminExample adminExample = new AdminExample();
        //添加用户名a_name的条件
        adminExample.createCriteria().andANameEqualTo(name);
        List<Admin> list = adminMapper.selectByExample(adminExample);
        if (list.size() > 0){
            Admin admin = list.get(0);
            String miPwd = MD5Util.getMD5(pwd);
            System.out.println(pwd);
            System.out.println(miPwd);
            System.out.println(admin.getaPass());
            if (!admin.getaPass().equals(miPwd)){
                return admin;
            }
        }
        return null;
    }

}
