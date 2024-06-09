package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.suttondemo.Start;

public interface ModuleUri {
    String PATH_ELEMENT = "modules";
    String REL_PATH = Start.CONTEXT_PATH + "/api/partneruniversities/{universityId}/" + PATH_ELEMENT;
    String REL_PATH_ID = REL_PATH + "/{id}";
}
