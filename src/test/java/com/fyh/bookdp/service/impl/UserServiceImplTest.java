package com.fyh.bookdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void text(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("login_name","cgn");
        wrapper.eq("password","123");
        System.out.println(userService.getOne(wrapper));
    }
}