package de.fhws.fiw.fds.suttondemo.client.web;

import de.fhws.fiw.fds.sutton.client.web.GenericWebClient;
import de.fhws.fiw.fds.sutton.client.web.WebApiResponse;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;

import java.io.IOException;

public class PartnerUniversityWebClient {

    private final GenericWebClient<PartnerUniversityClientModel> client;

    public PartnerUniversityWebClient() {
        this.client = new GenericWebClient<>();
    }

    public PartnerUniversityWebResponse getDispatcher(String url) throws IOException {
        return createResponse(this.client.sendGetSingleRequest(url));
    }

    public PartnerUniversityWebResponse getSinglePartnerUniversity(String url) throws IOException {
        return createResponse(this.client.sendGetSingleRequest(url, PartnerUniversityClientModel.class));
    }

    public PartnerUniversityWebResponse getCollectionOfPartnerUniversities(String url) throws IOException {
        return createResponse(this.client.sendGetCollectionRequest(url, PartnerUniversityClientModel.class));
    }

    public PartnerUniversityWebResponse postNewPartnerUniversity(String url, PartnerUniversityClientModel partnerUniversity)
            throws IOException {
        return createResponse(this.client.sendPostRequest(url, partnerUniversity));
    }

    public PartnerUniversityWebResponse putPartnerUniversity(String url, PartnerUniversityClientModel partnerUniversity) throws IOException {
        return createResponse(this.client.sendPutRequest(url, partnerUniversity));
    }

    public PartnerUniversityWebResponse deletePartnerUniversity(String url) throws IOException {
        return createResponse(this.client.sendDeleteRequest(url));
    }

    public PartnerUniversityWebResponse resetDatabaseOnServer(String url) throws IOException {
        return createResponse(this.client.sendGetSingleRequest(url + "/resetdatabase"));
    }

    private PartnerUniversityWebResponse createResponse(WebApiResponse<PartnerUniversityClientModel> response) {
        return new PartnerUniversityWebResponse(response.getResponseData(), response.getResponseHeaders(),
                response.getLastStatusCode());
    }
}
