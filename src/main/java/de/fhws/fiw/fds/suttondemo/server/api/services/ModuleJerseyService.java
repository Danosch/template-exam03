package de.fhws.fiw.fds.suttondemo.server.api.services;

import de.fhws.fiw.fds.sutton.server.api.services.AbstractJerseyService;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;
import de.fhws.fiw.fds.suttondemo.server.api.states.modules.*;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("partneruniversities/{universityId}/modules")
public class ModuleJerseyService extends AbstractJerseyService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createModuleForUniversity(@PathParam("universityId") long universityId, Module module) {
        SingleModelResult<PartnerUniversity> result = DaoFactory.getInstance().getPartnerUniversityDao().readById(universityId);
        if (result == null || result.getResult() == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Partneruniversität nicht gefunden").build();
        }
        module.setPartnerUniversity(result.getResult());
        return new PostNewModule(this.serviceContext, universityId, module).execute();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllModulesForUniversity(@PathParam("universityId") long universityId,
                                               @QueryParam("offset") @DefaultValue("0") int offset,
                                               @QueryParam("size") @DefaultValue("20") int size) {
        GetAllModulesForUniversity.AllModulesForUniversityQuery<Response> query = new GetAllModulesForUniversity.AllModulesForUniversityQuery<>(universityId, offset, size);
        return new GetAllModulesForUniversity(this.serviceContext, query).execute();
    }

    @GET
    @Path("{moduleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSingleModule(@PathParam("universityId") long universityId, @PathParam("moduleId") long moduleId) {
        return new GetSingleModuleForUniversity(this.serviceContext, universityId, moduleId).execute();
    }

    @PUT
    @Path("{moduleId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateModuleForUniversity(@PathParam("universityId") long universityId, @PathParam("moduleId") long moduleId, Module module) {
        return new PutSingleModuleForUniversity(this.serviceContext, universityId, moduleId, module).execute();
    }

    @DELETE
    @Path("{moduleId}")
    public Response deleteModule(@PathParam("universityId") long universityId, @PathParam("moduleId") long moduleId) {
        return new DeleteSingleModuleForUniversity(this.serviceContext, universityId, moduleId).execute();
    }
}
