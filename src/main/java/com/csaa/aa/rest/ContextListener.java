package com.csaa.aa.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextListener implements ServletContextListener {
	
	Logger logger = LoggerFactory.getLogger(ContextListener.class);

	public void contextInitialized(ServletContextEvent sce) {
		logger.debug("Entering ContextListener.contextInitialized()");
		ESClient.startClient();
		logger.debug("Exiting ContextListener.contextInitialized()");

	}

	public void contextDestroyed(ServletContextEvent sce) {
		logger.debug("Entering ContextListener.contextDestroyed()");
		ESClient.stopClient();
		logger.debug("Exiting ContextListener.contextDestroyed()");
		
	}
	

}
