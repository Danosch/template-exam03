package de.fhws.fiw.fds.suttondemo.server.database;

import de.fhws.fiw.fds.sutton.server.database.IDatabaseAccessObject;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;

public interface ModuleDao extends IDatabaseAccessObject<Module> {
    CollectionModelResult<Module> readByUniversityId(long universityId, SearchParameter searchParameter);
    SingleModelResult<Module> readById(long universityId, long moduleId); // Diese Methode hinzufügen
}
