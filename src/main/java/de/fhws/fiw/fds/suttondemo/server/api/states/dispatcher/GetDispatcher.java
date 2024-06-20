package de.fhws.fiw.fds.suttondemo.server.api.states.dispatcher;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.get.AbstractGetDispatcherState;
import de.fhws.fiw.fds.suttondemo.server.api.states.modules.ModuleRelTypes;
import de.fhws.fiw.fds.suttondemo.server.api.states.modules.ModuleUri;
import de.fhws.fiw.fds.suttondemo.server.api.states.partneruniversities.PartnerUniversityRelTypes;
import de.fhws.fiw.fds.suttondemo.server.api.states.partneruniversities.PartnerUniversityUri;
import jakarta.ws.rs.core.Response;

public class GetDispatcher extends AbstractGetDispatcherState<Response> {

    public GetDispatcher(ServiceContext serviceContext) {
        super(serviceContext);
        this.suttonResponse = new JerseyResponse<>();
    }

    @Override
    protected void defineTransitionLinks() {
        // Partner University links
        addLink(PartnerUniversityUri.REL_PATH, PartnerUniversityRelTypes.GET_ALL_PARTNER_UNIVERSITIES, getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH, PartnerUniversityRelTypes.CREATE_PARTNER_UNIVERSITY, getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH_ID, PartnerUniversityRelTypes.GET_SINGLE_PARTNER_UNIVERSITY, getAcceptRequestHeader(), "{id}");
        addLink(PartnerUniversityUri.REL_PATH_ID, PartnerUniversityRelTypes.UPDATE_SINGLE_PARTNER_UNIVERSITY, getAcceptRequestHeader(), "{id}");
        addLink(PartnerUniversityUri.REL_PATH_ID, PartnerUniversityRelTypes.DELETE_SINGLE_PARTNER_UNIVERSITY, getAcceptRequestHeader(), "{id}");

        // Module links under a specific Partner University
        addLink(PartnerUniversityUri.REL_PATH + "/{universityId}/modules", ModuleRelTypes.GET_ALL_MODULES, getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "/{universityId}/modules", ModuleRelTypes.CREATE_MODULE, getAcceptRequestHeader());
        addLink(ModuleUri.REL_PATH_ID, ModuleRelTypes.GET_SINGLE_MODULE, getAcceptRequestHeader(), "{id}");
        addLink(ModuleUri.REL_PATH_ID, ModuleRelTypes.UPDATE_SINGLE_MODULE, getAcceptRequestHeader(), "{id}");
        addLink(ModuleUri.REL_PATH_ID, ModuleRelTypes.DELETE_SINGLE_MODULE, getAcceptRequestHeader(), "{id}");
    }
}
