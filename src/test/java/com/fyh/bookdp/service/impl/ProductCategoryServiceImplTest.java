package com.fyh.bookdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.service.ProductCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductCategoryServiceImplTest {

    @Autowired
    private ProductCategoryService service;

    @Test
    void getAllProductCategoryVO(){
        service.getAllProductCategoryVO();
    }

    @Test
    void test(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type",1);
        service.list(wrapper).forEach(System.out::println);
    }

}