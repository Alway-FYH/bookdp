package com.fyh.bookdp.controller;

import com.fyh.bookdp.service.UserAddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserAddressControllerTest {

    @Autowired
    private UserAddressService service;

    @Test
    void list() {
    }

    @Test
    void delete() {
        service.removeById(15);
    }
}