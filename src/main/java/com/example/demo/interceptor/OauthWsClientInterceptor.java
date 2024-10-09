package com.example.demo.interceptor;

import static com.example.demo.config.Oauth2Config.REGISTRATION_ID;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;

public class OauthWsClientInterceptor implements ClientInterceptor {

  private final OAuth2AuthorizedClientManager manager;
  private final Authentication authentication;

  private static final Logger LOGGER = LoggerFactory.getLogger(OauthWsClientInterceptor.class);
  public OauthWsClientInterceptor(OAuth2AuthorizedClientManager manager) {
    this.manager = manager;
    this.authentication = createPrincipal();
  }

  private Authentication createPrincipal() {
    return new Authentication() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptySet();
      }

      @Override
      public Object getCredentials() {
        return null;
      }

      @Override
      public Object getDetails() {
        return null;
      }

      @Override
      public Object getPrincipal() {
        return this;
      }

      @Override
      public boolean isAuthenticated() {
        return false;
      }

      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
      }

      @Override
      public String getName() {
        return REGISTRATION_ID;
      }
    };
  }

  @Override
  public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
    OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
        .withClientRegistrationId(REGISTRATION_ID)
        .principal(authentication)
        .build();
    OAuth2AuthorizedClient oAuth2AuthorizedClient = manager.authorize(oAuth2AuthorizeRequest);
    if (oAuth2AuthorizedClient == null) {
      throw new OAuth2AuthenticationException("Failed to authorize client");
    } else {
      LOGGER.debug("""
      !!!A ne pas faire, mauvaise idée, juste pour du debug !!!
      token{} 
      issuedAt {}
      expiresAt {}""", oAuth2AuthorizedClient.getAccessToken().getTokenValue(),
          oAuth2AuthorizedClient.getAccessToken().getIssuedAt(),
          oAuth2AuthorizedClient.getAccessToken().getExpiresAt());
    }

    var context = TransportContextHolder.getTransportContext();
    var connection = (HttpUrlConnection) context.getConnection();

    try {
      connection.addRequestHeader(HttpHeaders.AUTHORIZATION, "Bearer " + oAuth2AuthorizedClient.getAccessToken().getTokenValue());
      //I-CITY mandatory headers
      connection.addRequestHeader("x-businessflow-id", "sample_code_cirb");
      connection.addRequestHeader("x-context-id", "bos-exchange-out");
      connection.addRequestHeader("x-user-id", "cirb-test");

    } catch (IOException e) {
      LOGGER.error("Cannot add header", e);
      return false;
    }
    LOGGER.debug("headers added in soap request");
    return true;
  }

  @Override
  public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
    return true;
  }

  @Override
  public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
    LOGGER.debug("Fault");
    return true;
  }

  @Override
  public void afterCompletion(MessageContext messageContext, Exception ex)
      throws WebServiceClientException {

  }
}
