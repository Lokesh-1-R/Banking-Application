package com.axis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.axis.user.Role.*;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers("/api/v1/auth/**","/api/v1/accounts")
        
        .permitAll()
       
//        .requestMatchers("/api/v1/auth/manager/Register").hasRole(ADMIN.name())
        
//       .requestMatchers("api/v1/employee/**").hasAnyRole(ADMIN.name(),MANAGER.name(),EMPLOYEE.name())
//       .requestMatchers(GET,"api/v1/employee/**").hasAnyAuthority(ADMIN_READ.name(),MANAGER_READ.name(),EMPLOYEE_READ.name())
//       .requestMatchers(POST,"api/v1/employee/**").hasAnyAuthority(ADMIN_CREATE.name(),MANAGER_CREATE.name(),EMPLOYEE_CREATE.name())
//       .requestMatchers(PUT,"api/v1/employee/**").hasAnyAuthority(ADMIN_UPDATE.name(),MANAGER_UPDATE.name(),EMPLOYEE_UPDATE.name())
//       .requestMatchers(DELETE,"api/v1/employee/**").hasAnyAuthority(ADMIN_DELETE.name(),MANAGER_DELETE.name(),EMPLOYEE_DELETE.name())
//       
//       .requestMatchers("api/v1/requestAccount/**").hasAnyRole(ADMIN.name(),MANAGER.name(),EMPLOYEE.name())
//       .requestMatchers(GET,"api/v1/requestAccount/**").hasAnyAuthority(ADMIN_READ.name(),MANAGER_READ.name(),EMPLOYEE_READ.name())
//       .requestMatchers(POST,"api/v1/requestAccount/**").hasAnyAuthority(ADMIN_CREATE.name(),MANAGER_CREATE.name(),EMPLOYEE_CREATE.name())
//       .requestMatchers(PUT,"api/v1/requestAccount/**").hasAnyAuthority(ADMIN_UPDATE.name(),MANAGER_UPDATE.name(),EMPLOYEE_UPDATE.name())
//       .requestMatchers(DELETE,"api/v1/requestAccount/**").hasAnyAuthority(ADMIN_DELETE.name(),MANAGER_DELETE.name(),EMPLOYEE_DELETE.name())
//
//       .requestMatchers("api/v1/manager/**").hasAnyRole(ADMIN.name(),MANAGER.name(),EMPLOYEE.name())
//       .requestMatchers(GET,"api/v1/manager/**").hasAnyAuthority(ADMIN_READ.name(),MANAGER_READ.name())
//       .requestMatchers(POST,"api/v1/manager/**").hasAnyAuthority(ADMIN_CREATE.name(),MANAGER_CREATE.name())
//       .requestMatchers(PUT,"api/v1/manager/**").hasAnyAuthority(ADMIN_UPDATE.name(),MANAGER_UPDATE.name())
//       .requestMatchers(DELETE,"api/v1/manager/**").hasAnyAuthority(ADMIN_DELETE.name(),MANAGER_DELETE.name())

//       .requestMatchers("api/v1/admin/**","api/v1/auth/admin/Register","api/v1/auth/manager/Register","api/v1/customer/Register","api/v1/create").hasRole(ADMIN.name())
//       .requestMatchers(GET,"api/v1/admin/**").hasAuthority(ADMIN_READ.name())
//       .requestMatchers(POST,"api/v1/admin/**","api/v1/auth/admin/Register","api/v1/auth/manager/Register","api/v1/create","api/v1/customer/Register").hasAuthority(ADMIN_CREATE.name())
//       .requestMatchers(PUT,"api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
//       .requestMatchers(DELETE,"api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())

//        .requestMatchers("api/v1/create","api/v1/auth/admin/Register").hasRole(ADMIN.name())

        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
