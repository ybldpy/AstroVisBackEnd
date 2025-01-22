package com.astrovis.astrovisbackend.Config;


import com.astrovis.astrovisbackend.auth.AuthEntryPoint;
import com.astrovis.astrovisbackend.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }





    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests().antMatchers("/user/login","/user/register").permitAll().anyRequest().authenticated()
                .and().formLogin().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().authenticationEntryPoint(new AuthEntryPoint());

    }






}
