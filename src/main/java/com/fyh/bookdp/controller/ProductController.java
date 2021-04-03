package com.fyh.bookdp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.User;
import com.fyh.bookdp.service.CartService;
import com.fyh.bookdp.service.ProductCategoryService;
import com.fyh.bookdp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list/{type}/{id}")
    public ModelAndView list(@PathVariable("type") String type ,
                             @PathVariable("id") Integer id,
                             HttpSession session){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productList");
        modelAndView.addObject("productList",productService.findByCategoryId(type,id));
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        User user = (User) session.getAttribute("user");
        if (user == null){
            modelAndView.addObject("cartList",new ArrayList<>());
        }else {
            modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        }
        return modelAndView;
    }

    @GetMapping("/findById/{id}")
    public ModelAndView findById(@PathVariable("id") Integer id,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productDetail");
        modelAndView.addObject("product",productService.getById(id));
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        User user = (User) session.getAttribute("user");
        if (user == null){
            modelAndView.addObject("cartList",new ArrayList<>());
        }else {
            modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        }
        return modelAndView;
    }

    @PostMapping("/findLike")
    public ModelAndView findLike(String keyWord,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productList");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("name",keyWord);
        wrapper.eq("state",0);
        modelAndView.addObject("productList",productService.list(wrapper));
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        User user = (User) session.getAttribute("user");
        if (user == null){
            modelAndView.addObject("cartList",new ArrayList<>());
        }else {
            modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
        }
        return modelAndView;
    }
}

