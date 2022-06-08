package com.ndr.socialasteroids.security;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserPrincipalService userAuthService;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, UserPrincipalService userAuthService){
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
                //.successHandler()
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
            // .and()
            // .sessionManagement()
            //     .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            //     .maximumSessions(2)
            //     .expiredUrl("expiredUrl");
    }


    // @Bean
    // public HttpSessionEventPublisher httpSessionEventPublisher(){
    //     return new HttpSessionEventPublisher();
    // }
    
}
