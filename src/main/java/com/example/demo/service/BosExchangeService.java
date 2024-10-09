package com.example.demo.service;

import com.example.demo.bos.exchange.Properties;
import com.example.demo.bos.exchange.Property;
import com.example.demo.bos.exchange.SendDocRequest;
import com.example.demo.ws.BosExchangeClient;
import org.springframework.stereotype.Service;

@Service
public class BosExchangeService {

  private final BosExchangeClient bosExchangeClient;

  public BosExchangeService(BosExchangeClient bosExchangeClient) {
    this.bosExchangeClient = bosExchangeClient;
  }

  public void sendDocRequest() {
    SendDocRequest sendDocRequest = new SendDocRequest();
    sendDocRequest.setProcedure("procedure");
    sendDocRequest.setContextId("contextId");
    sendDocRequest.setInstanceId("instanceId");
    Property property = new Property();
    property.setName("propName");
    property.setValue("propValue");
    Properties properties = new Properties();
    properties.getProperty().add(property);
    sendDocRequest.setProperties(properties);

    bosExchangeClient.sendDocument(sendDocRequest);
  }
}
