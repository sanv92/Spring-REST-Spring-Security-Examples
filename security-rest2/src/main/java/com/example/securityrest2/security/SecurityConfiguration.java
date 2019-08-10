package com.example.securityrest2.security;

import com.example.securityrest2.domain.AuthoritiesConstants;
import com.example.securityrest2.repositories.TokenRepository;
import com.example.securityrest2.repositories.UserRepository;
import com.example.securityrest2.security.handlers.RestAccessDeniedHandler;
import com.example.securityrest2.security.handlers.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfiguration {

    @Configuration
    @Order(1)
    public static class LoginConfiguration extends WebSecurityConfigurerAdapter {

        private static final RequestMatcher URLS = new OrRequestMatcher(
                new AntPathRequestMatcher(SecurityConstants.LOGIN_URL)
        );

        private final UserDetailsService userDetailsService;

        private final PasswordEncoder passwordEncoder;

        private final TokenRepository tokenRepository;

        @Autowired
        public LoginConfiguration(
                UserDetailsService userDetailsService,
                PasswordEncoder passwordEncoder, TokenRepository tokenRepository
        ) {
            super();
            this.userDetailsService = userDetailsService;
            this.passwordEncoder = passwordEncoder;
            this.tokenRepository = tokenRepository;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .disable()
                    .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                    .exceptionHandling()
                        .accessDeniedHandler(accessDeniedHandler())
                        .defaultAuthenticationEntryPointFor(authenticationEntryPoint(), URLS)
                        .and()
                    .requestMatcher(URLS)
                        .authorizeRequests()
                        .antMatchers(SecurityConstants.LOGIN_URL).permitAll();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder builder) throws Exception {
            builder.authenticationProvider(authenticationProvider());
            builder.userDetailsService(userDetailsService);
        }

        private GenerateTokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
            GenerateTokenAuthenticationFilter filter = new GenerateTokenAuthenticationFilter(tokenRepository);
            filter.setAuthenticationManager(authenticationManagerBean());
            filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
            filter.setAuthenticationFailureHandler(authenticationFailureHandler());

            return filter;
        }

        public AuthenticationProvider authenticationProvider() {
            return new GenerateTokenAuthenticationProvider(
                    passwordEncoder,
                    userDetailsService
            );
        }

        private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler() {
            final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
            successHandler.setRedirectStrategy(new NoRedirectStrategy());

            return successHandler;
        }

        private SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
            return new SimpleUrlAuthenticationFailureHandler();
        }

        private RestAccessDeniedHandler accessDeniedHandler() {
            return new RestAccessDeniedHandler();
        }

        private RestAuthenticationEntryPoint authenticationEntryPoint() {
            return new RestAuthenticationEntryPoint();
        }

    }

    @Configuration
    @Order(2)
    public static class AdminConfiguration extends WebSecurityConfigurerAdapter {

        private static final RequestMatcher URLS = new OrRequestMatcher(
                new AntPathRequestMatcher("/admin/**")
        );

        private final UserDetailsService userDetailsService;

        private final TokenRepository tokenRepository;

        private final UserRepository userRepository;

        @Autowired
        public AdminConfiguration(
                UserDetailsService userDetailsService,
                TokenRepository tokenRepository,
                UserRepository userRepository
        ) {
            super();
            this.userDetailsService = userDetailsService;
            this.tokenRepository = tokenRepository;
            this.userRepository = userRepository;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .addFilterBefore(tokenAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                    .exceptionHandling()
                        .accessDeniedHandler(accessDeniedHandler())
                        .defaultAuthenticationEntryPointFor(authenticationEntryPoint(), URLS)
                        .and()
                    .requestMatcher(URLS)
                        .authorizeRequests()
                        .anyRequest().hasAuthority(AuthoritiesConstants.ADMIN);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder builder) throws Exception {
            builder.authenticationProvider(authorizationProvider());
            builder.userDetailsService(userDetailsService);
        }

        private VerifyTokenFilter tokenAuthorizationFilter() throws Exception {
            final VerifyTokenFilter filter = new VerifyTokenFilter(URLS);
            filter.setAuthenticationManager(authenticationManager());
            filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
            filter.setAuthenticationFailureHandler(authenticationFailureHandler());

            return filter;
        }

        public AuthenticationProvider authorizationProvider() {
            return new VerifyTokenProvider(
                    userDetailsService,
                    tokenRepository,
                    userRepository
            );
        }

        private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler() {
            final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
            successHandler.setRedirectStrategy(new NoRedirectStrategy());

            return successHandler;
        }

        private SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
            return new SimpleUrlAuthenticationFailureHandler();
        }

        private RestAccessDeniedHandler accessDeniedHandler() {
            return new RestAccessDeniedHandler();
        }

        private RestAuthenticationEntryPoint authenticationEntryPoint() {
            return new RestAuthenticationEntryPoint();
        }

    }

    @Configuration
    @Order(3)
    public static class WebConfiguration extends WebSecurityConfigurerAdapter {

        private static final RequestMatcher URLS = new OrRequestMatcher(
                new AntPathRequestMatcher("/users/test2"),
                new AntPathRequestMatcher("/users/test3"),
                new AntPathRequestMatcher("/users/test4")
        );

        private final UserDetailsService userDetailsService;

        private final TokenRepository tokenRepository;

        private final UserRepository userRepository;

        @Autowired
        public WebConfiguration(
                UserDetailsService userDetailsService,
                TokenRepository tokenRepository,
                UserRepository userRepository
        ) {
            super();
            this.userDetailsService = userDetailsService;
            this.tokenRepository = tokenRepository;
            this.userRepository = userRepository;
        }

        @Override
        public void configure(WebSecurity web) {
            // Filters will not get executed for the resources
            web.ignoring()
                    .antMatchers("/", "/favicon.ico", "/resources/**", "/static/**", "/public/**", "/webui/**", "/h2-console/**"
                            , "/configuration/**", "/swagger-ui/**", "/swagger-resources/**", "/api-docs", "/api-docs/**", "/v2/api-docs/**"
                            , "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.ttf", "/**/*.woff", "/**/*.otf"
                    )
                    .antMatchers(HttpMethod.OPTIONS, "/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .addFilterBefore(tokenAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                    .exceptionHandling()
                        .accessDeniedHandler(accessDeniedHandler())
                        .defaultAuthenticationEntryPointFor(authenticationEntryPoint(), URLS)
                        .and()
                    .requestMatcher(URLS)
                        .authorizeRequests()
                        .antMatchers("/users/test2").authenticated()
                        .antMatchers("/users/test3").hasAuthority(AuthoritiesConstants.ADMIN)
                        .antMatchers("/users/test4").hasAuthority(AuthoritiesConstants.ADMIN);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder builder) throws Exception {
            builder.authenticationProvider(authorizationProvider());
            builder.userDetailsService(userDetailsService);
        }

        private VerifyTokenFilter tokenAuthorizationFilter() throws Exception {
            final VerifyTokenFilter filter = new VerifyTokenFilter(URLS);
            filter.setAuthenticationManager(authenticationManager());
            filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
            filter.setAuthenticationFailureHandler(authenticationFailureHandler());

            return filter;
        }

        public AuthenticationProvider authorizationProvider() {
            return new VerifyTokenProvider(
                    userDetailsService,
                    tokenRepository,
                    userRepository
            );
        }

        private SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler() {
            final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
            successHandler.setRedirectStrategy(new NoRedirectStrategy());

            return successHandler;
        }

        private SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
            return new SimpleUrlAuthenticationFailureHandler();
        }

        private RestAccessDeniedHandler accessDeniedHandler() {
            return new RestAccessDeniedHandler();
        }

        private RestAuthenticationEntryPoint authenticationEntryPoint() {
            return new RestAuthenticationEntryPoint();
        }

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(@Qualifier("myUserDetailsService") UserDetailsService userDetailsService) {
        return userDetailsService;
    }

}
