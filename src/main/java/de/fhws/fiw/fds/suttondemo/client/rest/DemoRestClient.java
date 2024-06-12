package de.fhws.fiw.fds.suttondemo.client.rest;

import de.fhws.fiw.fds.sutton.client.rest2.AbstractRestClient;
import de.fhws.fiw.fds.suttondemo.client.models.ModuleClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.web.ModuleWebClient;
import de.fhws.fiw.fds.suttondemo.client.web.PartnerUniversityWebClient;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DemoRestClient extends AbstractRestClient {
    private static final String BASE_URL = "http://localhost:8080/demo/api";
    private static final String GET_ALL_PARTNER_UNIVERSITIES = "getAllPartnerUniversities";
    private static final String CREATE_PARTNER_UNIVERSITY = "createPartnerUniversity";
    private static final String UPDATE_PARTNER_UNIVERSITY = "updatePartnerUniversity";
    private static final String DELETE_PARTNER_UNIVERSITY = "deletePartnerUniversity";
    private static final String GET_ALL_MODULES = "getAllModules";
    private static final String CREATE_MODULE = "createModule";
    private static final String UPDATE_MODULE = "updateModule";
    private static final String DELETE_MODULE = "deleteModule";

    private List<PartnerUniversityClientModel> currentPartnerUniversityData;
    private int cursorPartnerUniversityData = 0;

    private List<ModuleClientModel> currentModuleData;
    private int cursorModuleData = 0;

    final private PartnerUniversityWebClient partnerUniversityClient;
    final private ModuleWebClient moduleClient;

    public DemoRestClient() {
        super();
        this.partnerUniversityClient = new PartnerUniversityWebClient();
        this.moduleClient = new ModuleWebClient();
        this.currentPartnerUniversityData = Collections.emptyList();
        this.currentModuleData = Collections.emptyList();
    }

    public void resetDatabase() throws IOException {
        processResponse(this.partnerUniversityClient.resetDatabaseOnServer(BASE_URL), (response) -> {});
    }

    public void start() throws IOException {
        processResponse(this.partnerUniversityClient.getDispatcher(BASE_URL), (response) -> {
            System.out.println("Dispatcher started, available links: " + isLinkAvailable(GET_ALL_PARTNER_UNIVERSITIES) + ", " + isLinkAvailable(CREATE_PARTNER_UNIVERSITY) + ", " + isLinkAvailable(GET_ALL_MODULES) + ", " + isLinkAvailable(CREATE_MODULE));
        });
    }

    public boolean isCreatePartnerUniversityAllowed() {
        boolean allowed = isLinkAvailable(CREATE_PARTNER_UNIVERSITY);
        System.out.println("isCreatePartnerUniversityAllowed: " + allowed);
        return allowed;
    }

    public void createPartnerUniversity(PartnerUniversityClientModel partnerUniversity) throws IOException {
        if (isCreatePartnerUniversityAllowed()) {
            processResponse(this.partnerUniversityClient.postNewPartnerUniversity(getUrl(CREATE_PARTNER_UNIVERSITY), partnerUniversity), (response) -> {
                this.currentPartnerUniversityData = Collections.emptyList();
                this.cursorPartnerUniversityData = 0;
            });
        } else {
            throw new IllegalStateException("Create Partner University not allowed");
        }
    }

    public boolean isGetAllPartnerUniversitiesAllowed() {
        boolean allowed = isLinkAvailable(GET_ALL_PARTNER_UNIVERSITIES);
        System.out.println("isGetAllPartnerUniversitiesAllowed: " + allowed);
        return allowed;
    }

    public void getAllPartnerUniversities() throws IOException {
        if (isGetAllPartnerUniversitiesAllowed()) {
            processResponse(this.partnerUniversityClient.getCollectionOfPartnerUniversities(getUrl(GET_ALL_PARTNER_UNIVERSITIES)), (response) -> {
                this.currentPartnerUniversityData = new LinkedList<>(response.getResponseData());
                this.cursorPartnerUniversityData = 0;
            });
        } else {
            throw new IllegalStateException("Get All Partner Universities not allowed");
        }
    }

    public List<PartnerUniversityClientModel> partnerUniversityData() {
        return currentPartnerUniversityData;
    }

    public boolean isCreateModuleAllowed() {
        boolean allowed = isLinkAvailable(CREATE_MODULE);
        System.out.println("isCreateModuleAllowed: " + allowed);
        return allowed;
    }

    public void createModule(ModuleClientModel module) throws IOException {
        if (isCreateModuleAllowed()) {
            processResponse(this.moduleClient.postNewModule(getUrl(CREATE_MODULE), module), (response) -> {
                this.currentModuleData = Collections.emptyList();
                this.cursorModuleData = 0;
            });
        } else {
            throw new IllegalStateException("Create Module not allowed");
        }
    }

    public boolean isGetAllModulesAllowed() {
        boolean allowed = isLinkAvailable(GET_ALL_MODULES);
        System.out.println("isGetAllModulesAllowed: " + allowed);
        return allowed;
    }

    public void getAllModules() throws IOException {
        if (isGetAllModulesAllowed()) {
            processResponse(this.moduleClient.getCollectionOfModules(getUrl(GET_ALL_MODULES)), (response) -> {
                this.currentModuleData = new LinkedList<>(response.getResponseData());
                this.cursorModuleData = 0;
            });
        } else {
            throw new IllegalStateException("Get All Modules not allowed");
        }
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

    public void updatePartnerUniversity(String url, PartnerUniversityClientModel university) throws IOException {
        processResponse(this.partnerUniversityClient.putPartnerUniversity(url, university), (response) -> {
            // Update current partner university data if needed
        });
    }

    public void deletePartnerUniversity(String url) throws IOException {
        processResponse(this.partnerUniversityClient.deletePartnerUniversity(url), (response) -> {
            // Update current partner university data if needed
        });
    }

    public List<ModuleClientModel> moduleData() {
        return currentModuleData;
    }

    public String getModuleUrl(long moduleId) {
        return String.format("%s/modules/%d", BASE_URL, moduleId);
    }

    public String getPartnerUniversityUrl(long universityId) {
        return String.format("%s/partneruniversities/%d", BASE_URL, universityId);
    }
}
