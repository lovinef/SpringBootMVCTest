package com.test.demo.controller;

import com.test.demo.model.User;
import com.test.demo.service.UserLoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping(value = "/user")
@AllArgsConstructor
@Slf4j
public class UserLoginController {
    private UserLoginService userLoginService;

    @GetMapping("/login/result")
    public String loginResult(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        log.info("session > " + userDetails.getUsername());

        userDetails.getAuthorities()
                .forEach(auth -> log.info("auth > " + auth.toString()));

        return "loginSuccess";
    }

    // 로그아웃 결과 페이지
    @GetMapping("/logout/result")
    public String logoutResult() {
        return "index";
    }

    // 회원가입
    @GetMapping("/signup")
    public String getUserSignup() {
        return "signup";
    }

    // 회원가입
    @PostMapping("/signup")
    public String postUserSignup(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "password") String password
    ) {
        User user = User.builder().name(name).password(password).build();
        userLoginService.joinUser(user);
        return "redirect:/";
    }
}
