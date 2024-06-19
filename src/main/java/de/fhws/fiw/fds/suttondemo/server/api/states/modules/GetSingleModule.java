package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.get.AbstractGetRelationState;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetSingleModule extends AbstractGetRelationState<Response, Module> {
    private static final Logger LOGGER = Logger.getLogger(GetSingleModule.class.getName());

    public GetSingleModule(ServiceContext serviceContext, long primaryId, long requestedId) {
        super(serviceContext, primaryId, requestedId);
        this.suttonResponse = new JerseyResponse<>(); // Ensure suttonResponse is initialized
    }

    @Override
    protected SingleModelResult<Module> loadModel() {
        try {
            return DaoFactory.getInstance().getModuleDao().readById(this.primaryId, this.requestedId);
        } catch (Exception e) {
            // Log the exception and return an empty SingleModelResult
            LOGGER.log(Level.SEVERE, "Error loading module", e);
            return new SingleModelResult<>(); // Return an empty result
        }
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(ModuleUri.REL_PATH_ID, ModuleRelTypes.GET_SINGLE_MODULE, getAcceptRequestHeader(), this.primaryId, this.requestedId);
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.GET_ALL_MODULES, getAcceptRequestHeader(), this.primaryId);
        addLink(ModuleUri.REL_PATH_ID, ModuleRelTypes.UPDATE_SINGLE_MODULE, getAcceptRequestHeader(), this.primaryId, this.requestedId);
        addLink(ModuleUri.REL_PATH_ID, ModuleRelTypes.DELETE_SINGLE_MODULE, getAcceptRequestHeader(), this.primaryId, this.requestedId);
    }
}
