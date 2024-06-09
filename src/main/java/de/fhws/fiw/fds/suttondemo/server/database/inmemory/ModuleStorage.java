package de.fhws.fiw.fds.suttondemo.server.database.inmemory;

import de.fhws.fiw.fds.sutton.server.database.inmemory.AbstractInMemoryStorage;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.ModuleDao;

import java.util.stream.Collectors;

public class ModuleStorage extends AbstractInMemoryStorage<Module> implements ModuleDao {
    @Override
    public NoContentResult create(final Module model) {
        this.storage.put(model.getId(), model);
        return new NoContentResult();
    }

    @Override
    public NoContentResult update(final Module model) {
        if (this.storage.containsKey(model.getId())) {
            this.storage.put(model.getId(), model);
            return new NoContentResult();
        }
        return new NoContentResult();
    }

    @Override
    public NoContentResult delete(final long id) {
        this.storage.remove(id);
        return new NoContentResult();
    }

    @Override
    public SingleModelResult<Module> readByIdAndUniversityId(long id, long universityId) {
        Module module = this.storage.values().stream()
                .filter(m -> m.getId() == id && m.getPartnerUniversity() != null && m.getPartnerUniversity().getId() == universityId)
                .findFirst()
                .orElse(null);
        return new SingleModelResult<>(module);
    }

    @Override
    public CollectionModelResult<Module> readAllByUniversityId(long universityId) {
        return new CollectionModelResult<>(this.storage.values().stream()
                .filter(m -> m.getPartnerUniversity() != null && m.getPartnerUniversity().getId() == universityId)
                .collect(Collectors.toList()));
    }
}
