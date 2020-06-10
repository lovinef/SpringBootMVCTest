package com.test.demo.config;

import com.test.demo.service.UserLoginService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity  // Spring Security 설정할 클래스라고 정의, 설정은 WebSebSecurityConfigurerAdapter 클래스를 상속받아 메서드를 구현하는 것이 일반적인 방법
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    private UserLoginService userLoginService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 디렉터리의 하위 파일 목록은 인증 무시
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // HttpSecurity를 통해 HTTP 요청에 대한 웹 기반 보안을 구성할 수 있습니다
        http.authorizeRequests()    //HttpServletRequest에 따라 접근(access)을 제한
                // 페이지 권한 설정
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                // .anyRequest().authenticated() // 모든 요청에 대해, 인증된 사용자만 접근하도록 설정
                .and()
                .formLogin()    // form 기반으로 인증. 로그인 정보는 기본적으로 HttpSession을 이용
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/user/login/result")    // 로그인이 성공했을 때 이동되는 페이지
                    .failureUrl("/?loginerror=true")     // 로그인 실패시 처리
                // .usernameParameter("파라미터명")  // 로그인 form에서 아이디는 name=username인 input을 기본으로 인식하나, 파라미터명 변경 가능
                .permitAll()
                .and()
                .logout()   // 로그아웃을 지원하는 메서드이며, WebSecurityConfigurerAdapter를 사용할 때 자동으로 적용
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))    // 로그아웃의 기본 URL(/logout) 이 아닌 다른 URL로 재정의.
                    .logoutSuccessUrl("/")    // 로그아웃 성공시 호출되는 URL
                    .invalidateHttpSession(true)    // HTTP 세션을 초기화하는 작업
                .and()
                // 403 예외처리 핸들링
                .exceptionHandling().accessDeniedPage("/user/denied")
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Spring Security에서 모든 인증은 AuthenticationManager를 통해 이루어지며 AuthenticationManager를 생성하기 위해서는 AuthenticationManagerBuilder를 사용
//        로그인 처리 즉, 인증을 위해서는 UserDetailService를 통해서 필요한 정보들을 가져오는데, 예제에서는 서비스 클래스(userLoginService)에서 이를 처리
        auth.userDetailsService(userLoginService).passwordEncoder(passwordEncoder());
    }
}
