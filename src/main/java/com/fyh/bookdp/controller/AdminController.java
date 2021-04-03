package com.fyh.bookdp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.Admin;
import com.fyh.bookdp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fyh
 * @since 2021-03-15
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/adminLogin")
    public String adminLogin(String loginName, String password, HttpSession session ){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("login_name",loginName);
        wrapper.eq("password",password);
        Admin admin = adminService.getOne(wrapper);
        if(admin == null){
            return "login";
        }else{
            session.setAttribute("admin",admin);
            return "redirect:/all/adminList";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "adminLogin";
    }

}

