package de.fhws.fiw.fds.suttondemo.server.database;

import de.fhws.fiw.fds.suttondemo.server.database.inmemory.*;

public class DaoFactory {

    private static DaoFactory INSTANCE;

    private final PersonDao personDao;
    private final LocationDao locationDao;
    private final PersonLocationDao personLocationDao;
    private final PartnerUniversityDao partnerUniversityDao;
    private final ModuleDao moduleDao;

    private DaoFactory() {
        this.personDao = new PersonStorage();
        this.locationDao = new LocationStorage();
        this.personLocationDao = new PersonLocationStorage(this.locationDao);
        this.partnerUniversityDao = new PartnerUniversityStorage(); // Richtiger Klassenname verwendet
        this.moduleDao = new ModuleStorage(); // Richtiger Klassenname verwendet
    }

    public static DaoFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoFactory();
        }
        return INSTANCE;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public LocationDao getLocationDao() {
        return locationDao;
    }

    public PersonLocationDao getPersonLocationDao() {
        return personLocationDao;
    }

    public PartnerUniversityDao getPartnerUniversityDao() {
        return partnerUniversityDao;
    }

    public ModuleDao getModuleDao() {
        return moduleDao;
    }
}