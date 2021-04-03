package com.fyh.bookdp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.Orders;
import com.fyh.bookdp.entity.User;
import com.fyh.bookdp.mapper.OrderMapper;
import com.fyh.bookdp.service.CartService;
import com.fyh.bookdp.service.OrderDetailService;
import com.fyh.bookdp.service.OrderService;
import com.fyh.bookdp.service.ProductCategoryService;
import com.fyh.bookdp.utli.QRCodeGenenator;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/settlement3")
    public ModelAndView settlement3(Orders orders, HttpSession session,String address,String remark){
        if (!address.equals("")){
            User user = (User)session.getAttribute("user");
            orderService.save(orders,user,address,remark);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("test");
            modelAndView.addObject("orders",orders);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("order_id",orders.getId());
            List list = orderDetailService.list(wrapper);
            int size = list.size();
            if (size == 0){
                orderService.removeById(orders.getId());
                modelAndView.setViewName("redirect:/productCategory/list");
                return modelAndView;
            }else {
                Random r = new Random();
                int i = r.nextInt(10000);
                String s = String.valueOf(i);
                Integer filname = orders.getId();
                String file =System.getProperty("user.dir")+"/src/main/resources/static/images/userBuy"+filname+".png";
                QRCodeGenenator.generaQR(s,350,350,file);
                modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
                if (user == null){
                    modelAndView.addObject("cartList",new ArrayList<>());
                }else {
                    modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
                }
                return modelAndView;
            }
        }else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/cart/settlement2");
            return modelAndView;
        }
    }


    @PostMapping("/money")
    public ModelAndView money(HttpServletRequest request,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        User user = (User)session.getAttribute("user");
        MultiFormatReader formatReader = new MultiFormatReader();
        Result result = null;
        String ordersId = request.getParameter("ordersId");
        String path =System.getProperty("user.dir")+"/src/main/resources/static/images/userBuy"+ordersId+".png";
        File file = new File(path);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            HashMap hashMap = new HashMap();
            hashMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            result = formatReader.decode(binaryBitmap, hashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String text = result.getText();
        String payCode = request.getParameter("payCode");
        if (payCode.equals(text)){
            modelAndView.setViewName("settlement3");
            modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("id",ordersId);
            modelAndView.addObject("orders",orderService.getOne(wrapper));
            Orders ordersOne = orderService.getOne(wrapper);
            ordersOne.setPay(1);
            orderMapper.updateById(ordersOne);
            file.delete();
            return modelAndView;
        }
        else {
            modelAndView.setViewName("redirect:/orderDetail/findUserBuy");
            modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
            if (user == null){
                modelAndView.addObject("cartList",new ArrayList<>());
            }else {
                modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));
            }
            return modelAndView;
        }
    }

    @GetMapping("/pay/{id}")
    public ModelAndView pay(@PathVariable("id") Integer id,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",id);
        modelAndView.setViewName("test");
        modelAndView.addObject("orders",orderService.getOne(wrapper));
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

