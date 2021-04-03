package com.fyh.bookdp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.User;
import com.fyh.bookdp.service.CartService;
import com.fyh.bookdp.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Controller
@RequestMapping("/userAddress")
public class UserAddressController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserAddressService userAddressService;

    @GetMapping("/list")
    public ModelAndView list(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userAddressList");
        User user = (User)session.getAttribute("user");
        modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",user.getId());
        modelAndView.addObject("addressList",userAddressService.list(wrapper));
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        userAddressService.removeById(id);
        return "redirect:/userAddress/list";
    }
}

