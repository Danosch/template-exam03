package de.fhws.fiw.fds.suttondemo.server.database.utils;

import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;

public class InitializeDatabase {

    public static void initializeDBWithRelations() {
        DaoFactory.getInstance().getPartnerUniversityDao().initializeDatabase();
        DaoFactory.getInstance().getModuleDao().initializeDatabase();
    }
}
