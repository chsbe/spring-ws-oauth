package com.example.demo.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Configuration
public class Oauth2Config {

  public static final String REGISTRATION_ID = "i-city";

  @Value("${i-city.registration-id}")
  private String registrationId;

  @Value("${i-city.client-id}")
  private String clientId;

  @Value("${i-city.client-secret}")
  private String clientSecret;

  @Value("${i-city.authorization-grant-type}")
  private String authGrandType;

  @Value("${i-city.token-uri}")
  private String tokenUri;

  @Bean
  public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
      ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientService authorizedClientService) {
    OAuth2AuthorizedClientProvider oAuth2AuthorizedClientProvider =
        OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .clientCredentials()
            .refreshToken()
            .password()
            .build();

    AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
        new AuthorizedClientServiceOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientService);

    manager.setAuthorizedClientProvider(oAuth2AuthorizedClientProvider);
    return manager;
  }

  @Bean
  InMemoryClientRegistrationRepository clientRegistrationRepository() {
    Builder clientRegistrationBuilder = ClientRegistration.withRegistrationId(registrationId);
    ClientRegistration clientRegistration = clientRegistrationBuilder.clientId(clientId)
        .clientSecret(clientSecret)
        .tokenUri(tokenUri)
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .authorizationGrantType(new AuthorizationGrantType(authGrandType))
        .build();
    return new InMemoryClientRegistrationRepository(List.of(clientRegistration));
  }

  @Bean
  OAuth2AuthorizedClientService oAuth2AuthorizedClientService(
      ClientRegistrationRepository clientRegistrationRepository) {
    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
  }
}
