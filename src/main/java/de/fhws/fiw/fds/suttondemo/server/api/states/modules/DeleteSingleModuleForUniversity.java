package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.delete.AbstractDeleteState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class DeleteSingleModuleForUniversity extends AbstractDeleteState<Response, Module> {
    private long universityId;

    public DeleteSingleModuleForUniversity(ServiceContext serviceContext, long universityId, long modelIdToDelete) {
        super(serviceContext, modelIdToDelete);
        this.universityId = universityId;
        this.suttonResponse = new JerseyResponse<>();
    }

    @Override
    protected SingleModelResult<Module> loadModel() {
        return DaoFactory.getInstance().getModuleDao().readByIdAndUniversityId(this.modelIdToDelete, this.universityId);
    }

    @Override
    protected NoContentResult deleteModel() {
        return DaoFactory.getInstance().getModuleDao().delete(this.modelIdToDelete);
    }

    @Override
    protected void defineTransitionLinks() {

    }
}
