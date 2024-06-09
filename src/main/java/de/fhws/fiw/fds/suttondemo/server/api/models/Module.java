package de.fhws.fiw.fds.suttondemo.server.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import de.fhws.fiw.fds.sutton.client.utils.Link;
import de.fhws.fiw.fds.sutton.server.api.hyperlinks.annotations.SelfLink;
import de.fhws.fiw.fds.sutton.server.models.AbstractModel;
import jakarta.xml.bind.annotation.XmlRootElement;

@JsonRootName("module")
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement(name = "module")
public class Module extends AbstractModel {

    private String moduleName;
    private int semester;
    private int creditPoints;
    private PartnerUniversity partnerUniversity;

    @SelfLink(pathElement = "modules")
    private transient Link selfLink;

    public Module() {
        // Constructor for JPA
    }

    public Module(String moduleName, int semester, int creditPoints, PartnerUniversity partnerUniversity) {
        if (partnerUniversity == null) {
            throw new IllegalArgumentException("Module cannot exist without a partner university.");
        }
        this.moduleName = moduleName;
        this.semester = semester;
        this.creditPoints = creditPoints;
        this.partnerUniversity = partnerUniversity;
        partnerUniversity.addModule(this);
    }

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

    public PartnerUniversity getPartnerUniversity() {
        return partnerUniversity;
    }

    public void setPartnerUniversity(PartnerUniversity partnerUniversity) {
        this.partnerUniversity = partnerUniversity;
    }

    public Link getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(Link selfLink) {
        this.selfLink = selfLink;
    }
}
