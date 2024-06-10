package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.put.AbstractPutRelationState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class PutSingleModule extends AbstractPutRelationState<Response, Module> {
    public PutSingleModule(ServiceContext serviceContext, long primaryId, long requestedId, Module modelToUpdate) {
        super(serviceContext, primaryId, requestedId, modelToUpdate);
    }

    @Override
    protected SingleModelResult<Module> loadModel() {
        return DaoFactory.getInstance().getModuleDao().readById(this.primaryId, this.requestedId);
    }

    @Override
    protected NoContentResult updateModel() {
        return DaoFactory.getInstance().getModuleDao().update(this.modelToUpdate);
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(ModuleUri.REL_PATH_ID, ModuleRelTypes.GET_SINGLE_MODULE, getAcceptRequestHeader(), this.primaryId, this.requestedId);
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.GET_ALL_MODULES, getAcceptRequestHeader(), this.primaryId);
    }
}
