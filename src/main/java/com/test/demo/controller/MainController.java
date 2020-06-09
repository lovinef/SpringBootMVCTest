package com.test.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class MainController {
    @GetMapping("/")
    public String hello(){
        return "index";
    }

    @PostMapping("/")
    public String postHello(HttpServletRequest request, Model model){
        // 로그인 실패시 실패 전달.
        Optional.ofNullable(request.getParameter("loginerror"))
                .ifPresent(reqParam -> model.addAttribute("loginerror", true));

        // 로그인 실패시 아이디 전달.
        Optional.ofNullable(request.getParameter("username"))
                .ifPresent(reqParam -> model.addAttribute("name", reqParam));

        return "index";
    }
}
