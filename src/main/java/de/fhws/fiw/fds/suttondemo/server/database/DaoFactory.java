package de.fhws.fiw.fds.suttondemo.server.database;

import de.fhws.fiw.fds.suttondemo.server.database.inmemory.*;

public class DaoFactory {
    private static DaoFactory INSTANCE;


    private final PartnerUniversityDao partnerUniversityDao;
    private final ModuleDao moduleDao;

    private DaoFactory() {
        this.partnerUniversityDao = new PartnerUniversityStorage();
        this.moduleDao = new ModuleStorage();
    }

    public static synchronized DaoFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoFactory();
        }
        return INSTANCE;
    }

    public PartnerUniversityDao getPartnerUniversityDao() {
        return partnerUniversityDao;
    }

    public ModuleDao getModuleDao() {
        return moduleDao;
    }
}
