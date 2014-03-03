package org.jwebconsole.client.service;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jwebconsole.client.model.thread.info.ThreadInfoListResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path(ServiceConstants.SERVICE_PATH + "thread/info")
@Consumes(MediaType.APPLICATION_JSON)
public interface ThreadInfoService extends RestService {

    @GET
    @Path("/all/{hostId}")
    void getThreadInfo(@PathParam("hostId") String hostId, MethodCallback<ThreadInfoListResponse> callback);

}
