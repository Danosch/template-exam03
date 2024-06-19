package de.fhws.fiw.fds.suttondemo.server.database.inmemory;

import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.inmemory.AbstractInMemoryStorage;
import de.fhws.fiw.fds.sutton.server.database.inmemory.InMemoryPaging;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.ModuleDao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModuleStorage extends AbstractInMemoryStorage<Module> implements ModuleDao {

    @Override
    public CollectionModelResult<Module> readByUniversityId(long universityId, SearchParameter searchParameter) {
        Predicate<Module> predicate = module -> module.getPartnerUniversityId() == universityId;
        List<Module> filteredList = storage.values().stream().filter(predicate).collect(Collectors.toList());

        if (searchParameter.getOrderByAttribute() != null && !searchParameter.getOrderByAttribute().isEmpty()) {
            String[] orderParts = searchParameter.getOrderByAttribute().split(" ");
            String sortAttribute = orderParts[0];
            boolean descending = orderParts.length > 1 && "desc".equalsIgnoreCase(orderParts[1]);

            Comparator<Module> comparator;
            switch (sortAttribute) {
                case "moduleName":
                    comparator = Comparator.comparing(Module::getModuleName);
                    break;
                case "creditPoints":
                    comparator = Comparator.comparingInt(Module::getCreditPoints);
                    break;
                default:
                    comparator = Comparator.comparingLong(Module::getId);
                    break;
            }

            if (descending) {
                comparator = comparator.reversed();
            }

            filteredList.sort(comparator);
        }

        return InMemoryPaging.page(new CollectionModelResult<>(filteredList), searchParameter.getOffset(), searchParameter.getSize());
    }

    @Override
    public SingleModelResult<Module> readById(long universityId, long moduleId) {
        Module result = storage.values().stream()
                .filter(module -> module.getPartnerUniversityId() == universityId && module.getId() == moduleId)
                .findFirst().orElse(null);
        return new SingleModelResult<>(result);
    }

    @Override
    public NoContentResult create(final Module model) {
        model.setId(nextId());
        this.storage.put(model.getId(), model);
        return new NoContentResult();
    }

    @Override
    public NoContentResult update(final Module model) {
        this.storage.put(model.getId(), model);
        return new NoContentResult();
    }

    @Override
    public NoContentResult delete(long id) {
        this.storage.remove(id);
        return new NoContentResult();
    }

    @Override
    public NoContentResult deleteByUniversityId(long universityId) {
        storage.values().removeIf(module -> module.getPartnerUniversityId() == universityId);
        return new NoContentResult();
    }

    @Override
    public void resetDatabase() {
        this.storage.clear();
    }

    @Override
    public void initializeDatabase() {
        create(new Module("Introduction to AI", 1, 5, 1));
        create(new Module("Advanced Databases", 2, 5, 1));
    }

    private long nextId() {
        try {
            java.lang.reflect.Method method = AbstractInMemoryStorage.class.getDeclaredMethod("nextId");
            method.setAccessible(true);
            return (long) method.invoke(this);
        } catch (Exception e) {
            throw new RuntimeException("Unable to access nextId method", e);
        }
    }
}
