package com.fyh.bookdp.service.impl;

import com.fyh.bookdp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Test
    void a(){
        System.out.println(productService.list().size());
    }

}