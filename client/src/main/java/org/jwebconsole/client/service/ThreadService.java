package org.jwebconsole.client.service;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jwebconsole.client.model.host.HostConnectionListResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path(ServiceConstants.SERVICE_PATH + "thread")
@Consumes(MediaType.APPLICATION_JSON)
public interface ThreadService extends RestService {

    @GET
    @Path("/{id}/all")
    void getHostsStatus(@PathParam("id") String hostId, MethodCallback<HostConnectionListResponse> callback);

}
