package com.ndr.socialasteroids.infra.security;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ndr.socialasteroids.service.security.UserAuthService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthService userAuthService;
    private final PasswordEncoder passwordEncoder;
    

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, UserAuthService userAuthService){
        this.userAuthService = userAuthService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception{
        authBuilder.userDetailsService(userAuthService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            .authorizeRequests()
                .antMatchers("/h2-console","/h2-console/**").permitAll()
                .antMatchers("/user/register").permitAll()
                .anyRequest().authenticated()

            .and()
            .csrf().disable()
            .formLogin()
            .and()
            .rememberMe()
                //.tokenRepository() //TODO: Colocar no repositorio
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .key("temp")//TODO:: Colocar em config file
                .rememberMeParameter("rememberme")
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            .and()
            .headers().frameOptions().disable();//TODO: Configuração para acessar H2 console corretamente
    }
    
}
