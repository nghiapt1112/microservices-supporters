package demo.config;

import demo.domain.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * Order before the basic authentication access control provided by Boot.
 * This is a useful place to put user-defined access rules if you want to override the default access rules.
 */
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
//                .antMatchers("/uaa/user/**", "/user/**").permitAll()
                .antMatchers("/my/oauth/check_token/**").permitAll()
                .antMatchers("/tk/oauth/token/revokeById/**").permitAll()
                .antMatchers("/tk/tokens/**").permitAll()
                .antMatchers("/author/with_role").hasRole("ADMIN")
                .antMatchers("/author/multi_roles").access("hasRole('ADMIN') and hasRole('DBA')")
                .antMatchers("/my/user/**").permitAll()
                .anyRequest().authenticated()
//                .and().formLogin().permitAll()
                .and().csrf().disable()
        ;

        // logOUT
        http
                .logout()
                .logoutUrl("/my/logout")
                .logoutSuccessUrl("/my/index")
                //  Implement logout Success handler
                //  .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)
                //  CustomlogoutHandler
                //  .addLogoutHandler(logoutHandler)
                //  cookieNamesToClear
                //  .deleteCookies(cookieNamesToClear)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder);
        ;
    }

}
