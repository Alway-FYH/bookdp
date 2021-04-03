package com.fyh.bookdp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication()
@MapperScan("com.fyh.bookdp.mapper")
public class BookdpApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookdpApplication.class, args);
    }


    @Bean
    MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("/data/apps/temp");
        return factory.createMultipartConfig();
    }
}
