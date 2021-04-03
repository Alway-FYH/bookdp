package com.fyh.bookdp.config;

import com.fyh.bookdp.filter.AdminFilter;
import com.fyh.bookdp.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new UserFilter());
        filterRegistrationBean.addUrlPatterns("/cart/*","/orders/*","/user/userInfo","/orderDetail/findUserBuy");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean adminfilterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new AdminFilter());
        filterRegistrationBean.addUrlPatterns("/all/*");
        return filterRegistrationBean;
    }



}
