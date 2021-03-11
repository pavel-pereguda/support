package com.codexsoft.servicesupport.main.config.email;


import com.codexsoft.servicesupport.main.config.http.RestClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.Arrays;

import static java.util.Arrays.copyOfRange;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailingService {
//
//    @Value(value = "#{dw.mail.list}")
//    private String mailingList;
    @Value(value = "#{dw.apiConfiguration.machineToken}")
    private String accessToken;
    @Value(value = "#{dw.mail.serverUrl}")
    private String url;
//    @Value(value = "#{dw.mail.sender}")
//    private String sender;
    private final RestClient restClient;

    public void sendMail(Message message) {

        if (message.getRecipients() == null && message.getBbcs() != null) {
            message.setRecipients(message.getBbcs()[0]);
            message.setBbcs(copyOfRange(message.getBbcs(), 1, message.getBbcs().length));
        }

        this.doPostRequest(message);
    }

//    public void notifyParsingException(Exception ex, Task task) {
//        Message message = buildMessage(ex, task, new MessageParts("Document loading error", "Load pdf task"));
//    }

//
//    public void notifyUnexpectedSystemException(Throwable ex) {
//        Message message = Message.builder()
//                .subject(ex.getMessage())
//                .content(buildSystemErrorMailText(ex))
//                .bbcs(mailingList.split(","))
//                .sender("service@netfonds.de")
//                .build();
//    }

//
//    private String buildSystemErrorMailText(Throwable ex) {
//        String env = System.getenv(Constants.APPLICATION_ENVIRONMENT_VARIABLE);
//        String result = "Environment: " + env + "\n";
//        return result.concat(getExceptionMessage(ex));
//    }
//
//    private Message buildMessage(Exception e, BaseEntity taskEntity, MessageParts messageParts) {
//        return Message.builder()
//                .bbcs(mailingList.split(","))
//                .content(buildErrorMailMessage(e, taskEntity, messageParts.taskType))
//                .subject(messageParts.messageSubject)
//                .sender("service@netfonds.de")
//                .build();
//    }
//
//    private String buildErrorMailMessage(Exception ex, BaseEntity taskEntity, String taskType) {
//        String env = System.getenv(Constants.APPLICATION_ENVIRONMENT_VARIABLE);
//        return "Task id: " + taskEntity.getId() +
//                "\n" +
//                "Task type: " + taskType +
//                "\n" +
//                "Environment: " + env +
//                "\n" +
//                "Error message: " + getExceptionMessage(ex);
//    }

    private String getExceptionMessage(Throwable ex) {
        StringBuilder result = new StringBuilder();
        result.append(ex.getClass()).append(": ").append(ex.getMessage()).append("\n");

        Arrays.stream(ex.getStackTrace())
                .filter(stackTraceElement -> stackTraceElement.toString().contains("com.codexsoft.fomular"))
                .forEach(stackTraceElement -> result.append("\t")
                        .append(stackTraceElement.toString())
                        .append("\n"));
        return result.toString();
    }


    private void doPostRequest(Message message) {

        try {
            log.info("Start to sent message. User: " + message.getRecipients());

            restClient.doPost(
                    url,
                    MediaType.APPLICATION_JSON_TYPE,
                    restClient.machineTokenHeader.get(),
                    Entity.json(message),
                    String.class
            );

            log.info("Message sent. User: " + message.getRecipients());
        } catch (RuntimeException ex) {
            log.error("error in sending email.User: {} ", message.getRecipients(), ex);
        }
    }

    @AllArgsConstructor
    private static class MessageParts {
        private String messageSubject;
        private String taskType;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Message implements Serializable {
        private String content;
        private String sender;
        private String subject;
        private String recipients;
        private String[] bbcs;
        private static final long serialVersionUID = 4886;
    }


}
