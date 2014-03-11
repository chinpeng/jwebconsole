package org.jwebconsole.client.service.host;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jwebconsole.client.model.base.SimpleResponse;
import org.jwebconsole.client.model.host.HostConnection;
import org.jwebconsole.client.model.host.HostConnectionListResponse;
import org.jwebconsole.client.model.host.HostConnectionResponse;
import org.jwebconsole.client.service.ServiceConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(ServiceConstants.SERVICE_PATH + "hosts")
@Consumes(MediaType.APPLICATION_JSON)
public interface HostService extends RestService {

    @GET
    @Path("/get/{id}")
    void getHost(@PathParam("id") String id, MethodCallback<HostConnectionResponse> callback);

    @GET
    @Path("/all")
    void getHostsStatus(MethodCallback<HostConnectionListResponse> callback);

    @POST
    @Path("/add")
    void addNewHost(HostConnection connection, MethodCallback<HostConnectionResponse> callback);

    @PUT
    @Path("/edit/{id}")
    void editHost(@PathParam("id") String id, HostConnection connection, MethodCallback<HostConnectionResponse> callback);

    @DELETE
    @Path("/delete/{id}")
    void delete(@PathParam("id") String id, MethodCallback<SimpleResponse> callback);

}
