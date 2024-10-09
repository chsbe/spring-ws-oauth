package com.example.demo.config;

import com.example.demo.interceptor.OauthWsClientInterceptor;
import com.example.demo.ws.BosExchangeClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class BosConfiguration {

  @Bean
  public Jaxb2Marshaller jaxb2Marshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("com.example.demo.bos.exchange");
    return marshaller;
  }

  @Bean
  public BosExchangeClient bosExchangeClient(Jaxb2Marshaller jaxb2Marshaller,
      OAuth2AuthorizedClientManager manager) {
    BosExchangeClient client = new BosExchangeClient();

    client.setMarshaller(jaxb2Marshaller);
    client.setUnmarshaller(jaxb2Marshaller);
    client.setInterceptors(
        new ClientInterceptor[]{new OauthWsClientInterceptor(manager)});
    return client;
  }
}
