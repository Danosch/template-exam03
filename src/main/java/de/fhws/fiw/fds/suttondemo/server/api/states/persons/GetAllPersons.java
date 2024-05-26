/*
 * Copyright 2021 University of Applied Sciences Würzburg-Schweinfurt, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.fhws.fiw.fds.suttondemo.server.api.states.persons;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractQuery;
import de.fhws.fiw.fds.sutton.server.api.serviceAdapters.responseAdapter.JerseyResponse;
import de.fhws.fiw.fds.sutton.server.api.services.ServiceContext;
import de.fhws.fiw.fds.sutton.server.api.states.get.AbstractGetCollectionState;
import de.fhws.fiw.fds.suttondemo.server.api.models.Person;
import de.fhws.fiw.fds.suttondemo.server.api.queries.QueryByFirstAndLastName;
import jakarta.ws.rs.core.Response;

public class GetAllPersons extends AbstractGetCollectionState<Response, Person> {

    public GetAllPersons(ServiceContext serviceContext, AbstractQuery<Response, Person> query) {
        super(serviceContext, query);
        this.suttonResponse = new JerseyResponse<>();
    }

    @Override
    protected void defineTransitionLinks() {
        addLink(PersonUri.REL_PATH, PersonRelTypes.CREATE_PERSON, getAcceptRequestHeader());
        addLink(PersonUri.REL_PATH + "?firstname={FIRSTNAME}&lastname={LASTNAME}", PersonRelTypes.FILTER_BY_NAME, getAcceptRequestHeader());

        if (this.query instanceof QueryByFirstAndLastName) {
            var query = (QueryByFirstAndLastName<Response>) this.query;

            if (!query.getFirstName().isEmpty() || !query.getLastName().isEmpty()) {
                addLink(PersonUri.REL_PATH + "?firstname={id}&lastname={id}&order={id}", PersonRelTypes.FILTER_BY_NAME_ORDER_BY_FIRSTNAME, getAcceptRequestHeader(), query.getFirstName(), query.getLastName(), query.getNextOrderAttributeForFirstName());
                addLink(PersonUri.REL_PATH + "?firstname={id}&lastname={id}&order={id}", PersonRelTypes.FILTER_BY_NAME_ORDER_BY_LASTNAME, getAcceptRequestHeader(), query.getFirstName(), query.getLastName(), query.getNextOrderAttributeForLastName());
            }
        }
    }
}
