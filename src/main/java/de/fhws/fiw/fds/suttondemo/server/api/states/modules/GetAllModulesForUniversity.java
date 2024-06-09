package de.fhws.fiw.fds.suttondemo.server.api.states.modules;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractQuery;
import de.fhws.fiw.fds.sutton.server.api.queries.PagingBehaviorUsingOffsetSize;
import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.get.AbstractGetCollectionState;
import de.fhws.fiw.fds.sutton.server.database.DatabaseException;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.suttondemo.server.api.models.Module;
import de.fhws.fiw.fds.suttondemo.server.database.DaoFactory;
import jakarta.ws.rs.core.Response;

public class GetAllModulesForUniversity extends AbstractGetCollectionState<Response, Module> {

    public GetAllModulesForUniversity(ServiceContext serviceContext, AbstractQuery<Response, Module> query) {
        super(serviceContext, query);
        this.suttonResponse = new JerseyResponse<>();
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(String.format("%s/%d/modules", ModuleUri.REL_PATH, ((AllModulesForUniversityQuery<?>) this.query).getUniversityId()), ModuleRelTypes.CREATE_MODULE, getAcceptRequestHeader());
    }

    public static class AllModulesForUniversityQuery<R> extends AbstractQuery<R, Module> {
        private final long universityId;

        public AllModulesForUniversityQuery(long universityId, int offset, int size) {
            this.universityId = universityId;
            this.pagingBehavior = new PagingBehaviorUsingOffsetSize<>(offset, size);
        }

        public long getUniversityId() {
            return universityId;
        }

        @Override
        protected CollectionModelResult<Module> doExecuteQuery(SearchParameter searchParameter) throws DatabaseException {
            return DaoFactory.getInstance().getModuleDao().readAllByUniversityId(universityId);
        }
    }
}
