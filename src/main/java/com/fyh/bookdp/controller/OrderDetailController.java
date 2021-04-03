package com.fyh.bookdp.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.OrderDetail;
import com.fyh.bookdp.entity.Orders;
import com.fyh.bookdp.entity.Product;
import com.fyh.bookdp.entity.User;
import com.fyh.bookdp.service.CartService;
import com.fyh.bookdp.service.OrderDetailService;
import com.fyh.bookdp.service.OrderService;
import com.fyh.bookdp.service.ProductService;
import com.fyh.bookdp.vo.ProductListVO;
import com.fyh.bookdp.vo.ProductVO;
import com.fyh.bookdp.vo.UserBuyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Controller
@RequestMapping("/orderDetail")
public class OrderDetailController {


    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;


    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductService productService;

    @GetMapping("/findUserBuy")
    public ModelAndView findUserBuy(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("orderList");
        List<UserBuyVO> list = new ArrayList();
        User user = (User) session.getAttribute("user");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",user.getId());
        List<Orders> ordersList = orderService.list(wrapper);
        for (Orders orders : ordersList) {
            List<ProductListVO> productList = new ArrayList();
            wrapper = new QueryWrapper();
            wrapper.eq("order_id",orders.getId());
            List<OrderDetail> orderServiceList = orderDetailService.list(wrapper);
            for (OrderDetail orderDetail : orderServiceList) {
                wrapper = new QueryWrapper();
                wrapper.eq("id",orderDetail.getProductId());
                Product product = productService.getOne(wrapper);
                ProductListVO productListVO = new ProductListVO(product.getName(),product.getFileName(),orderDetail.getQuantity(),orderDetail.getCost());
                productList.add(productListVO);
            }
            UserBuyVO userBuyVO = new UserBuyVO(orders.getId(),orders.getLoginName(),orders.getUserAddress(),orders.getCost(),orders.getSerialnumber(),orders.getState(),orders.getPay(),productList);
            list.add(userBuyVO);
        }
        modelAndView.addObject("list",list);
        modelAndView.addObject("cartList",cartService.findAllCartVOByUserId(user.getId()));

        return modelAndView;
    }
}



