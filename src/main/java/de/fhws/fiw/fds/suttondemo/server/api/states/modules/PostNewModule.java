package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.post.AbstractPostState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class PostNewModule extends AbstractPostState<Response, Module> {
    public PostNewModule(ServiceContext serviceContext, Module modelToStore) {
        super(serviceContext, modelToStore);
        this.suttonResponse = new JerseyResponse<>(); // Initialisieren der suttonResponse
    }

    @Override
    protected NoContentResult saveModel() {
        return DaoFactory.getInstance().getModuleDao().create(this.modelToStore);
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.GET_ALL_MODULES, getAcceptRequestHeader(), this.modelToStore.getPartnerUniversityId());
    }
}
