package com.example.my_blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {

    @GetMapping("/login")
    public String longin(){
        return "login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
}
