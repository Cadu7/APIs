package br.com.api.apispring.config;

import br.com.api.apispring.security.JwtAuthenticationFilter;
import br.com.api.apispring.security.JwtAuthorizationFilter;
import br.com.api.apispring.security.JwtUtil;
import br.com.api.apispring.security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static br.com.api.apispring.domain.model.Role.ROLE_ADMIN;
import static br.com.api.apispring.domain.model.Role.ROLE_USER;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailService userDetailService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        String[] userPath = { "/api/user/**" };
        
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, userPath).permitAll()
                .antMatchers(HttpMethod.POST, userPath).permitAll()
                .antMatchers(HttpMethod.PUT, userPath).hasAnyAuthority(ROLE_USER.toString(), ROLE_ADMIN.toString())
                .antMatchers(HttpMethod.DELETE, userPath).hasAuthority(ROLE_ADMIN.toString())
                
                .antMatchers(HttpMethod.POST,"/login").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                
                .anyRequest().authenticated()
                .and().exceptionHandling();
        http.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtUtil));
        http.addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtUtil, userDetailService));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }
    
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
}
