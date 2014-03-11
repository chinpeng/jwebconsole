package org.jwebconsole.client.service.summary;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.jwebconsole.client.model.summary.SummaryResponse;
import org.jwebconsole.client.model.thread.info.ThreadInfoListResponse;
import org.jwebconsole.client.service.ServiceConstants;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path(ServiceConstants.SERVICE_PATH + "summary")
@Consumes(MediaType.APPLICATION_JSON)
public interface SummaryService extends RestService {

    @GET
    @Path("/get/{hostId}")
    void getSummary(@PathParam("hostId") String hostId, MethodCallback<SummaryResponse> callback);

}
