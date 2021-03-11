package com.codexsoft.servicesupport.main.config.email;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;

@Component("emailMessageSender")
@AllArgsConstructor
public class EmailMessageSender implements MessageSender
{

    private final Client jerseyClient;

    private final Logger logger = LoggerFactory.getLogger(EmailMessageSender.class);

    private static final String HEADER_VALUE_PATTERN = "name= %s, url= %s, destination= %s, subject=%s";
}
