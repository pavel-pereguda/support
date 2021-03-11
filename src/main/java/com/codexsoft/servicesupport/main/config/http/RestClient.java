package com.codexsoft.servicesupport.main.config.http;

import com.codexsoft.servicesupport.main.config.exception.ServiceClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class RestClient {

    private static final String BEARER = "Bearer";
    private static final String AUTHORIZATION = "Authorization";
    private final Client jerseyClient;

    @Autowired
    public RestClient(final Client client) {
        this.jerseyClient = client;
    }

    @Value("#{dw.apiConfiguration.machineToken}")
    private String machineToken;

    public final Function<String, MultivaluedMap<String, Object>> userAuthHeader = token -> {
        MultivaluedMap<String, Object> result = new MultivaluedHashMap<>(1);
        if (token != null) {
            result.add(AUTHORIZATION, BEARER + " " + token);
        } else {
            result.add(AUTHORIZATION, BEARER + " " + machineToken);
        }
        return result;
    };

    public final Supplier<MultivaluedMap<String, Object>> machineTokenHeader = new Supplier<>() {
        @Override
        public MultivaluedMap<String, Object> get() {
            MultivaluedMap<String, Object> result = new MultivaluedHashMap<>(1);
            result.add(AUTHORIZATION, BEARER + " " + machineToken);
            return result;
        }
    };


    public Response executeGetRequest(String targetUrl, MediaType mediaType, MultivaluedMap<String, Object> headers) {
        return jerseyClient.target(targetUrl)
                .request(mediaType)
                .headers(headers)
                .buildGet()
                .invoke();
    }

    private Response executeEntityRequest(String method, String targetUrl, MediaType mediaType, MultivaluedMap<String, Object> headers, Entity<?> entity) {
        return jerseyClient.target(targetUrl)
                .request(mediaType)
                .headers(headers)
                .build(method, entity)
                .invoke();
    }


    public <T> T doGet(String targetUrl, MediaType mediaType, MultivaluedMap<String, Object> headers, Class<T> cast) {
        var response = this.executeGetRequest(targetUrl, mediaType, headers);
        return this.fetchResultAndClose(response, cast);
    }

    public <T> T doPost(String targetUrl, MediaType mediaType, MultivaluedMap<String, Object> headers, Entity<?> entity, Class<T> cast) {
        return this.sendEntityRequest("POST", targetUrl, mediaType, headers, entity, cast);
    }

    public <T> T doPut(String targetUrl, MediaType mediaType, MultivaluedMap<String, Object> headers, Entity<?> entity, Class<T> cast) {
        return this.sendEntityRequest("PUT", targetUrl, mediaType, headers, entity, cast);
    }

    public <T> T doDelete(String targetUrl, MediaType mediaType, MultivaluedMap<String, Object> headers, Class<T> cast) {
        Response response = jerseyClient.target(targetUrl)
                .request(mediaType)
                .headers(headers)
                .buildDelete()
                .invoke();
        return fetchResultAndClose(response, cast);
    }

    private <T> T sendEntityRequest(String method, String targetUrl, MediaType mediaType, MultivaluedMap<String, Object> headers, Entity<?> entity, Class<T> castTo) {
        var response = this.executeEntityRequest(method, targetUrl, mediaType, headers, entity);
        return this.fetchResultAndClose(response, castTo);
    }


    public <T> T fetchResultAndClose(Response response, Class<T> cast) {
        try (response) {
            if (isOk(response)) {
                return response.readEntity(cast);
            } else {
                throw new ServiceClientException(String.format(
                        "Respond with not OK status, %s", response.getHeaders())
                );
            }
        }
    }


    private boolean isOk(Response response) {
        return response.getStatusInfo().toEnum() == Response.Status.OK;
    }

}
