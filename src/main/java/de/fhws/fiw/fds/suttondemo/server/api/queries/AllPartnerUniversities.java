package de.fhws.fiw.fds.suttondemo.server.api.queries;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractQuery;
import de.fhws.fiw.fds.sutton.server.api.queries.PagingBehaviorUsingOffsetSize;
import de.fhws.fiw.fds.sutton.server.database.DatabaseException;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;

public class AllPartnerUniversities<R> extends AbstractQuery<R, PartnerUniversity> {
    private String sortParameter;

    public AllPartnerUniversities(int offset, int size, String sortParameter) {
        this.pagingBehavior = new PagingBehaviorUsingOffsetSize<>(offset, size);
        this.sortParameter = sortParameter;
    }

    @Override
    protected CollectionModelResult<PartnerUniversity> doExecuteQuery(SearchParameter searchParameter) throws DatabaseException {
        searchParameter.setOrderByAttribute(sortParameter);
        return DaoFactory.getInstance().getPartnerUniversityDao().readAll(searchParameter);
    }
}
