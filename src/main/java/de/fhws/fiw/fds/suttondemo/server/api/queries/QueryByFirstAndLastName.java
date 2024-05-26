// Copyright 2022 Peter Braun
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package de.fhws.fiw.fds.suttondemo.server.api.queries;

import de.fhws.fiw.fds.sutton.server.api.queries.AbstractQuery;
import de.fhws.fiw.fds.sutton.server.api.queries.PagingBehaviorUsingOffsetSize;
import de.fhws.fiw.fds.sutton.server.database.DatabaseException;
import de.fhws.fiw.fds.sutton.server.database.SearchParameter;
import de.fhws.fiw.fds.sutton.server.database.results.CollectionModelResult;
import de.fhws.fiw.fds.suttondemo.server.DaoFactory;
import de.fhws.fiw.fds.suttondemo.server.api.models.Person;

public class QueryByFirstAndLastName<R> extends AbstractQuery<R, Person> {

    private final String firstName;
    private final String lastName;
    private final String order;

    public QueryByFirstAndLastName(String firstName, String lastName, String order, int offset, int size) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.order = order;
        this.pagingBehavior = new PagingBehaviorUsingOffsetSize<>(offset, size);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOrder() {
        return order;
    }

    public String getOrderAttribute() {
        return this.order.substring(1);
    }

    public String getNextOrderAttributeForLastName() {
        return inverseSortingOrderOrDefault("lastname");
    }

    public String getNextOrderAttributeForFirstName() {
        return inverseSortingOrderOrDefault("firstname");
    }

    @Override
    protected CollectionModelResult<Person> executeQuery() {
        return super.executeQuery();
    }

    protected CollectionModelResult<Person> doExecuteQuery(SearchParameter searchParameter) throws DatabaseException {
        searchParameter.setOrderByAttribute(this.order);
        return DaoFactory.getInstance().getPersonDao().readByFirstNameAndLastName(
                this.firstName,
                this.lastName,
                searchParameter);
    }

    private String inverseSortingOrderOrDefault(String orderAttribute) {
        if (getOrderAttribute().equals(orderAttribute)) {
            return inverseSortingOrder();
        } else {
            return "%2B" + orderAttribute;
        }
    }

    private String inverseSortingOrder() {
        return this.order.startsWith("+") || this.order.startsWith("%2B") ? "-" + this.order.substring(1) : "%2B" + this.order.substring(1);
    }
}
