package de.fhws.fiw.fds.sutton.server;

import de.fhws.fiw.fds.suttondemo.client.models.ModuleClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.rest.DemoRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ModulesIT {
    private DemoRestClient client;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        this.client = new DemoRestClient();
        this.client.resetDatabase();
        waitForDatabaseReset();
        client.start();
    }

    @Test
    public void test_dispatcher_is_available() throws IOException {
        assertEquals(200, client.getLastStatusCode());
    }

    @Test
    public void test_dispatcher_is_get_all_modules_allowed() throws IOException {
        assertTrue(client.isGetAllModulesAllowed());
    }

    @Test
    public void test_create_and_read_module() throws IOException, InterruptedException {
        var university = createAndVerifyPartnerUniversity();

        var module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setPartnerUniversityId(university.getId());

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());

        var modules = client.moduleData();
        assertFalse(modules.isEmpty(), "No modules found");
        var moduleFromServer = modules.get(0);
        assertEquals(module.getModuleName(), moduleFromServer.getModuleName());
    }

    @Test
    public void test_update_module() throws IOException, InterruptedException {
        var university = createAndVerifyPartnerUniversity();

        var module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setPartnerUniversityId(university.getId());

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());

        var modules = client.moduleData();
        var moduleToUpdate = modules.get(0);
        moduleToUpdate.setModuleName("Updated Module Name");

        client.updateModule(client.getModuleUrl(moduleToUpdate.getId()), moduleToUpdate);
        assertEquals(204, client.getLastStatusCode());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        modules = client.moduleData();
        var updatedModule = modules.get(0);
        assertEquals("Updated Module Name", updatedModule.getModuleName());
    }

    @Test
    public void test_delete_module() throws IOException, InterruptedException {
        var university = createAndVerifyPartnerUniversity();

        var module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setPartnerUniversityId(university.getId());

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());

        var modules = client.moduleData();
        var moduleToDelete = modules.get(0);

        client.deleteModule(client.getModuleUrl(moduleToDelete.getId()));
        assertEquals(204, client.getLastStatusCode());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        modules = client.moduleData();
        assertTrue(modules.isEmpty(), "Module not deleted");
    }

    @Test
    public void test_delete_partner_university_and_associated_modules() throws IOException, InterruptedException {
        var university = createAndVerifyPartnerUniversity();

        var module1 = new ModuleClientModel();
        module1.setModuleName("Test Module 1");
        module1.setSemester(1);
        module1.setCreditPoints(5);
        module1.setPartnerUniversityId(university.getId());

        client.createModule(module1);
        assertEquals(201, client.getLastStatusCode());

        var module2 = new ModuleClientModel();
        module2.setModuleName("Test Module 2");
        module2.setSemester(2);
        module2.setCreditPoints(10);
        module2.setPartnerUniversityId(university.getId());

        client.createModule(module2);
        assertEquals(201, client.getLastStatusCode());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        var modules = client.moduleData();
        assertEquals(2, modules.size());

        client.deletePartnerUniversity(client.getPartnerUniversityUrl(university.getId()));
        assertEquals(204, client.getLastStatusCode());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        modules = client.moduleData();
        assertTrue(modules.isEmpty(), "Modules not deleted with partner university");
    }

    private PartnerUniversityClientModel createAndVerifyPartnerUniversity() throws IOException, InterruptedException {
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

        waitForPartnerUniversityCreation();
        return university;
    }

    private void waitForDatabaseReset() throws InterruptedException, IOException {
        int retries = 20;
        while (retries-- > 0) {
            try {
                client.start();
                if (client.isGetAllPartnerUniversitiesAllowed()) {
                    return;
                }
            } catch (Exception e) {
                // Ignore exceptions and retry
            }
            Thread.sleep(1000);
        }
        fail("Database reset did not complete in time");
    }

    private void waitForPartnerUniversityCreation() throws InterruptedException, IOException {
        int retries = 20;
        while (retries-- > 0) {
            try {
                client.getAllPartnerUniversities();
                if (client.getLastStatusCode() == 200 && !client.partnerUniversityData().isEmpty()) {
                    return;
                }
            } catch (Exception e) {
                // Ignore exceptions and retry
            }
            Thread.sleep(1000);
        }
        fail("Partner university creation did not complete in time");
    }
}
