package com.fyh.bookdp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.Orders;
import com.fyh.bookdp.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper mapper;

    @Test
    void test(){
        QueryWrapper<Orders> wrapper = new QueryWrapper();
        wrapper.eq("user_id",10);
        System.out.println(orderService.list(wrapper));
    }
}