/*
 * Copyright (C) 2016 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Application Intrusion Detection project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dominikschadow.dukeencounters.config;

import org.owasp.appsensor.integration.springsecurity.context.AppSensorSecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.sql.DataSource;

/**
 * Spring Security configuration.
 *
 * @author Dominik Schadow
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DukeEncountersProperties properties;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/", "/register", "/encounters", "/search", "/error").permitAll()
                .anyRequest().authenticated()
                .and()
            .csrf()
                .ignoringAntMatchers("/admin/h2-console/*")
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/account")
                .permitAll()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .and()
            .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
            .rememberMe()
                .and()
            .securityContext().securityContextRepository(securityContextRepository())
                .and()
            .headers()
                .contentSecurityPolicy("default-src 'self'; img-src 'self' https://camo.githubusercontent.com")
                .and()
                .frameOptions().sameOrigin();
        // @formatter:on
    }

    /**
     * BCryptPasswordEncoder constructor takes a work factor as argument. The default is 10, the valid range is 4 to 31.
     * The amount of work increases exponentially.
     *
     * @return The PasswordEncoder to use for all dukeEncountersUser passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(properties.getPasswordStrength());
    }

    @Override
    public void configure(final WebSecurity web) {
        // @formatter:off
        web
            .ignoring()
                .antMatchers("/css/**", "/img/**", "/webjars/**");
        // @formatter:on
    }

    /**
     * Enables JDBC authentication and sets the configured BCryptPasswordEncoder.
     *
     * @param auth The AuthenticationManagerBuilder
     * @throws Exception during configuration
     */
    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth
            .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder());
        // @formatter:on
    }

    /**
     * Sets the AppSensorSecurityContextRepository as the SecurityContextRepository.
     *
     * @return The SecurityContextRepository to use
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new AppSensorSecurityContextRepository();
    }
}
