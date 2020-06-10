package com.test.demo.controller;

import com.test.demo.model.User;
import com.test.demo.service.UserLoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping(value = "/user")
@AllArgsConstructor
@Slf4j
public class UserLoginController {
    private UserLoginService userLoginService;

    // User 로그인 성공시
    @GetMapping("/login/result")
    public String loginResult(Authentication authentication){
        return "user/loginSuccess";
    }

    // 로그아웃 결과 페이지
    @GetMapping("/logout/result")
    public String logoutResult() {
        return "index";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String getUserSignup() {
        return "user/signup";
    }

    // 회원가입 수행
    @PostMapping("/signup")
    public String postUserSignup(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "password") String password
    ) {
        User user = User.builder().name(name).password(password).build();
        userLoginService.joinUser(user);
        return "redirect:/";
    }

    // 비밀번호 변경 페이지
    @GetMapping("/password")
    public String changePassword() {
        return "user/passwordChange";
    }

    @PostMapping("/password/change")
    public String passwordChange(
            @RequestParam(name = "password") String password,
            Principal principal
    ){
        User user = User.builder()
                .name(principal.getName())
                .password(password)
                .build();
        userLoginService.changePassword(user);
        return "redirect:/user/logout";
    }
}
