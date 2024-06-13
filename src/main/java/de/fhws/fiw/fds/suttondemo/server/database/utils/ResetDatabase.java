package de.fhws.fiw.fds.suttondemo.server.database.utils;

import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;

public class ResetDatabase {

    public static void resetAll() {
        DaoFactory.getInstance().getPartnerUniversityDao().resetDatabase();
        DaoFactory.getInstance().getModuleDao().resetDatabase();
    }

}
