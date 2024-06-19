package de.fhws.fiw.fds.suttondemo.client.rest;

import de.fhws.fiw.fds.sutton.client.rest2.AbstractRestClient;
import de.fhws.fiw.fds.sutton.client.utils.Link;
import de.fhws.fiw.fds.suttondemo.client.models.ModuleClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.web.ModuleWebClient;
import de.fhws.fiw.fds.suttondemo.client.web.PartnerUniversityWebClient;
import de.fhws.fiw.fds.suttondemo.server.api.states.modules.ModuleUri;
import de.fhws.fiw.fds.suttondemo.server.api.states.partneruniversities.PartnerUniversityUri;

import java.io.IOException;
import java.util.*;

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
    private static final String GET_SINGLE_PARTNER_UNIVERSITY = "getSinglePartnerUniversity";
    private static final String GET_SINGLE_MODULE = "getSingleModule";

    private List<PartnerUniversityClientModel> currentPartnerUniversityData;
    private int cursorPartnerUniversityData = 0;

    private List<ModuleClientModel> currentModuleData;
    private int cursorModuleData = 0;

    private final PartnerUniversityWebClient partnerUniversityClient;
    private final ModuleWebClient moduleClient;

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
            System.out.println("Dispatcher started, available links: " + getAvailableLinks());
        });
    }

    public boolean isCreatePartnerUniversityAllowed() {
        boolean allowed = isLinkAvailable(CREATE_PARTNER_UNIVERSITY);
        System.out.println("isCreatePartnerUniversityAllowed: " + allowed);
        System.out.println("Available links: " + getAvailableLinks());
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

    public boolean isGetSinglePartnerUniversityAllowed() {
        boolean allowed = isLinkAvailable(GET_SINGLE_PARTNER_UNIVERSITY);
        System.out.println("isGetSinglePartnerUniversityAllowed: " + allowed);
        return allowed;
    }

    public void getSinglePartnerUniversity(long createdId) throws IOException {
        if (isGetSinglePartnerUniversityAllowed()) {
            getSinglePartnerUniversity(getPartnerUniversityUrl(createdId));
        } else {
            throw new IllegalStateException("Get Single Partner University not allowed");
        }
    }

    public void getSinglePartnerUniversity(int index) throws IOException {
        getSinglePartnerUniversity(this.currentPartnerUniversityData.get(index).getSelfLink().getUrl());
    }

    public void getSinglePartnerUniversity(String url) throws IOException {
        processResponse(this.partnerUniversityClient.getSinglePartnerUniversity(url), (response) -> {
            Collection<PartnerUniversityClientModel> responseData = response.getResponseData();
            if (!responseData.isEmpty()) {
                PartnerUniversityClientModel partnerUniversity = responseData.iterator().next();
                this.currentPartnerUniversityData = List.of(partnerUniversity);
                this.cursorPartnerUniversityData = 0;
            }
        });
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

    public boolean isUpdatePartnerUniversityAllowed() {
        boolean allowed = isLinkAvailable(UPDATE_PARTNER_UNIVERSITY);
        System.out.println("isUpdatePartnerUniversityAllowed: " + allowed);
        return allowed;
    }

    public void updatePartnerUniversity(String url, PartnerUniversityClientModel university) throws IOException {
        if (isUpdatePartnerUniversityAllowed()) {
            processResponse(this.partnerUniversityClient.putPartnerUniversity(url, university), (response) -> {
                try {
                    getAllPartnerUniversities(); // Refresh the data
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            });
        } else {
            throw new IllegalStateException("Update Partner University not allowed");
        }
    }

    public boolean isDeletePartnerUniversityAllowed() throws IOException {
        String availableLinks = getAvailableLinks();
        System.out.println("Checking if deletePartnerUniversity is allowed...");
        System.out.println("Available links: " + availableLinks);
        boolean isAllowed = availableLinks.contains("deletePartnerUniversity ->");
        System.out.println("isDeletePartnerUniversityAllowed: " + isAllowed);
        return isAllowed;
    }

    public void deletePartnerUniversity(String url) throws IOException {
        if (isDeletePartnerUniversityAllowed()) {
            processResponse(this.partnerUniversityClient.deletePartnerUniversity(url), (response) -> {
                try {
                    getAllPartnerUniversities(); // Refresh the data
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            });
        } else {
            throw new IllegalStateException("Delete Partner University not allowed");
        }
    }

    public boolean isCreateModuleAllowed() {
        boolean allowed = isLinkAvailable(CREATE_MODULE);
        System.out.println("isCreateModuleAllowed: " + allowed);
        return allowed;
    }

    public void createModule(ModuleClientModel module, long partnerUniversityId) throws IOException {
        if (isCreateModuleAllowed()) {
            String moduleUrl = getModuleCreationUrl(partnerUniversityId);
            processResponse(this.moduleClient.postNewModule(moduleUrl, module), (response) -> {
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

    public void getAllModules(long partnerUniversityId) throws IOException {
        if (isGetAllModulesAllowed()) {
            String moduleUrl = getAllModulesUrl(partnerUniversityId);
            processResponse(this.moduleClient.getCollectionOfModules(moduleUrl), (response) -> {
                this.currentModuleData = new LinkedList<>(response.getResponseData());
                this.cursorModuleData = 0;
            });
        } else {
            throw new IllegalStateException("Get All Modules not allowed");
        }
    }

    public boolean isGetSingleModuleAllowed() {
        boolean allowed = isLinkAvailable(GET_SINGLE_MODULE);
        System.out.println("isGetSingleModuleAllowed: " + allowed);
        return allowed;
    }

    public void getSingleModule(long universityId, long moduleId) throws IOException {
        if (isGetSingleModuleAllowed()) {
            getSingleModule(getModuleUrl(universityId, moduleId));
        } else {
            throw new IllegalStateException("Get Single Module not allowed");
        }
    }

    public void getSingleModule(String url) throws IOException {
        processResponse(this.moduleClient.getSingleModule(url), (response) -> {
            Collection<ModuleClientModel> responseData = response.getResponseData();
            if (!responseData.isEmpty()) {
                ModuleClientModel module = responseData.iterator().next();
                this.currentModuleData = List.of(module);
                this.cursorModuleData = 0;
            }
        });
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

    public boolean isUpdateModuleAllowed() {
        boolean allowed = isLinkAvailable(UPDATE_MODULE);
        System.out.println("isUpdateModuleAllowed: " + allowed);
        return allowed;
    }

    public void updateModule(String url, ModuleClientModel module) throws IOException {
        if (isUpdateModuleAllowed()) {
            processResponse(this.moduleClient.putModule(url, module), (response) -> {
                try {
                    getAllModules(module.getPartnerUniversityId()); // Refresh the data
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            });
        } else {
            throw new IllegalStateException("Update Module not allowed");
        }
    }

    public boolean isDeleteModuleAllowed() {
        boolean allowed = isLinkAvailable(DELETE_MODULE);
        System.out.println("isDeleteModuleAllowed: " + allowed);
        return allowed;
    }

    public void deleteModule(String url) throws IOException {
        if (isDeleteModuleAllowed()) {
            processResponse(this.moduleClient.deleteModule(url), (response) -> {
                long partnerUniversityId = extractPartnerUniversityIdFromModuleUrl(url);
                try {
                    getAllModules(partnerUniversityId); // Refresh the data
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            });
        } else {
            throw new IllegalStateException("Delete Module not allowed");
        }
    }

    public String getModuleCreationUrl(long partnerUniversityId) {
        return String.format("%s/partneruniversities/%d/modules", BASE_URL, partnerUniversityId);
    }

    public String getAllModulesUrl(long partnerUniversityId) {
        return String.format("%s/partneruniversities/%d/modules", BASE_URL, partnerUniversityId);
    }

    public String getModuleUrl(long universityId, long moduleId) {
        return String.format("%s/partneruniversities/%d/modules/%d", BASE_URL, universityId, moduleId);
    }

    public String getPartnerUniversityUrl(long universityId) {
        return String.format("%s/partneruniversities/%d", BASE_URL, universityId);
    }

    private long extractPartnerUniversityIdFromModuleUrl(String url) {
        String[] parts = url.split("/");
        return Long.parseLong(parts[parts.length - 3]);
    }

    public String getAvailableLinks() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Link> entry : getPossibleNextStates().entrySet()) {
            builder.append(entry.getKey()).append(" -> ").append(entry.getValue().getUrl()).append("\n");
        }
        return builder.toString();
    }
}
