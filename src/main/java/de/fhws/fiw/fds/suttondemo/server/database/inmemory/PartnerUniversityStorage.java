package de.fhws.fiw.fds.suttondemo.server.database.inmemory;

import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.inmemory.AbstractInMemoryStorage;
import de.fhws.fiw.fds.sutton.server.database.inmemory.InMemoryPaging;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.sutton.server.database.results.NoContentResult;
import de.fhws.fiw.fds.sutton.server.database.results.SingleModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.PartnerUniversity;
import de.fhws.fiw.fds.suttondemo.server.database.PartnerUniversityDao;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PartnerUniversityStorage extends AbstractInMemoryStorage<PartnerUniversity> implements PartnerUniversityDao {

    private String countryFilter;
    private String nameFilter;

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
        DaoFactory.getInstance().getModuleDao().deleteByUniversityId(id);
        return super.delete(id);
    }

    @Override
    public void resetDatabase() {
        this.storage.clear();
    }

    @Override
    public void initializeDatabase() {
        create(new PartnerUniversity("University of AI", "Germany", "Computer Science", "http://uni-ai.example.com", "Dr. John Doe", 10, 5, "01-03-2023", "01-09-2023"));
        create(new PartnerUniversity("Tech University", "USA", "Engineering", "http://techuni.example.com", "Prof. Jane Smith", 20, 15, "01-04-2023", "01-10-2023"));
    }

    public void setCountryFilter(String country) {
        this.countryFilter = country;
    }

    public void setNameFilter(String name) {
        this.nameFilter = name;
    }

    @Override
    public CollectionModelResult<PartnerUniversity> readAll(SearchParameter searchParameter) {
        List<PartnerUniversity> filteredList = new ArrayList<>(storage.values());

        if (countryFilter != null && !countryFilter.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(university -> university.getCountry().equalsIgnoreCase(countryFilter))
                    .collect(Collectors.toList());
        }

        if (nameFilter != null && !nameFilter.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(university -> university.getUniversityName().equalsIgnoreCase(nameFilter))
                    .collect(Collectors.toList());
        }

        if (searchParameter.getOrderByAttribute() != null && !searchParameter.getOrderByAttribute().isEmpty()) {
            String[] orderParts = searchParameter.getOrderByAttribute().split(" ");
            String sortAttribute = orderParts[0];
            boolean descending = orderParts.length > 1 && "desc".equalsIgnoreCase(orderParts[1]);

            Comparator<PartnerUniversity> comparator;
            switch (sortAttribute) {
                case "country":
                    comparator = Comparator.comparing(PartnerUniversity::getCountry);
                    break;
                case "universityName":
                    comparator = Comparator.comparing(PartnerUniversity::getUniversityName);
                    break;
                default:
                    comparator = Comparator.comparing(PartnerUniversity::getId);
                    break;
            }

            if (descending) {
                comparator = comparator.reversed();
            }

            filteredList.sort(comparator);
        }

        return InMemoryPaging.page(new CollectionModelResult<>(filteredList), searchParameter.getOffset(), searchParameter.getSize());
    }
}
