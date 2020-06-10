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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserLoginService implements UserDetailsService {
    private UserRepository userRepository;

    @Transactional
    public Long joinUser(User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        com.test.demo.entity.User userEntity = com.test.demo.entity.User.builder()
                .name(user.getName())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();

        return userRepository.save(userEntity).getId();
    }

    @Transactional
    public void changePassword(User user){
        userRepository.findByName(user.getName()).ifPresent(userEntity ->{
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userEntity.changePassword(passwordEncoder.encode(user.getPassword()));
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.test.demo.entity.User userEntity = userRepository.findByName(username)
                                                            .orElseThrow(() -> new UsernameNotFoundException(username));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String password = request.getParameter("password"); // get from request parameter

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(password, userEntity.getPassword())){
            throw new UsernameNotFoundException("비밀번호가 잘못 되었습니다.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin").equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }

        return new org.springframework.security.core.userdetails.User(userEntity.getName(), userEntity.getPassword(), authorities);
    }
}
