package de.fhws.fiw.fds.suttondemo.server.api.states.dispatcher;

import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.get.AbstractGetDispatcherState;
import de.fhws.fiw.fds.suttondemo.server.api.states.persons.PersonRelTypes;
import de.fhws.fiw.fds.suttondemo.server.api.states.persons.PersonUri;
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
        // Person links
        addLink(PersonUri.REL_PATH, PersonRelTypes.GET_ALL_PERSONS, "get_all_persons", getAcceptRequestHeader());
        addLink(PersonUri.REL_PATH, PersonRelTypes.CREATE_PERSON, "create_person", getAcceptRequestHeader());

        // Partner University links
        addLink(PartnerUniversityUri.REL_PATH, PartnerUniversityRelTypes.GET_ALL_PARTNER_UNIVERSITIES, "get_all_partner_universities", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH, PartnerUniversityRelTypes.CREATE_PARTNER_UNIVERSITY, "create_partner_university", getAcceptRequestHeader());

        // Module links
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.GET_ALL_MODULES, "get_all_modules", getAcceptRequestHeader());
        addLink(ModuleUri.REL_PATH, ModuleRelTypes.CREATE_MODULE, "create_module", getAcceptRequestHeader());

        // Pagination and Sorting Links for Partner Universities
        addLink(PartnerUniversityUri.REL_PATH + "?offset={offset}&size={size}", "self_partner_universities", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "?offset={nextOffset}&size={size}", "next_partner_universities", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "?offset={prevOffset}&size={size}", "prev_partner_universities", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "?sort=name&order=asc", "sort_asc_partner_universities", getAcceptRequestHeader());
        addLink(PartnerUniversityUri.REL_PATH + "?sort=name&order=desc", "sort_desc_partner_universities", getAcceptRequestHeader());

        // Pagination and Sorting Links for Modules
        addLink(ModuleUri.REL_PATH + "?offset={offset}&size={size}", "self_modules", getAcceptRequestHeader());
        addLink(ModuleUri.REL_PATH + "?offset={nextOffset}&size={size}", "next_modules", getAcceptRequestHeader());
        addLink(ModuleUri.REL_PATH + "?offset={prevOffset}&size={size}", "prev_modules", getAcceptRequestHeader());
        addLink(ModuleUri.REL_PATH + "?sort=moduleName&order=asc", "sort_asc_modules", getAcceptRequestHeader());
        addLink(ModuleUri.REL_PATH + "?sort=moduleName&order=desc", "sort_desc_modules", getAcceptRequestHeader());
    }
}
