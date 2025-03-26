//package com.example.wordcounter.config;
//
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class AppConfig {
//
//    @Bean
//    @LoadBalanced
//    public RestTemplate loadBalancedRestTemplate() {
//        return new RestTemplate();
//    }
//
//    @Bean
//    public RestTemplate simpleRestTemplate() {
//        return new RestTemplate();
//    }
//}

package com.example.wordcounter.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced
    @Primary  // Mark this as primary for general use
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "simpleRestTemplate")
    public RestTemplate simpleRestTemplate() {
        return new RestTemplate();
    }
}