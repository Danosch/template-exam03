package de.fhws.fiw.fds.suttondemo.server.api.services;

import de.fhws.fiw.fds.suttondemo.server.database.DatabaseManager;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("reset")
public class ResetService {

    @POST
    public Response resetDatabase() {
        DatabaseManager.resetAllDatabases();
        return Response.ok().build();
    }
}
