package com.example.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@ComponentScan("com.example.auth")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService userDetailsService;

    private final DataSource dataSource;

    @Autowired
    public WebSecurityConfiguration(
            MyUserDetailsService userDetailsService,
            @Qualifier("dataSource") DataSource dataSource) {
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/registration/*").permitAll()
                    .antMatchers("/registration**").permitAll()
                    .antMatchers("/users**").hasAuthority("ADMIN")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/login*").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    //.loginProcessingUrl("/perform_login")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login?error=true")
                    //.failureHandler(authenticationFailureHandler())
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/perform_logout")
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    .and()
                .rememberMe()
                    .key("uniqueAndSecret")
                    .rememberMeParameter("remember-me")
                    .rememberMeCookieName("remember-me")
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(1209600)
                    .and()
                .csrf()
                    .ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
                    .and()
                .headers()
                    .frameOptions()
                    .sameOrigin();

        http.csrf().disable();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setCreateTableOnStartup(false);
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
    }
}
