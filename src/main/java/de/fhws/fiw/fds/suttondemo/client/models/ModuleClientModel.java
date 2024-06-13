package de.fhws.fiw.fds.suttondemo.client.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.fhws.fiw.fds.sutton.client.converters.ClientLinkJsonConverter;
import de.fhws.fiw.fds.sutton.client.model.AbstractClientModel;
import de.fhws.fiw.fds.sutton.client.utils.Link;

public class ModuleClientModel extends AbstractClientModel {
    private String moduleName;
    private int semester;
    private int creditPoints;
    private long partnerUniversityId;

    @JsonDeserialize(using = ClientLinkJsonConverter.class)
    private transient Link selfLink;

    // Getters and Setters
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public long getPartnerUniversityId() {
        return partnerUniversityId;
    }

    public void setPartnerUniversityId(long partnerUniversityId) {
        this.partnerUniversityId = partnerUniversityId;
    }

    public Link getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(Link selfLink) {
        this.selfLink = selfLink;
    }

    @Override
    public String toString() {
        return "ModuleClientModel{" +
                "moduleName='" + moduleName + '\'' +
                ", semester=" + semester +
                ", creditPoints=" + creditPoints +
                ", partnerUniversityId=" + partnerUniversityId +
                ", selfLink=" + selfLink +
                '}';
    }
}
