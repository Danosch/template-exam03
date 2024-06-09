package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.post.AbstractPostState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class PostNewModule extends AbstractPostState<Response, Module> {
    private long universityId;

    public PostNewModule(ServiceContext serviceContext, long universityId, Module modelToStore) {
        super(serviceContext, modelToStore);
        this.universityId = universityId;
        this.suttonResponse = new JerseyResponse<>();
    }

    @Override
    protected NoContentResult saveModel() {
        // Ensure that the module is associated with the correct university
        modelToStore.setPartnerUniversity(DaoFactory.getInstance().getPartnerUniversityDao().readById(universityId).getResult());
        return DaoFactory.getInstance().getModuleDao().create(modelToStore);
    }

    @Override
    protected void defineTransitionLinks() {
        // Define any transition links after creating a module
    }
}
