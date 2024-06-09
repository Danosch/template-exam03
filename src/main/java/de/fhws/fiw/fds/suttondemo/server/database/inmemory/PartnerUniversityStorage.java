package de.fhws.fiw.fds.suttondemo.server.database.inmemory;

import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.inmemory.AbstractInMemoryStorage;
import de.fhws.fiw.fds.sutton.server.database.inmemory.InMemoryPaging;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;
import de.fhws.fiw.fds.suttondemo.server.database.PartnerUniversityDao;

public class PartnerUniversityStorage extends AbstractInMemoryStorage<PartnerUniversity> implements PartnerUniversityDao {

    @Override
    public NoContentResult create(PartnerUniversity model) {
        return super.create(model);
    }

    @Override
    public SingleModelResult<PartnerUniversity> readById(long id) {
        return super.readById(id);
    }

    @Override
    public NoContentResult update(PartnerUniversity model) {
        return super.update(model);
    }

    @Override
    public NoContentResult delete(long id) {
        return super.delete(id);
    }

    @Override
    public void resetDatabase() {
        this.storage.clear();
    }

    @Override
    public CollectionModelResult<PartnerUniversity> readAll(SearchParameter searchParameter) {
        return InMemoryPaging.page(new CollectionModelResult<>(storage.values()), searchParameter.getOffset(), searchParameter.getSize());
    }
}
