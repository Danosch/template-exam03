package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractQuery;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.get.AbstractGetCollectionState;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import jakarta.ws.rs.core.Response;

public class GetAllModules extends AbstractGetCollectionState<Response, Module> {
    private final long universityId;

    public GetAllModules(ServiceContext serviceContext, long universityId, AbstractQuery<Response, Module> query) {
        super(serviceContext, query);
        this.universityId = universityId;
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.CREATE_MODULE, getAcceptRequestHeader(), this.universityId);
    }
}
