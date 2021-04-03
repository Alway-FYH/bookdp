package com.fyh.bookdp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RedirectHandler {

    @GetMapping("/{url}")
    public String redirect(@PathVariable("url") String url){
        return url;
    }

    @GetMapping("/")
    public String main(){
        return "redirect:/productCategory/list";
    }

    @GetMapping("/userLogin")
    public ModelAndView userLogin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
    @GetMapping("/adminLogin")
    public ModelAndView adminLogin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminLogin");
        return modelAndView;
    }
}
