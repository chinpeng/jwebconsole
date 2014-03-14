package org.jwebconsole.client.service.thread;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jwebconsole.client.model.thread.details.ThreadDetailsListResponse;
import org.jwebconsole.client.service.ServiceConstants;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path(ServiceConstants.SERVICE_PATH + "thread/details")
@Consumes(MediaType.APPLICATION_JSON)
public interface ThreadDetailsService extends RestService {

    @GET
    @Path("/{hostId}/{threadId}")
    void getThreadDetails(
            @PathParam("hostId") String hostId,
            @PathParam("threadId") Long threadId,
            MethodCallback<ThreadDetailsListResponse> callback);

}
