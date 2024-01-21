package com.upo.springtest.security;

import com.upo.springtest.enums.EmployeePosition;
import com.upo.springtest.enums.Role;
import com.upo.springtest.model.Address;
import com.upo.springtest.model.Customer;
import com.upo.springtest.model.Employee;
import com.upo.springtest.model.User;
import com.upo.springtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UserService userService;

    @Autowired
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/access-denied");
        };
    }



    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }





    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        http.formLogin(login -> login
                .defaultSuccessUrl("/")
                .permitAll());

        http.authenticationProvider(authenticationProvider());

        http.exceptionHandling(
                exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
        );

        http.authorizeHttpRequests(configurer ->
                configurer

                        .requestMatchers(antMatcher("/register")).permitAll()
/*                        .requestMatchers(antMatcher("/process-register")).permitAll()
                        .requestMatchers(antMatcher("/images/**")).permitAll()*/
                        .requestMatchers(antMatcher("/model-images/**")).permitAll()
                        .requestMatchers(antMatcher("/access-denied")).permitAll()

                        .requestMatchers(antMatcher("/")).permitAll()
                        .requestMatchers(antMatcher("/info")).permitAll()
                        .requestMatchers(antMatcher("/contact")).permitAll()
                        .requestMatchers(antMatcher("/account")).authenticated()


                        .requestMatchers(antMatcher("/customers/**")).hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.GET,"/employees/**")).hasAuthority("ADMIN")
                        .requestMatchers(antMatcher("/users/update/**")).authenticated()
                        .requestMatchers(antMatcher("/registerEmployee")).hasAuthority("ADMIN")
                        .requestMatchers(antMatcher("/employees/update/**")).hasAuthority("ADMIN")
                        .requestMatchers(antMatcher("/employees/delete/**")).hasAuthority("ADMIN")
                        .requestMatchers(antMatcher("/dashboard")).hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.GET,"/customers/**")).hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(antMatcher("/models/update/**")).hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(antMatcher("/models/add/**")).hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(antMatcher("/models/delete/**")).hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(antMatcher("/cars/**")).hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.GET, "/models/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/models/**")).hasAnyAuthority("EMPLOYEE", "ADMIN")
                        //.requestMatchers(antMatcher(HttpMethod.GET, "/sendMail/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/bookings/cancel/**")).permitAll()
                        .requestMatchers(antMatcher("/bookings/update/**")).hasAnyAuthority("EMPLOYEE", "ADMIN")


                        .requestMatchers(antMatcher("/bookACar")).authenticated()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/bookings/**")).authenticated()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/cars/map")).hasAnyAuthority("EMPLOYEE", "ADMIN")


        );


        return http.build();
    }



}
