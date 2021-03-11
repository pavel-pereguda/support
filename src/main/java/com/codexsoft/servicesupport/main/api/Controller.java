package com.codexsoft.servicesupport.main.api;

import com.codexsoft.servicesupport.main.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@RequiredArgsConstructor
public class Controller {


    private final SupportService supportService;

    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFinanceFile(@Context SecurityContext securityContext,
                                    @QueryParam("previewImage") Boolean isPreviewImage) throws IOException, InterruptedException {

        System.out.println("Start async check");
            supportService.checkAsync("");
            supportService.checkAsyncPooled("");
        System.out.println("Main thread work" + Thread.currentThread().getName());
        Thread.sleep(1000);
        System.out.println("Main thread after 1000 delay " + Thread.currentThread().getName());


        return null; //TODO replace this stub to something useful
    }

}
