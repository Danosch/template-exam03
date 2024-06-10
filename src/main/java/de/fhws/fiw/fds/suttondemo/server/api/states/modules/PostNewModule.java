package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.post.AbstractPostRelationState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class PostNewModule extends AbstractPostRelationState<Response, Module> {
    public PostNewModule(ServiceContext serviceContext, long primaryId, Module modelToStore) {
        super(serviceContext, primaryId, modelToStore);
    }

    @Override
    protected NoContentResult saveModel() {
        return DaoFactory.getInstance().getModuleDao().create(this.modelToStore);
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.GET_ALL_MODULES, getAcceptRequestHeader(), this.primaryId);
    }
}
