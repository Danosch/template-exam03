package de.fhws.fiw.fds.suttondemo.server.api.states.partneruniversities;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.delete.AbstractDeleteState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class DeleteSinglePartnerUniversity extends AbstractDeleteState<Response, PartnerUniversity> {

    public DeleteSinglePartnerUniversity(ServiceContext serviceContext, long modelIdToDelete) {
        super(serviceContext, modelIdToDelete);
        this.suttonResponse = new JerseyResponse<>();
    }

    @Override
    protected SingleModelResult<PartnerUniversity> loadModel() {
        return DaoFactory.getInstance().getPartnerUniversityDao().readById(this.modelIdToDelete);
    }

    @Override
    protected NoContentResult deleteModel() {
        // Delete associated modules first
        DaoFactory.getInstance().getModuleDao().deleteByUniversityId(this.modelIdToDelete);
        // Then delete the partner university
        return DaoFactory.getInstance().getPartnerUniversityDao().delete(this.modelIdToDelete);
    }

    @Override
    protected void defineTransitionLinks() {

    }
}
