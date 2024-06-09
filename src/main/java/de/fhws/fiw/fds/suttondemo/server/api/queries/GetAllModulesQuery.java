package de.fhws.fiw.fds.suttondemo.server.api.queries;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractQuery;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;

public class GetAllModulesQuery extends AbstractQuery<CollectionModelResult<Module>, Module> {
    private final long universityId;

    public GetAllModulesQuery(long universityId) {
        this.universityId = universityId;
    }

    @Override
    protected CollectionModelResult<Module> doExecuteQuery(SearchParameter searchParameter) {
        return DaoFactory.getInstance().getModuleDao().readByUniversityId(universityId, searchParameter);
    }
}
