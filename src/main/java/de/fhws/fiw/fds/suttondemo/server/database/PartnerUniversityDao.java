package de.fhws.fiw.fds.suttondemo.server.database;

import de.fhws.fiw.fds.sutton.server.database.IDatabaseAccessObject;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;

public interface PartnerUniversityDao extends IDatabaseAccessObject<PartnerUniversity> {
    void resetDatabase();
}
