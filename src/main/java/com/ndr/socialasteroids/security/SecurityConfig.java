package com.ndr.socialasteroids.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ndr.socialasteroids.security.JWT.AuthEntryPoint;
import com.ndr.socialasteroids.security.JWT.AuthTokenFilter;

// @Configuration @EnableWebSecurity @EnableGlobalMethodSecurity(prePostEnabled = true)
// public class SecurityConfig extends WebSecurityConfigurerAdapter
// {

//     private final UserDetailsService userDetailsService;

//     // @Autowired
//     // private AuthEntryPointJwt unauthorizedHandler;

//     // @Bean
//     // public AuthTokenFilter authenticationJwtTokenFilter()
//     // {
//     //     return new AuthTokenFilter();
//     // }

//     @Bean
//     public PasswordEncoder passwordEncoder()
//     {
//         return new BCryptPasswordEncoder();
//     }

//     @Autowired
//     public SecurityConfig(UserDetailsService userDetailsService)
//     {
//         this.userDetailsService = userDetailsService;
//     }

//     @Override
//     protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception
//     {
//         authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//     }

//     @Override
//     protected void configure(HttpSecurity http) throws Exception
//     {
//         http.authorizeRequests().antMatchers("/h2-console", "/h2-console/**").permitAll().antMatchers("/user/register")
//                 .permitAll().anyRequest().authenticated().and().csrf().disable().formLogin()
//                 //.successHandler()
//                 .and().rememberMe()
//                 //.tokenRepository() //TODO: Colocar no repositorio
//                 .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)).key("temp")//TODO:: Colocar em config file
//                 .rememberMeParameter("rememberme").and().logout()
//                 .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")).clearAuthentication(true)
//                 .invalidateHttpSession(true).deleteCookies("JSESSIONID").and().headers().frameOptions().disable();//TODO: Configuração para acessar H2 console corretamente
//         // .and()
//         // .sessionManagement()
//         //     .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//         //     .maximumSessions(2)
//         //     .expiredUrl("expiredUrl");
//     }

//     @Bean
//     public HttpSessionEventPublisher httpSessionEventPublisher()
//     {
//         return new HttpSessionEventPublisher();
//     }

// }


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final UserDetailsService userDetailsService;
    private final AuthTokenFilter authTokenFilter;
    private final PasswordEncoder passwordEncoder;
    private final AuthEntryPoint unauthorizedHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, AuthTokenFilter authTokenFilter, PasswordEncoder passwordEncoder, AuthEntryPoint unauthorizedHandler)
     {
         this.userDetailsService = userDetailsService;
         this.authTokenFilter = authTokenFilter;
         this.passwordEncoder = passwordEncoder;
         this.unauthorizedHandler = unauthorizedHandler;
     }

    @Override
    public void configure(AuthenticationManagerBuilder authBuilder) throws Exception
    {
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    // @Override
    // public void configure(WebSecurity webSecurity) throws Exception
    // {
    //     webSecurity.ignoring().antMatchers("/user");
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .cors().and().csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers("/h2-console", "/h2-console/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler);
                //.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}