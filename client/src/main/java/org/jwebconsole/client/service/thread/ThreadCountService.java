package org.jwebconsole.client.service.thread;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jwebconsole.client.model.thread.count.ThreadCountListResponse;
import org.jwebconsole.client.service.ServiceConstants;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path(ServiceConstants.SERVICE_PATH + "thread/count")
@Consumes(MediaType.APPLICATION_JSON)
public interface ThreadCountService extends RestService {

    @GET
    @Path("/all/{hostId}")
    void getThreadCount(@PathParam("hostId") String hostId, MethodCallback<ThreadCountListResponse> callback);

    @GET
    @Path("/last/{number}/{hostId}")
    void getLastNumberOfThreadCount(
            @PathParam("number") Integer number,
            @PathParam("hostId") String hostId,
            MethodCallback<ThreadCountListResponse> callback);

}
