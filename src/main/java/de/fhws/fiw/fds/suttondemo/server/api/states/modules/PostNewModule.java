package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.post.AbstractPostRelationState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

public class PostNewModule extends AbstractPostRelationState<Response, Module> {
    private static final Logger LOGGER = Logger.getLogger(PostNewModule.class.getName());

    public PostNewModule(ServiceContext serviceContext, long primaryId, Module modelToStore) {
        super(serviceContext, primaryId, modelToStore);
        this.suttonResponse = new JerseyResponse<>(); // Ensure suttonResponse is initialized if not done in base class
    }

    @Override
    protected NoContentResult saveModel() {
        try {
            return DaoFactory.getInstance().getModuleDao().create(this.modelToStore);
        } catch (Exception e) {
            LOGGER.severe("Error saving module: " + e.getMessage());
            throw e;
        }
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.GET_ALL_MODULES, getAcceptRequestHeader(), this.primaryId);
    }

    @Override
    protected Response buildInternal() {
        SingleModelResult<PartnerUniversity> partnerUniversityResult = DaoFactory.getInstance().getPartnerUniversityDao().readById(this.primaryId);

        if (partnerUniversityResult.getResult() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Partner university does not exist").build();
        }

        return super.buildInternal();
    }
}
