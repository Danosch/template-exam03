package de.fhws.fiw.fds.suttondemo.server.database;

import de.fhws.fiw.fds.suttondemo.server.database.inmemory.ModuleStorage;
import de.fhws.fiw.fds.suttondemo.server.database.inmemory.PartnerUniversityStorage;

public class DatabaseManager {
    private static final PartnerUniversityStorage partnerUniversityStorage = new PartnerUniversityStorage();
    private static final ModuleStorage moduleStorage = new ModuleStorage();

    public static void resetAllDatabases() {
        partnerUniversityStorage.resetDatabase();
        moduleStorage.resetDatabase();
    }

    public static PartnerUniversityDao getPartnerUniversityStorage() {
        return partnerUniversityStorage;
    }

    public static ModuleDao getModuleStorage() {
        return moduleStorage;
    }
}
