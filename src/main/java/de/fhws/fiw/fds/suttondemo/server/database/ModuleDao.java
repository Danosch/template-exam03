package de.fhws.fiw.fds.suttondemo.server.database;

import de.fhws.fiw.fds.sutton.server.database.IDatabaseAccessObject;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;

public interface ModuleDao extends IDatabaseAccessObject<Module> {
    SingleModelResult<Module> readByIdAndUniversityId(long id, long universityId);
    CollectionModelResult<Module> readAllByUniversityId(long universityId);
}
