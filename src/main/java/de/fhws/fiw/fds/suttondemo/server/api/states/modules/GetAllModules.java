package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractRelationQuery;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.get.AbstractGetCollectionRelationState;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import jakarta.ws.rs.core.Response;

public class GetAllModules extends AbstractGetCollectionRelationState<Response, Module> {
    public GetAllModules(ServiceContext serviceContext, long primaryId, AbstractRelationQuery<Response, Module> query) {
        super(serviceContext, primaryId, query);
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.GET_ALL_MODULES, getAcceptRequestHeader(), this.primaryId);
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.CREATE_MODULE, getAcceptRequestHeader(), this.primaryId);
    }
}
