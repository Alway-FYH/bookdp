package com.fyh.bookdp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService service;

    @Test
    void test(){
        Map<String,Object> map = new HashMap<>();
        map.put("categorylevelthree_id",655);
        service.listByMap(map).forEach(System.out::println);
    }

    @Test
    void get(){
        System.out.println(service.getById(733));
    }

    @Test
    void test5(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("name","香");
        service.list(wrapper).forEach(System.out::println);

    }

    @Test
    void Test(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("ID",752);
        System.out.println(service.getOne(wrapper));
    }

    @Test
    void www(){
        QueryWrapper wrapper = new QueryWrapper();
        String str = "categorylevel"+"one"+"_id";
        wrapper.eq(str,548);
        service.list(wrapper).forEach(System.out::println);
    }
}