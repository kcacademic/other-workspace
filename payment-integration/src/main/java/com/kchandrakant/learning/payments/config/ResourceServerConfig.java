package com.kchandrakant.learning.payments.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@RefreshScope
@PropertySource(value = "classpath:application.yml")
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    Logger logger = LoggerFactory.getLogger(ResourceServerConfig.class);

    @Value("${payments.oauth.token.endpoint.url}")
    private String endpointUrl;

    @Value("${payments.oauth.token.endpoint.clientId}")
    private String clientId;

    @Value("${payments.oauth.token.endpoint.clientSecret}")
    private String clientSecret;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/payments/**")
            .authenticated()
            .antMatchers("/")
            .permitAll();
    }

    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(endpointUrl);
        tokenService.setClientId(clientId);
        tokenService.setClientSecret(clientSecret);
        return tokenService;
    }
}