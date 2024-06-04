package de.fhws.fiw.fds.suttondemo.server.api.queries;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractQuery;
import de.fhws.fiw.fds.sutton.server.api.queries.PagingBehaviorUsingOffsetSize;
import de.fhws.fiw.fds.sutton.server.database.DatabaseException;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;

public class AllModules<R> extends AbstractQuery<R, Module> {
    private String sortParameter; // Add additional parameters as needed for filtering

    public AllModules(int offset, int size, String sortParameter) {
        this.pagingBehavior = new PagingBehaviorUsingOffsetSize<>(offset, size);
        this.sortParameter = sortParameter;
    }

    protected CollectionModelResult<Module> doExecuteQuery(SearchParameter searchParameter) throws DatabaseException {
        searchParameter.setOrderByAttribute(sortParameter);
        return DaoFactory.getInstance().getModuleDao().readAll(searchParameter);
    }
}
