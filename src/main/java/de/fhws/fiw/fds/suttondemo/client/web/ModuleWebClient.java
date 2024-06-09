package de.fhws.fiw.fds.suttondemo.client.web;

import de.fhws.fiw.fds.sutton.client.web.GenericWebClient;
import de.fhws.fiw.fds.sutton.client.web.WebApiResponse;
import de.fhws.fiw.fds.suttondemo.client.models.ModuleClientModel;

import java.io.IOException;

public class ModuleWebClient {

    private final GenericWebClient<ModuleClientModel> client;

    public ModuleWebClient() {
        this.client = new GenericWebClient<>();
    }

    public ModuleWebResponse getSingleModule(String url) throws IOException {
        return createResponse(this.client.sendGetSingleRequest(url, ModuleClientModel.class));
    }

    public ModuleWebResponse getCollectionOfModules(String url) throws IOException {
        return createResponse(this.client.sendGetCollectionRequest(url, ModuleClientModel.class));
    }

    public ModuleWebResponse postNewModule(String url, ModuleClientModel module) throws IOException {
        return createResponse(this.client.sendPostRequest(url, module));
    }

    public ModuleWebResponse putModule(String url, ModuleClientModel module) throws IOException {
        return createResponse(this.client.sendPutRequest(url, module));
    }

    public ModuleWebResponse deleteModule(String url) throws IOException {
        return createResponse(this.client.sendDeleteRequest(url));
    }

    private ModuleWebResponse createResponse(WebApiResponse<ModuleClientModel> response) {
        return new ModuleWebResponse(response.getResponseData(), response.getResponseHeaders(), response.getLastStatusCode());
    }
}
