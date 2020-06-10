package com.test.demo.controller;

import com.test.demo.model.User;
import com.test.demo.service.UserRestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/user/api")
public class UserRestController {
    private UserRestService userRestService;

    @GetMapping("/list")
    public List<User> userList(){
        return userRestService.getUserList();
    }
}
