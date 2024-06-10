package de.fhws.fiw.fds.suttondemo.server.api.queries;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractRelationQuery;
import de.fhws.fiw.fds.sutton.server.api.queries.PagingBehaviorUsingOffsetSize;
import de.fhws.fiw.fds.sutton.server.database.DatabaseException;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;

public class QueryByUniversityId<R> extends AbstractRelationQuery<R, Module> {

    private final long universityId;

    public QueryByUniversityId(long universityId, int offset, int size) {
        super(universityId);
        this.universityId = universityId;
        this.pagingBehavior = new PagingBehaviorUsingOffsetSize<>(offset, size);
    }

    @Override
    protected CollectionModelResult<Module> doExecuteQuery(SearchParameter searchParameter) throws DatabaseException {
        return DaoFactory.getInstance().getModuleDao().readByUniversityId(this.universityId, searchParameter);
    }
}
