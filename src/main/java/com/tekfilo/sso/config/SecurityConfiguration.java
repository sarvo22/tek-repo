package com.tekfilo.sso.config;

import com.tekfilo.sso.config.filter.JWTAuthenticationFilter;
import com.tekfilo.sso.config.filter.JWTAuthorizationFilter;
import com.tekfilo.sso.config.service.AuthUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthUserDetailService authenticationUserDetailService;


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/sso/login,/sso/inquiry,/sso/getsignupdetails/*,/sso/getsignupdetails,/sso/getsignupdetails/**,/sso/signup/verify,/sso/signup/verify/**,/sso/signup/verify/*/*,/sso/confirminvitation,/sso/confirminvitation/**,/sso/confirminvitation/*/*,/sso/inquiry,/sso/inquiry/**,/sso/inquiry/*/*");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/sso/login").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/inquiry").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/getcountries").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/emailcheck").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/usercheck").permitAll()
                .antMatchers(HttpMethod.POST, AuthenticationConfigConstants.SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/login").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/getcountries").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/emailcheck").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/usercheck").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/getsignupdetails").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/getsignupdetails").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/getsignupdetails").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/getsignupdetails/**").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/getsignupdetails/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/getsignupdetails/**").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/signup/verify").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/signup/verify").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/signup/verify").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/signup/verify/**").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/signup/verify/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/signup/verify/**").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/signup/verify/*/*").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/signup/verify/*/*").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/signup/verify/*/*").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/confirminvitation").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/confirminvitation").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/confirminvitation").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/confirminvitation/**").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/confirminvitation/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/confirminvitation/**").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/confirminvitation/*/*").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/confirminvitation/*/*").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/confirminvitation/*/*").permitAll()
                .antMatchers(HttpMethod.OPTIONS, AuthenticationConfigConstants.SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/inquiry").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/inquiry/*/*").permitAll()
                .antMatchers(HttpMethod.POST, "/sso/inquiry/*/*").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/sso/inquiry/*/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationUserDetailService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/sso/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").allowedHeaders("*");
    }
}
