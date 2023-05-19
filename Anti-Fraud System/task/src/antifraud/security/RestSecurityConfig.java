package antifraud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder);
    }

    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers(HttpMethod.DELETE, "/api/auth/user/*").hasAuthority(SecurityConfig.Roles.ADMINISTRATOR.name())
                .antMatchers(HttpMethod.GET, "/api/auth/list", "/api/auth/list/*").hasAnyAuthority(SecurityConfig.Roles.ADMINISTRATOR.name(), SecurityConfig.Roles.SUPPORT.name())
                .antMatchers(HttpMethod.POST, "/api/antifraud/transaction", "/api/antifraud/transaction/*").hasAuthority(SecurityConfig.Roles.MERCHANT.name())
                .antMatchers(HttpMethod.PUT, "/api/auth/access", "/api/auth/access/*").hasAuthority(SecurityConfig.Roles.ADMINISTRATOR.name())
                .antMatchers(HttpMethod.PUT, "/api/auth/role", "/api/auth/role/*").hasAuthority(SecurityConfig.Roles.ADMINISTRATOR.name())
                .antMatchers(HttpMethod.POST, "/api/antifraud/suspicious-ip", "/api/antifraud/suspicious-ip/*").hasAuthority(SecurityConfig.Roles.SUPPORT.name())
                .antMatchers(HttpMethod.DELETE, "/api/antifraud/suspicious-ip", "/api/antifraud/suspicious-ip/*").hasAuthority(SecurityConfig.Roles.SUPPORT.name())
                .antMatchers(HttpMethod.GET, "/api/antifraud/suspicious-ip", "/api/antifraud/suspicious-ip/*").hasAuthority(SecurityConfig.Roles.SUPPORT.name())
                .antMatchers(HttpMethod.POST, "/api/antifraud/stolencard", "/api/antifraud/stolencard/*").hasAuthority(SecurityConfig.Roles.SUPPORT.name())
                .antMatchers(HttpMethod.DELETE, "/api/antifraud/stolencard", "/api/antifraud/stolencard/*").hasAuthority(SecurityConfig.Roles.SUPPORT.name())
                .antMatchers(HttpMethod.GET, "/api/antifraud/stolencard", "/api/antifraud/stolencard/*").hasAuthority(SecurityConfig.Roles.SUPPORT.name())
                .antMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                .antMatchers("/actuator/shutdown", "/h2-console/*").permitAll() // needs to run test
                .antMatchers("/**").authenticated()
                // other matchers
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }
}
