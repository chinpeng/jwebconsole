package org.jwebconsole.client.sandbox;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("http://localhost:8080/hosts")
@Consumes(MediaType.APPLICATION_JSON)
public interface HostService extends RestService {

    @GET
    @Path("/status/{host}/{port}")
    public void getHostStatus(@PathParam("host") String host,
                              @PathParam("port") Integer port,
                              MethodCallback<HostStatus> callback);

}
