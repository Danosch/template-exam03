package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.caching.CachingUtils;
import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.put.AbstractPutState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class PutSingleModuleForUniversity extends AbstractPutState<Response, Module> {
    private long universityId;

    public PutSingleModuleForUniversity(ServiceContext serviceContext, long universityId, long moduleId, Module modelToUpdate) {
        super(serviceContext, moduleId, modelToUpdate);
        this.universityId = universityId;
        this.suttonResponse = new JerseyResponse<>();
    }

    @Override
    protected SingleModelResult<Module> loadModel() {
        return DaoFactory.getInstance().getModuleDao().readByIdAndUniversityId(this.modelToUpdate.getId(), this.universityId);
    }

    @Override
    protected NoContentResult updateModel() {
        return DaoFactory.getInstance().getModuleDao().update(this.modelToUpdate);
    }

    @Override
    protected void defineHttpCaching() {
        this.suttonResponse.cacheControl(CachingUtils.create30SecondsPublicCaching());
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(String.format("%s/%d/modules/%d", ModuleUri.REL_PATH, universityId, this.modelToUpdate.getId()), ModuleRelTypes.UPDATE_SINGLE_MODULE, getAcceptRequestHeader());
    }
}
