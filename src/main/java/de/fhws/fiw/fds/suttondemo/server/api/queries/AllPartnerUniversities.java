package de.fhws.fiw.fds.suttondemo.server.api.queries;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractQuery;
import de.fhws.fiw.fds.sutton.server.api.queries.PagingBehaviorUsingOffsetSize;
import de.fhws.fiw.fds.sutton.server.database.DatabaseException;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import de.fhws.fiw.fds.suttondemo.server.database.PartnerUniversityDao;
import de.fhws.fiw.fds.suttondemo.server.database.inmemory.PartnerUniversityStorage;

public class AllPartnerUniversities<R> extends AbstractQuery<R, PartnerUniversity> {
    private String sortParameter;
    private String sortOrder;
    private String country;
    private String name;

    public AllPartnerUniversities(int offset, int size, String sortParameter, String sortOrder, String country, String name) {
        this.pagingBehavior = new PagingBehaviorUsingOffsetSize<>(offset, size);
        this.sortParameter = sortParameter;
        this.sortOrder = sortOrder;
        this.country = country;
        this.name = name;
    }

    @Override
    protected CollectionModelResult<PartnerUniversity> doExecuteQuery(SearchParameter searchParameter) throws DatabaseException {
        searchParameter.setOrderByAttribute(sortParameter + " " + sortOrder);

        PartnerUniversityDao dao = DaoFactory.getInstance().getPartnerUniversityDao();

        if (dao instanceof PartnerUniversityStorage) {
            PartnerUniversityStorage storage = (PartnerUniversityStorage) dao;
            storage.setCountryFilter(country);
            storage.setNameFilter(name);
        }

        return dao.readAll(searchParameter);
    }
}
