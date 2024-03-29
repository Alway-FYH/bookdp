package com.fyh.bookdp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    void findAllCartVOByUserId() {
        cartService.findAllCartVOByUserId(17).forEach(System.out::println);
    }
}