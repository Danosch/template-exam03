package de.fhws.fiw.fds.suttondemo.server.database.inmemory;

import de.fhws.fiw.fds.sutton.server.database.inmemory.AbstractInMemoryStorage;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.ModuleDao;

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
        } else {
            return new NoContentResult(); // Still return NoContentResult as operation assumes success unless exception
        }
    }

    @Override
    public NoContentResult delete(final long id) {
        this.storage.remove(id);
        return new NoContentResult();
    }
}
