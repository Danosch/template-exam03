package de.fhws.fiw.fds.suttondemo.server.api.states.partneruniversities;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.post.AbstractPostState;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class PostNewPartnerUniversity extends AbstractPostState<Response, PartnerUniversity> {

    public PostNewPartnerUniversity(ServiceContext serviceContext, PartnerUniversity modelToStore) {
        super(serviceContext, modelToStore);
        this.suttonResponse = new JerseyResponse<>();
    }

    @Override
    protected NoContentResult saveModel() {
        return DaoFactory.getInstance().getPartnerUniversityDao().create(this.modelToStore);
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(PartnerUniversityUri.REL_PATH, PartnerUniversityRelTypes.GET_ALL_PARTNER_UNIVERSITIES, getAcceptRequestHeader());
    }
}
