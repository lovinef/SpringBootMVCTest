package com.test.demo.service;

import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserRestService {
    private UserRepository userRepository;

    public List<User> getUserList(){
        List<User> userList = new ArrayList<>();
        userRepository.findAll()
                .forEach(userEntity ->
                        userList.add(
                            User.builder()
                                .id(userEntity.getId())
                                .name(userEntity.getName())
                            .build()
                        )
                );

        return userList;
    }
}
