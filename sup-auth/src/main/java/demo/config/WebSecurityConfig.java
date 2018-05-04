package demo.config;

import demo.service.CustomUserDetailsServiceImpl;
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
 *  This is a useful place to put user-defined access rules if you want to override the default access rules.
 */
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER -2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private CustomUserDetailsServiceImpl userDetailsService;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // TODO: Test only.
    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth.inMemoryAuthentication()
                .withUser("john").password("123").roles("USER").and()
                .withUser("tom").password("111").roles("ADMIN").and()
                .withUser("user1").password("password").roles("USER").and()
                .withUser("admin").password("nimda").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/login").permitAll()
                .antMatchers("/oauth/token/revokeById/**").permitAll()
                .antMatchers("/tokens/**").permitAll()
//                .anyRequest().authenticated()
//                .and().formLogin().permitAll()
                .and().csrf().disable();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(userDetailsService);
//                .passwordEncoder(passwordEncoder);
//    }
}
