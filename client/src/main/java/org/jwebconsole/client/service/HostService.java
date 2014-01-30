package org.jwebconsole.client.service;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("../hosts")
@Consumes(MediaType.APPLICATION_JSON)
public interface HostService extends RestService {

    @GET
    @Path("/status/all")
    public void getHostStatus(MethodCallback<List<HostConnection>> callback);

    @POST
    @Path("/listen")
    public void addNewHost(HostConnection connection, MethodCallback<HostConnectionResponse> callback);

}
