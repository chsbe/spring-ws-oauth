package com.example.demo.ws;

import com.example.demo.bos.exchange.ObjectFactory;
import com.example.demo.bos.exchange.Properties;
import com.example.demo.bos.exchange.Property;
import com.example.demo.bos.exchange.SendDocRequest;
import jakarta.xml.bind.JAXBElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class BosExchangeClient extends WebServiceGatewaySupport {

  private static final Logger LOGGER = LoggerFactory.getLogger(BosExchangeClient.class);

  @Value("${i-city.bos.uri}")
  private String bosExchangeUri;

  public void sendDocument(SendDocRequest sendDocRequest) {
    ObjectFactory objectFactory = new ObjectFactory();
    JAXBElement<SendDocRequest> request = objectFactory.createSendDocRequest(sendDocRequest);

    getWebServiceTemplate().marshalSendAndReceive(bosExchangeUri, request);
    LOGGER.debug("Document sent");
  }

}
