package de.fhws.fiw.fds.suttondemo.client.web;

import de.fhws.fiw.fds.sutton.client.web.WebApiResponse;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import okhttp3.Headers;

import java.util.Collection;

public class PartnerUniversityWebResponse extends WebApiResponse<PartnerUniversityClientModel> {

    public PartnerUniversityWebResponse(final Collection<PartnerUniversityClientModel> responseData, final Headers headers, final int lastStatusCode) {
        super(responseData, headers, lastStatusCode);
    }
}
