package com.scm.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.scm.services.impl.SecurityCustomUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
    @Autowired
    private SecurityCustomUserDetailsService userDetailsService;
    @Autowired
    private OAuthenticationSuccessHandler oAuthenticationSuccessHandler;
    @Autowired
    AuthFailureHandler authFailureHandler;

    // user create and login using java code with in memory service
    // @Bean
    // @Bean
    // public UserDetailsService userDetailsService() {

    // UserDetails user =
    // User.withDefaultPasswordEncoder().username("admin").password("admin")
    // .roles("ADMIN").build();
    // UserDetails user1 =
    // User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
    // UserDetails
    // user2=User.withDefaultPasswordEncoder().username("user1").password("user1").roles("USER").build();

    // var InMemoryUserDetailsManager= new
    // InMemoryUserDetailsManager(user,user1,user2);
    // return InMemoryUserDetailsManager;

    // }

 
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Configure the httpSecurity

        //url configuration
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home","/register","/services").permitAll());
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        //form default login page
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/profile", true);
            formLogin.failureUrl("/login?error");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            formLogin.successHandler((request, response, authentication) -> {
                response.sendRedirect("/user/profile");
            });
            formLogin.failureHandler((request, response, exception) -> {
                response.sendRedirect("/login?error");
            });

            formLogin.failureHandler(authFailureHandler);
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/login");
           logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));
            // logout.invalidateHttpSession(true);
        });

        // oAuth2 Configuration
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.defaultSuccessUrl("/user/dashboard");
            oauth.failureUrl("/login?error");
            oauth.successHandler(oAuthenticationSuccessHandler);
        });
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
