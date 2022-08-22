package org.nnn4eu.hfische.blablahelp.blablahelpbackend.security;

import lombok.RequiredArgsConstructor;
import org.nnn4eu.hfische.blablahelp.blablahelpbackend.config.UrlMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
public class SecurityConfig implements WebSecurityCustomizer {

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http = http.cors()
                .and().csrf().disable();

        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) ->
                                response.sendError(
                                        HttpServletResponse.SC_UNAUTHORIZED,
                                        ex.getMessage()
                                )
                )
                .and();

        http.authorizeRequests()
                .antMatchers(UrlMapping.PUBLIC, UrlMapping.PUBLIC + "/**").permitAll()

                .antMatchers(UrlMapping.BASIC, UrlMapping.BASIC + "/**").hasAuthority("BASIC")
                .antMatchers(UrlMapping.ADMIN, UrlMapping.ADMIN + "/**").hasAuthority("ADMIN")//hasRole("BASIC")//
                .anyRequest().denyAll()
                .and().httpBasic()
        ;


        return http.build();
    }


    @Override
    public void customize(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedOriginPatterns("*")
                        .allowedHeaders("*")
                        .allowedMethods("*");
            }
        };
    }
}
