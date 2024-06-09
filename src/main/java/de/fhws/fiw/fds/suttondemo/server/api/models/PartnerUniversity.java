package de.fhws.fiw.fds.suttondemo.server.api.models;

import de.fhws.fiw.fds.sutton.server.models.AbstractModel;
import java.util.ArrayList;
import java.util.List;

public class PartnerUniversity extends AbstractModel {
    private String universityName;
    private String country;
    private String departmentName;
    private String websiteUrl;
    private String contactPerson;
    private int outgoingStudents;
    private int incomingStudents;
    private List<Module> modules = new ArrayList<>();

    public PartnerUniversity() {
    }

    // Getter und Setter Methoden
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

    public List<Module> getModules() {
        return modules;
    }

    public void addModule(Module module) {
        modules.add(module);
    }
}
