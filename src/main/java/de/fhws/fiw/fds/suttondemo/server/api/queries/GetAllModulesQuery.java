package de.fhws.fiw.fds.suttondemo.server.api.queries;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractQuery;
import de.fhws.fiw.fds.sutton.server.api.queries.PagingBehaviorUsingOffsetSize;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;

public class GetAllModulesQuery extends AbstractQuery<CollectionModelResult<Module>, Module> {
    private final long universityId;
    private String sortParameter;
    private String sortOrder;

    public GetAllModulesQuery(long universityId, int offset, int size, String sortParameter, String sortOrder) {
        this.universityId = universityId;
        this.pagingBehavior = new PagingBehaviorUsingOffsetSize<>(offset, size);
        this.sortParameter = sortParameter;
        this.sortOrder = sortOrder;
    }

    @Override
    protected CollectionModelResult<Module> doExecuteQuery(SearchParameter searchParameter) {
        searchParameter.setOrderByAttribute(sortParameter + " " + sortOrder);
        return DaoFactory.getInstance().getModuleDao().readByUniversityId(universityId, searchParameter);
    }
}
