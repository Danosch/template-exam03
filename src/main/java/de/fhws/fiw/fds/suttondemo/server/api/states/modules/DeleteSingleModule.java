package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.delete.AbstractDeleteRelationState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class DeleteSingleModule extends AbstractDeleteRelationState<Response, Module> {
    public DeleteSingleModule(ServiceContext serviceContext, long primaryId, long modelIdToDelete) {
        super(serviceContext, modelIdToDelete, primaryId);
    }

    @Override
    protected SingleModelResult<Module> loadModel() {
        return DaoFactory.getInstance().getModuleDao().readById(this.primaryId, this.modelIdToDelete);
    }

    @Override
    protected NoContentResult deleteModel() {
        return DaoFactory.getInstance().getModuleDao().delete(this.modelIdToDelete);
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.GET_ALL_MODULES, getAcceptRequestHeader(), this.primaryId);
    }
}
