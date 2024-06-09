package de.fhws.fiw.fds.suttondemo.server.database.inmemory;

import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.inmemory.AbstractInMemoryStorage;
import de.fhws.fiw.fds.sutton.server.database.inmemory.InMemoryPaging;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.ModuleDao;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModuleStorage extends AbstractInMemoryStorage<Module> implements ModuleDao {

    @Override
    public CollectionModelResult<Module> readByUniversityId(long universityId, SearchParameter searchParameter) {
        Predicate<Module> predicate = module -> module.getPartnerUniversityId() == universityId;
        CollectionModelResult<Module> filteredResult = new CollectionModelResult<>(
                storage.values().stream().filter(predicate).collect(Collectors.toList())
        );
        return InMemoryPaging.page(
                filteredResult,
                searchParameter.getOffset(),
                searchParameter.getSize()
        );
    }

    @Override
    public SingleModelResult<Module> readById(long universityId, long moduleId) {
        Module result = storage.values().stream()
                .filter(module -> module.getPartnerUniversityId() == universityId && module.getId() == moduleId)
                .findFirst().orElse(null);
        return new SingleModelResult<>(result);
    }
}
