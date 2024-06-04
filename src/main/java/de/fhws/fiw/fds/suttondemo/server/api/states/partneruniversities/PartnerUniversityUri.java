package de.fhws.fiw.fds.suttondemo.server.api.states.partneruniversities;

import de.fhws.fiw.fds.suttondemo.Start;

public interface PartnerUniversityUri {
    String PATH_ELEMENT = "partneruniversities";
    String REL_PATH = Start.CONTEXT_PATH + "/api/" + PATH_ELEMENT;
    String REL_PATH_ID = REL_PATH + "/{id}";
}
