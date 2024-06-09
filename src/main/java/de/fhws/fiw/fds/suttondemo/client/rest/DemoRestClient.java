package de.fhws.fiw.fds.suttondemo.client.rest;

import de.fhws.fiw.fds.sutton.client.rest2.AbstractRestClient;
import de.fhws.fiw.fds.suttondemo.client.models.ModuleClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PersonClientModel;
import de.fhws.fiw.fds.suttondemo.client.web.ModuleWebClient;
import de.fhws.fiw.fds.suttondemo.client.web.PartnerUniversityWebClient;
import de.fhws.fiw.fds.suttondemo.client.web.PersonWebClient;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DemoRestClient extends AbstractRestClient {
    private static final String BASE_URL = "http://localhost:8080/demo/api";
    private static final String GET_ALL_PERSONS = "getAllPersons";
    private static final String CREATE_PERSON = "createPerson";
    private static final String GET_ALL_PARTNER_UNIVERSITIES = "getAllPartnerUniversities";
    private static final String CREATE_PARTNER_UNIVERSITY = "createPartnerUniversity";
    private static final String GET_ALL_MODULES = "getAllModules";
    private static final String CREATE_MODULE = "createModule";

    private List<PersonClientModel> currentPersonData;
    private int cursorPersonData = 0;

    private List<PartnerUniversityClientModel> currentPartnerUniversityData;
    private int cursorPartnerUniversityData = 0;

    private List<ModuleClientModel> currentModuleData;
    private int cursorModuleData = 0;

    final private PersonWebClient client;
    final private PartnerUniversityWebClient partnerUniversityClient;
    final private ModuleWebClient moduleClient;

    public DemoRestClient() {
        super();
        this.client = new PersonWebClient();
        this.partnerUniversityClient = new PartnerUniversityWebClient();
        this.moduleClient = new ModuleWebClient();
        this.currentPersonData = Collections.emptyList();
        this.currentPartnerUniversityData = Collections.emptyList();
        this.currentModuleData = Collections.emptyList();
    }

    public void resetDatabase() throws IOException {
        processResponse(this.client.resetDatabaseOnServer(BASE_URL), (response) -> {
        });
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
                this.currentPersonData = Collections.emptyList();
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
            this.currentPersonData = new LinkedList<>(response.getResponseData());
            this.cursorPersonData = 0;
        });
    }

    public boolean isCreatePartnerUniversityAllowed() {
        return isLinkAvailable(CREATE_PARTNER_UNIVERSITY);
    }

    public void createPartnerUniversity(PartnerUniversityClientModel partnerUniversity) throws IOException {
        if (isCreatePartnerUniversityAllowed()) {
            processResponse(this.partnerUniversityClient.postNewPartnerUniversity(getUrl(CREATE_PARTNER_UNIVERSITY), partnerUniversity), (response) -> {
                this.currentPartnerUniversityData = Collections.emptyList();
                this.cursorPartnerUniversityData = 0;
            });
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isGetAllPartnerUniversitiesAllowed() {
        return isLinkAvailable(GET_ALL_PARTNER_UNIVERSITIES);
    }

    public void getAllPartnerUniversities() throws IOException {
        if (isGetAllPartnerUniversitiesAllowed()) {
            processResponse(this.partnerUniversityClient.getCollectionOfPartnerUniversities(getUrl(GET_ALL_PARTNER_UNIVERSITIES)), (response) -> {
                this.currentPartnerUniversityData = new LinkedList<>(response.getResponseData());
                this.cursorPartnerUniversityData = 0;
            });
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isGetSinglePartnerUniversityAllowed() {
        return !this.currentPartnerUniversityData.isEmpty() || isLocationHeaderAvailable();
    }

    public List<PartnerUniversityClientModel> partnerUniversityData() {
        if (this.currentPartnerUniversityData.isEmpty()) {
            throw new IllegalStateException();
        }

        return this.currentPartnerUniversityData;
    }

    public void setPartnerUniversityCursor(int index) {
        if (0 <= index && index < this.currentPartnerUniversityData.size()) {
            this.cursorPartnerUniversityData = index;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void getSinglePartnerUniversity() throws IOException {
        if (isLocationHeaderAvailable()) {
            getSinglePartnerUniversity(getLocationHeaderURL());
        } else if (!this.currentPartnerUniversityData.isEmpty()) {
            getSinglePartnerUniversity(this.cursorPartnerUniversityData);
        } else {
            throw new IllegalStateException();
        }
    }

    public void getSinglePartnerUniversity(int index) throws IOException {
        getSinglePartnerUniversity(this.currentPartnerUniversityData.get(index).getSelfLink().getUrl());
    }

    private void getSinglePartnerUniversity(String url) throws IOException {
        processResponse(this.partnerUniversityClient.getSinglePartnerUniversity(url), (response) -> {
            this.currentPartnerUniversityData = new LinkedList<>(response.getResponseData());
            this.cursorPartnerUniversityData = 0;
        });
    }

    public boolean isCreateModuleAllowed() {
        return isLinkAvailable(CREATE_MODULE);
    }

    public void createModule(ModuleClientModel module) throws IOException {
        if (isCreateModuleAllowed()) {
            processResponse(this.moduleClient.postNewModule(getUrl(CREATE_MODULE), module), (response) -> {
                this.currentModuleData = Collections.emptyList();
                this.cursorModuleData = 0;
            });
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isGetAllModulesAllowed() {
        return isLinkAvailable(GET_ALL_MODULES);
    }

    public void getAllModules() throws IOException {
        if (isGetAllModulesAllowed()) {
            processResponse(this.moduleClient.getCollectionOfModules(getUrl(GET_ALL_MODULES)), (response) -> {
                this.currentModuleData = new LinkedList<>(response.getResponseData());
                this.cursorModuleData = 0;
            });
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isGetSingleModuleAllowed() {
        return !this.currentModuleData.isEmpty() || isLocationHeaderAvailable();
    }

    public List<ModuleClientModel> moduleData() {
        if (this.currentModuleData.isEmpty()) {
            throw new IllegalStateException();
        }

        return this.currentModuleData;
    }

    public void setModuleCursor(int index) {
        if (0 <= index && index < this.currentModuleData.size()) {
            this.cursorModuleData = index;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void getSingleModule() throws IOException {
        if (isLocationHeaderAvailable()) {
            getSingleModule(getLocationHeaderURL());
        } else if (!this.currentModuleData.isEmpty()) {
            getSingleModule(this.cursorModuleData);
        } else {
            throw new IllegalStateException();
        }
    }

    public void getSingleModule(int index) throws IOException {
        getSingleModule(this.currentModuleData.get(index).getSelfLink().getUrl());
    }

    private void getSingleModule(String url) throws IOException {
        processResponse(this.moduleClient.getSingleModule(url), (response) -> {
            this.currentModuleData = new LinkedList<>(response.getResponseData());
            this.cursorModuleData = 0;
        });
    }

    public void updateModule(String url, ModuleClientModel module) throws IOException {
        processResponse(this.moduleClient.putModule(url, module), (response) -> {
            // Update current module data if needed
        });
    }

    public void deleteModule(String url) throws IOException {
        processResponse(this.moduleClient.deleteModule(url), (response) -> {
            // Update current module data if needed
        });
    }
}
