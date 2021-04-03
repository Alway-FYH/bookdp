package com.fyh.bookdp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImgConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String file =System.getProperty("user.dir")+"/src/main/resources/static/images/";
        registry.addResourceHandler("/images/**").addResourceLocations("file:"+file);
    }
}
