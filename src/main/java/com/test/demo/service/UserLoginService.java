package com.test.demo.service;

import com.test.demo.model.Role;
import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserLoginService implements UserDetailsService {
    private UserRepository userRepository;

    @Transactional
    public Long joinUser(User user){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        com.test.demo.entity.User userEntity = com.test.demo.entity.User.builder()
                .name(user.getName())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .build();

        return userRepository.save(userEntity).getId();
    }

    @Transactional
    public void changePassword(User user){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        userRepository.findByName(user.getName()).ifPresent(userEntity ->{
            userEntity.changePassword(bCryptPasswordEncoder.encode(user.getPassword()));
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.test.demo.entity.User userEntity = userRepository.findByName(username)
                                                            .orElseThrow(() -> new UsernameNotFoundException(username));

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin").equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }

        return new org.springframework.security.core.userdetails.User(userEntity.getName(), userEntity.getPassword(), authorities);
    }
}
