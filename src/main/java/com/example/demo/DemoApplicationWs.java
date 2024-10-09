package com.example.demo;

import com.example.demo.service.BosExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoApplicationWs {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationWs.class);

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("bean-config.xml");
		BosExchangeService bosExchangeService = context.getBean(BosExchangeService.class);
		bosExchangeService.sendDocRequest();
		LOGGER.debug("Fin bosExchangeService");
		bosExchangeService.sendDocRequest();
		LOGGER.debug("Fin 2 bosExchangeService");
	}

}
