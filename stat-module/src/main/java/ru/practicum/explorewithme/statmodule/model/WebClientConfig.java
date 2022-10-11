package ru.practicum.explorewithme.statmodule.model;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.statmodule.ClientStat;

//@Configuration
//public class WebClientConfig {
//    private String serviceUrl;
//
//    private String appName;
//
//    @Bean
//    public ClientStat clientStat(RestTemplateBuilder builder) {
//        var restTemplate = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl)).build();
//
//        ClientStat client = new ClientStat();
//        //ClientStat client = new ClientStat(restTemplate);
//        //client.setAppName(appName);
//        return client;
//    }
//}
