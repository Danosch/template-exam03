package de.fhws.fiw.fds.sutton.server;

import de.fhws.fiw.fds.suttondemo.client.models.ModuleClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.rest.DemoRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PartnerUniversityAndModulesIT {
    private DemoRestClient client;

    @BeforeEach
    public void setUp() throws IOException {
        this.client = new DemoRestClient();
        this.client.resetDatabase();
    }

    @Test
    public void test_create_partner_university_and_module() throws IOException {
        client.start();

        // Create Partner University
        var university = new PartnerUniversityClientModel();
        university.setUniversityName("Test University");
        university.setCountry("Germany");
        university.setDepartmentName("Computer Science");
        university.setWebsiteUrl("http://testuniversity.com");
        university.setContactPerson("John Doe");
        university.setOutgoingStudents(10);
        university.setIncomingStudents(5);
        university.setNextSpringSemesterStart("2024-04-01");
        university.setNextAutumnSemesterStart("2024-10-01");

        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode());

        // Create Module
        var module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(6);
        module.setPartnerUniversityId(1); // Assuming the partner university ID is 1 for this test

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());
    }

    @Test
    public void test_read_partner_university_and_module() throws IOException {
        client.start();

        // Create Partner University
        var university = new PartnerUniversityClientModel();
        university.setUniversityName("Test University");
        university.setCountry("Germany");
        university.setDepartmentName("Computer Science");
        university.setWebsiteUrl("http://testuniversity.com");
        university.setContactPerson("John Doe");
        university.setOutgoingStudents(10);
        university.setIncomingStudents(5);
        university.setNextSpringSemesterStart("2024-04-01");
        university.setNextAutumnSemesterStart("2024-10-01");

        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode());

        // Read Partner University
        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());
        assertNotNull(client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals("Test University"))
                .findFirst()
                .orElse(null));

        // Create Module
        var module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(6);
        module.setPartnerUniversityId(1); // Assuming the partner university ID is 1 for this test

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());

        // Read Module
        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        assertNotNull(client.moduleData().stream()
                .filter(m -> m.getModuleName().equals("Test Module"))
                .findFirst()
                .orElse(null));
    }

    @Test
    public void test_update_partner_university_and_module() throws IOException {
        client.start();

        // Create Partner University
        var university = new PartnerUniversityClientModel();
        university.setUniversityName("Test University");
        university.setCountry("Germany");
        university.setDepartmentName("Computer Science");
        university.setWebsiteUrl("http://testuniversity.com");
        university.setContactPerson("John Doe");
        university.setOutgoingStudents(10);
        university.setIncomingStudents(5);
        university.setNextSpringSemesterStart("2024-04-01");
        university.setNextAutumnSemesterStart("2024-10-01");

        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode());

        // Update Partner University
        university.setDepartmentName("Mathematics");
        client.updatePartnerUniversity(client.getPartnerUniversityUrl(1), university); // Adjust URL if needed
        assertEquals(200, client.getLastStatusCode());

        // Verify Update
        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());
        assertEquals("Mathematics", client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals("Test University"))
                .findFirst()
                .orElse(null)
                .getDepartmentName());

        // Create Module
        var module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(6);
        module.setPartnerUniversityId(1); // Assuming the partner university ID is 1 for this test

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());

        // Update Module
        module.setCreditPoints(8);
        client.updateModule(client.getModuleUrl(1), module); // Adjust URL if needed
        assertEquals(200, client.getLastStatusCode());

        // Verify Update
        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        assertEquals(8, client.moduleData().stream()
                .filter(m -> m.getModuleName().equals("Test Module"))
                .findFirst()
                .orElse(null)
                .getCreditPoints());
    }

    @Test
    public void test_delete_partner_university_and_module() throws IOException {
        client.start();

        // Create Partner University
        var university = new PartnerUniversityClientModel();
        university.setUniversityName("Test University");
        university.setCountry("Germany");
        university.setDepartmentName("Computer Science");
        university.setWebsiteUrl("http://testuniversity.com");
        university.setContactPerson("John Doe");
        university.setOutgoingStudents(10);
        university.setIncomingStudents(5);
        university.setNextSpringSemesterStart("2024-04-01");
        university.setNextAutumnSemesterStart("2024-10-01");

        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode());

        // Create Module
        var module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(6);
        module.setPartnerUniversityId(1); // Assuming the partner university ID is 1 for this test

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());

        // Delete Module
        client.deleteModule(client.getModuleUrl(1)); // Adjust URL if needed
        assertEquals(200, client.getLastStatusCode());

        // Verify Delete
        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        assertNull(client.moduleData().stream()
                .filter(m -> m.getModuleName().equals("Test Module"))
                .findFirst()
                .orElse(null));

        // Delete Partner University
        client.deletePartnerUniversity(client.getPartnerUniversityUrl(1)); // Adjust URL if needed
        assertEquals(200, client.getLastStatusCode());

        // Verify Delete
        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());
        assertNull(client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals("Test University"))
                .findFirst()
                .orElse(null));
    }
}
