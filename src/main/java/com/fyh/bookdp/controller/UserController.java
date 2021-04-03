package com.fyh.bookdp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.User;
import com.fyh.bookdp.service.CartService;
import com.fyh.bookdp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import java.io.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private CartService cartService;




    /**
     * 注册新用户
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String register(User user, Model model){
        boolean result = false;
        try {
            result = userService.save(user);
        } catch (Exception e) {
            model.addAttribute("error",user.getLoginName()+"已存在！请重新输入！");
            return "register";
        }
        if(result == true) {
            return "login";
        }
        else {
            return "register";
        }
    }

    /**
     * 登录
     * @param loginName
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    public String login(String loginName, String password,HttpSession session){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("login_name",loginName);
        wrapper.eq("password",password);
        User user = userService.getOne(wrapper);
        if(user == null){
            return "login";
        }else{
            session.setAttribute("user",user);
            return "redirect:/productCategory/list";
        }
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }


    /**
     * 用户信息
     */

    @GetMapping("/userInfo")
    public ModelAndView userInfo(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        modelAndView.setViewName("userInfo");
        modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        return modelAndView;
    }

//
    @PostMapping("/upload")
    public ModelAndView upload(MultipartFile img, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) session.getAttribute("user");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",user.getId());
        User one = userService.getOne(wrapper);
        String oldFile =System.getProperty("user.dir")+"/src/main/resources/static/images/"+one.getFileName();
        File path = new File(oldFile);
        path.delete();
        String name = img.getOriginalFilename();
        int i = name.lastIndexOf(".");
        String sub = name.substring(i);
        String filename = "user"+user.getId()+sub;
        String newFile =System.getProperty("user.dir")+"/src/main/resources/static/images/"+filename;
        try {
                //获取上传文件
                InputStream inputStream = img.getInputStream();
                //保存的路径
                FileOutputStream outputStream = new FileOutputStream(newFile);
                byte[] bytes = new byte[1024];
                while (inputStream.read(bytes) != -1){
                    outputStream.write(bytes);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        modelAndView.setViewName("userInfo");
        one.setFileName(filename);
        userService.updateById(one);
        modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        return modelAndView;
    }




}

