package de.fhws.fiw.fds.suttondemo.server.api.models;

import de.fhws.fiw.fds.sutton.server.models.AbstractModel;

public class Module extends AbstractModel {
    private String moduleName;
    private int semester;
    private int creditPoints;
    private long partnerUniversityId; // Partner University ID

    // Getter and Setter methods
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
}
