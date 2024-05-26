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

public interface PersonRelTypes {

    String CREATE_PERSON = "createPerson";
    String GET_ALL_PERSONS = "getAllPersons";
    String UPDATE_SINGLE_PERSON = "updatePerson";
    String DELETE_SINGLE_PERSON = "deletePerson";
    String GET_SINGLE_PERSON = "getPerson";
    String FILTER_BY_NAME = "filterPersonsByNames";
    String FILTER_BY_NAME_ORDER_BY_FIRSTNAME = "filterPersonsByNamesOrderByFirstname";
    String FILTER_BY_NAME_ORDER_BY_LASTNAME = "filterPersonsByNamesOrderByLastname";
}
