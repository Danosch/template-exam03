package de.fhws.fiw.fds.suttondemo.server.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import de.fhws.fiw.fds.sutton.client.utils.Link;
import de.fhws.fiw.fds.sutton.server.api.hyperlinks.annotations.SecondarySelfLink;
import de.fhws.fiw.fds.sutton.server.api.hyperlinks.annotations.SelfLink;
import de.fhws.fiw.fds.sutton.server.models.AbstractModel;
import jakarta.xml.bind.annotation.XmlRootElement;

@JsonRootName("partnerUniversity")
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement(name = "partnerUniversity")
public class PartnerUniversity extends AbstractModel {

    private String universityName;
    private String country;
    private String departmentName;
    private String websiteUrl;
    private String contactPerson;
    private int outgoingStudents;
    private int incomingStudents;
    private String nextSpringSemesterStart;
    private String nextAutumnSemesterStart;

    @SecondarySelfLink(
            primaryPathElement = "partnerUniversities",
            secondaryPathElement = "modules"
    )
    private transient Link selfLinkOnSecond;

    @SelfLink(pathElement = "partnerUniversities")
    private transient Link selfLink;

    public PartnerUniversity() {
        // make JPA happy
    }

    public PartnerUniversity(String universityName, String country, String departmentName, String websiteUrl, String contactPerson, int outgoingStudents, int incomingStudents, String nextSpringSemesterStart, String nextAutumnSemesterStart) {
        this.universityName = universityName;
        this.country = country;
        this.departmentName = departmentName;
        this.websiteUrl = websiteUrl;
        this.contactPerson = contactPerson;
        this.outgoingStudents = outgoingStudents;
        this.incomingStudents = incomingStudents;
        this.nextSpringSemesterStart = nextSpringSemesterStart;
        this.nextAutumnSemesterStart = nextAutumnSemesterStart;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public int getOutgoingStudents() {
        return outgoingStudents;
    }

    public void setOutgoingStudents(int outgoingStudents) {
        this.outgoingStudents = outgoingStudents;
    }

    public int getIncomingStudents() {
        return incomingStudents;
    }

    public void setIncomingStudents(int incomingStudents) {
        this.incomingStudents = incomingStudents;
    }

    public String getNextSpringSemesterStart() {
        return nextSpringSemesterStart;
    }

    public void setNextSpringSemesterStart(String nextSpringSemesterStart) {
        this.nextSpringSemesterStart = nextSpringSemesterStart;
    }

    public String getNextAutumnSemesterStart() {
        return nextAutumnSemesterStart;
    }

    public void setNextAutumnSemesterStart(String nextAutumnSemesterStart) {
        this.nextAutumnSemesterStart = nextAutumnSemesterStart;
    }

    public Link getSelfLinkOnSecond() {
        return selfLinkOnSecond;
    }

    public void setSelfLinkOnSecond(Link selfLinkOnSecond) {
        this.selfLinkOnSecond = selfLinkOnSecond;
    }

    public Link getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(Link selfLink) {
        this.selfLink = selfLink;
    }
}
