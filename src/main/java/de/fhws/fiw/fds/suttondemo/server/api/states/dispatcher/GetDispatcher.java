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

        // Pagination and Sorting Links for Partner Universities
        addLink(PartnerUniversityUri.REL_PATH + "?offset={offset}&size={size}", "self_partner_universities", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "?offset={nextOffset}&size={size}", "next_partner_universities", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "?offset={prevOffset}&size={size}", "prev_partner_universities", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "?sort=name&order=asc", "sort_asc_partner_universities", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "?sort=name&order=desc", "sort_desc_partner_universities", getAcceptRequestHeader());

        // Pagination and Sorting Links for Modules
        addLink(PartnerUniversityUri.REL_PATH + "/{universityId}/modules?offset={offset}&size={size}", "self_modules_for_university", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "/{universityId}/modules?offset={nextOffset}&size={size}", "next_modules_for_university", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "/{universityId}/modules?offset={prevOffset}&size={size}", "prev_modules_for_university", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "/{universityId}/modules?sort=moduleName&order=asc", "sort_asc_modules_for_university", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "/{universityId}/modules?sort=moduleName&order=desc", "sort_desc_modules_for_university", getAcceptRequestHeader());
    }
}
