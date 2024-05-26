package de.fhws.fiw.fds.suttondemo.client.rest;


import de.fhws.fiw.fds.sutton.client.rest2.AbstractRestClient;
import de.fhws.fiw.fds.suttondemo.client.models.LocationClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PersonClientModel;
import de.fhws.fiw.fds.suttondemo.client.web.PersonWebClient;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DemoRestClient extends AbstractRestClient {
    private static final String BASE_URL = "http://localhost:8080/demo/api";
    private static final String GET_ALL_PERSONS = "getAllPersons";
    private static final String CREATE_PERSON = "createPerson";
    private static final String FILTER_PERSONS_BY_NAMES = "filterPersonsByNames";
    private static final String NEXT_PAGE = "next";

    private List<PersonClientModel> currentPersonData;
    private int cursorPersonData = 0;

    private List<LocationClientModel> currentLocationData;
    private int cursorLocationData = 0;

    final private PersonWebClient client;

    public DemoRestClient() {
        super();
        this.client = new PersonWebClient();
        this.currentPersonData = Collections.EMPTY_LIST;
    }

    public void resetDatabase() throws IOException {
        processResponse(this.client.resetDatabaseOnServer(BASE_URL), (response) -> {
        });
    }

    public void fillDatabase() throws IOException
    {
        processResponse( this.client.fillDatabase(BASE_URL), (response) -> {} );
    }
    public void start() throws IOException {
        processResponse(this.client.getDispatcher(BASE_URL), (response) -> {
        });
    }

    public boolean isCreatePersonAllowed() {
        return isLinkAvailable(CREATE_PERSON);
    }

    public void createPerson(PersonClientModel person) throws IOException {
        if (isCreatePersonAllowed()) {
            processResponse(this.client.postNewPerson(getUrl(CREATE_PERSON), person), (response) -> {
                this.currentPersonData = Collections.EMPTY_LIST;
                this.cursorPersonData = 0;
            });
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isGetAllPersonsAllowed() {
        return isLinkAvailable(GET_ALL_PERSONS);
    }

    public void getAllPersons() throws IOException {
        if (isGetAllPersonsAllowed()) {
            processResponse(this.client.getCollectionOfPersons(getUrl(GET_ALL_PERSONS)), (response) -> {
                this.currentPersonData = new LinkedList<>(response.getResponseData());
                this.cursorPersonData = 0;
            });
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isGetByFirstNameAndLastNameAllowed() {
        return isLinkAvailable( FILTER_PERSONS_BY_NAMES );
    }

    public void getByFirstNameAndLastName(String firstName, String lastName) throws IOException {
        if (isGetByFirstNameAndLastNameAllowed()) {
            var url = getUrl( FILTER_PERSONS_BY_NAMES );
            url = url.replace("{FIRSTNAME}", firstName);
            url = url.replace("{LASTNAME}", lastName);
            processResponse(this.client.getCollectionOfPersons(url), (response) -> {
                this.currentPersonData = new LinkedList<>(response.getResponseData());
                this.cursorPersonData = 0;
            });
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isGetSinglePersonAllowed() {
        return !this.currentPersonData.isEmpty() || isLocationHeaderAvailable();
    }

    public List<PersonClientModel> personData() {
        if (this.currentPersonData.isEmpty()) {
            throw new IllegalStateException();
        }

        return this.currentPersonData;
    }

    public void setPersonCursor(int index) {
        if (0 <= index && index < this.currentPersonData.size()) {
            this.cursorPersonData = index;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void getSinglePerson() throws IOException {
        if (isLocationHeaderAvailable()) {
            getSinglePerson(getLocationHeaderURL());
        } else if (!this.currentPersonData.isEmpty()) {
            getSinglePerson(this.cursorPersonData);
        } else {
            throw new IllegalStateException();
        }
    }

    public void getSinglePerson(int index) throws IOException {
        getSinglePerson(this.currentPersonData.get(index).getSelfLink().getUrl());
    }

    private void getSinglePerson(String url) throws IOException {
        processResponse(this.client.getSinglePerson(url), (response) -> {
            this.currentPersonData = new LinkedList(response.getResponseData());
            this.cursorPersonData = 0;
        });
    }

    /*
     *  The rest of the class is omitted for brevity
     */


    public boolean isNextPageAvailable() throws IOException
    {
        return isLinkAvailable(NEXT_PAGE);
    }
}
