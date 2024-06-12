package de.fhws.fiw.fds.suttondemo.server.database;

import de.fhws.fiw.fds.sutton.server.database.IDatabaseAccessObject;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;

public interface ModuleDao extends IDatabaseAccessObject<Module> {
    CollectionModelResult<Module> readByUniversityId(long universityId, SearchParameter searchParameter);
    SingleModelResult<Module> readById(long universityId, long moduleId);
    NoContentResult deleteByUniversityId(long universityId);
    void resetDatabase();  // Add this method
}
