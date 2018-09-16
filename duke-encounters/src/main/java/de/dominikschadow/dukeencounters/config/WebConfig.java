/*
 * Copyright (C) 2018 Dominik Schadow, dominikschadow@gmail.com
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

import org.owasp.appsensor.core.AppSensorClient;
import org.owasp.appsensor.core.DetectionSystem;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Validator;

/**
 * Spring configuration file.
 *
 * @author Dominik Schadow
 */
@Configuration
@EnableConfigurationProperties(DukeEncountersProperties.class)
@ComponentScan(basePackages = "org.owasp.appsensor")
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public DetectionSystem detectionSystem(final AppSensorClient appSensorClient) {
        return new DetectionSystem(appSensorClient.getConfiguration().getServerConnection()
                .getClientApplicationIdentificationHeaderValue());
    }

    @Bean
    public Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public SpringValidatorAdapter springValidatorAdapter(final Validator jsr303Validator) {
        return new SpringValidatorAdapter(jsr303Validator);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/search").setViewName("search");
        registry.addViewController("/login").setViewName("login");
    }
}
