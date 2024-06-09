package de.fhws.fiw.fds.suttondemo.server.api.services;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.Exceptions.SuttonWebAppException;
import de.fhws.fiw.fds.sutton.server.api.services.AbstractJerseyService;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;
import de.fhws.fiw.fds.suttondemo.server.api.queries.AllPartnerUniversities;
import de.fhws.fiw.fds.suttondemo.server.api.states.partneruniversities.*;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("partneruniversities")
public class PartnerUniversityJerseyService extends AbstractJerseyService {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllPartnerUniversities(@QueryParam("offset") @DefaultValue("0") int offset,
                                              @QueryParam("size") @DefaultValue("20") int size,
                                              @QueryParam("sort") @DefaultValue("id") String sort) {
        try {
            return new GetAllPartnerUniversities(this.serviceContext, new AllPartnerUniversities<>(offset, size, sort)).execute();
        } catch (SuttonWebAppException e) {
            throw new WebApplicationException(Response.status(e.getStatus().getCode())
                    .entity(e.getExceptionMessage()).build());
        }
    }

    @GET
    @Path("{id: \\d+}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSinglePartnerUniversity(@PathParam("id") final long id) {
        try {
            return new GetSinglePartnerUniversity(this.serviceContext, id).execute();
        } catch (SuttonWebAppException e) {
            throw new WebApplicationException(Response.status(e.getStatus().getCode())
                    .entity(e.getExceptionMessage()).build());
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createSinglePartnerUniversity(final PartnerUniversity partnerUniversityModel) {
        try {
            return new PostNewPartnerUniversity(this.serviceContext, partnerUniversityModel).execute();
        } catch (SuttonWebAppException e) {
            throw new WebApplicationException(Response.status(e.getStatus().getCode())
                    .entity(e.getExceptionMessage()).build());
        }
    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateSinglePartnerUniversity(@PathParam("id") final long id, final PartnerUniversity partnerUniversityModel) {
        try {
            return new PutSinglePartnerUniversity(this.serviceContext, id, partnerUniversityModel).execute();
        } catch (SuttonWebAppException e) {
            throw new WebApplicationException(Response.status(e.getStatus().getCode())
                    .entity(e.getExceptionMessage()).build());
        }
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response deleteSinglePartnerUniversity(@PathParam("id") final long id) {
        try {
            return new DeleteSinglePartnerUniversity(this.serviceContext, id).execute();
        } catch (SuttonWebAppException e) {
            throw new WebApplicationException(Response.status(e.getStatus().getCode())
                    .entity(e.getExceptionMessage()).build());
        }
    }
}
