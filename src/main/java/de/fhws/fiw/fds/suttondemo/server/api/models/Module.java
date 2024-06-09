package de.fhws.fiw.fds.suttondemo.server.api.models;

import de.fhws.fiw.fds.sutton.server.models.AbstractModel;

public class Module extends AbstractModel {
    private String moduleName;
    private int semester;
    private int creditPoints;
    private PartnerUniversity partnerUniversity;

    public Module() {
    }

    // Getter und Setter Methoden
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
}
