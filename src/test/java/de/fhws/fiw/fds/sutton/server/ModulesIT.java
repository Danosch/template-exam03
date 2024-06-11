package de.fhws.fiw.fds.sutton.server;

import de.fhws.fiw.fds.suttondemo.client.models.ModuleClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.rest.DemoRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModulesIT {
    private DemoRestClient client;

    @BeforeEach
    public void setUp() throws IOException {
        this.client = new DemoRestClient();
        this.client.resetDatabase();
    }

    @Test
    public void test_dispatcher_is_available() throws IOException {
        client.start();
        assertEquals(200, client.getLastStatusCode());
    }

    @Test
    public void test_dispatcher_is_get_all_modules_allowed() throws IOException {
        client.start();
        assertTrue(client.isGetAllModulesAllowed());
    }

    @Test
    public void test_create_module_is_create_module_allowed() throws IOException {
        client.start();
        assertTrue(client.isCreateModuleAllowed());
    }

    @Test
    public void test_create_module() throws IOException {
        client.start();

        // Create a partner university first
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

        // Check if getting all partner universities is allowed
        if (!client.isGetAllPartnerUniversitiesAllowed()) {
            fail("Get All Partner Universities not allowed");
        }

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());

        var universities = client.partnerUniversityData();
        assertFalse(universities.isEmpty(), "No universities found");

        var universityFromServer = universities.get(0);

        // Create a module with the created university ID
        var module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setPartnerUniversityId(universityFromServer.getId());

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());
    }

    @Test
    public void test_create_module_and_get_new_module() throws IOException {
        client.start();

        // Create a partner university first
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

        // Check if getting all partner universities is allowed
        if (!client.isGetAllPartnerUniversitiesAllowed()) {
            fail("Get All Partner Universities not allowed");
        }

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());

        var universities = client.partnerUniversityData();
        assertFalse(universities.isEmpty(), "No universities found");

        var universityFromServer = universities.get(0);

        // Create a module with the created university ID
        var module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setPartnerUniversityId(universityFromServer.getId());

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());
        assertTrue(client.isGetAllModulesAllowed());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());

        var modules = client.moduleData();
        assertFalse(modules.isEmpty(), "No modules found");

        var moduleFromServer = modules.get(0);
        assertEquals("Test Module", moduleFromServer.getModuleName());
    }

    @Test
    public void test_create_5_modules_and_get_all() throws IOException {
        client.start();

        // Create a partner university first
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

        // Check if getting all partner universities is allowed
        if (!client.isGetAllPartnerUniversitiesAllowed()) {
            fail("Get All Partner Universities not allowed");
        }

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());

        var universities = client.partnerUniversityData();
        assertFalse(universities.isEmpty(), "No universities found");

        var universityFromServer = universities.get(0);

        for (int i = 0; i < 5; i++) {
            client.start();

            var module = new ModuleClientModel();
            module.setModuleName("Test Module " + i);
            module.setSemester(1);
            module.setCreditPoints(5);
            module.setPartnerUniversityId(universityFromServer.getId());

            client.createModule(module);
            assertEquals(201, client.getLastStatusCode());
        }

        client.start();
        assertTrue(client.isGetAllModulesAllowed());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        assertEquals(5, client.moduleData().size());
    }
}
