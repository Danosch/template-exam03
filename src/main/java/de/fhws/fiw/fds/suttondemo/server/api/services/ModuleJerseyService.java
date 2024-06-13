package de.fhws.fiw.fds.suttondemo.server.api.services;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.Exceptions.SuttonWebAppException;
import de.fhws.fiw.fds.sutton.server.api.services.AbstractJerseyService;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.api.queries.QueryByUniversityId;
import de.fhws.fiw.fds.suttondemo.server.api.states.modules.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("partneruniversities/{universityId}/modules")
public class ModuleJerseyService extends AbstractJerseyService {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllModules(@PathParam("universityId") final long universityId,
                                  @QueryParam("offset") @DefaultValue("0") int offset,
                                  @QueryParam("size") @DefaultValue("20") int size) {
        try {
            return new GetAllModules(this.serviceContext, universityId, new QueryByUniversityId<>(universityId, offset, size)).execute();
        } catch (SuttonWebAppException e) {
            throw new WebApplicationException(e.getExceptionMessage(), e.getStatus().getCode());
        }
    }

    @GET
    @Path("{id: \\d+}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSingleModule(@PathParam("universityId") final long universityId, @PathParam("id") final long id) {
        try {
            return new GetSingleModule(this.serviceContext, universityId, id).execute();
        } catch (SuttonWebAppException e) {
            throw new WebApplicationException(e.getExceptionMessage(), e.getStatus().getCode());
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createModule(@PathParam("universityId") final long universityId, final Module module) {
        try {
            module.setPartnerUniversityId(universityId);
            return new PostNewModule(this.serviceContext, universityId, module).execute();
        } catch (SuttonWebAppException e) {
            e.printStackTrace(); // Log the exception
            throw new WebApplicationException(e.getExceptionMessage(), e.getStatus().getCode());
        } catch (Exception e) {
            e.printStackTrace(); // Log unexpected exceptions
            throw new WebApplicationException("Internal server error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateModule(@PathParam("universityId") final long universityId, @PathParam("id") final long id, final Module module) {
        try {
            module.setId(id);
            module.setPartnerUniversityId(universityId);
            return new PutSingleModule(this.serviceContext, universityId, id, module).execute();
        } catch (SuttonWebAppException e) {
            throw new WebApplicationException(e.getExceptionMessage(), e.getStatus().getCode());
        }
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response deleteModule(@PathParam("universityId") final long universityId, @PathParam("id") final long id) {
        try {
            return new DeleteSingleModule(this.serviceContext, universityId, id).execute();
        } catch (SuttonWebAppException e) {
            throw new WebApplicationException(e.getExceptionMessage(), e.getStatus().getCode());
        }
    }
}
