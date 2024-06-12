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
        Thread.sleep(2000); // Erhöhen der Verzögerung auf 2 Sekunden, um sicherzustellen, dass die Datenbank zurückgesetzt wird
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
    public void test_create_module_is_create_module_allowed() throws IOException {
        assertTrue(client.isCreateModuleAllowed());
    }

    @Test
    public void test_create_module() throws IOException, InterruptedException {
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

        // Fügen Sie eine Verzögerung hinzu, um sicherzustellen, dass der Server den Zustand aktualisiert hat
        Thread.sleep(2000);

        // Überprüfen Sie die Verfügbarkeit aller Links nach dem Erstellen der Partneruniversität
        client.start();
        System.out.println("Verfügbare Links nach dem Erstellen der Partneruniversität: ");
        System.out.println("GET_ALL_PARTNER_UNIVERSITIES: " + client.isGetAllPartnerUniversitiesAllowed());
        System.out.println("CREATE_PARTNER_UNIVERSITY: " + client.isCreatePartnerUniversityAllowed());
        System.out.println("GET_ALL_MODULES: " + client.isGetAllModulesAllowed());
        System.out.println("CREATE_MODULE: " + client.isCreateModuleAllowed());

        // Verify that fetching all partner universities is allowed
        assertTrue(client.isGetAllPartnerUniversitiesAllowed());

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());

        var universities = client.partnerUniversityData();
        assertFalse(universities.isEmpty(), "No universities found");

        var universityFromServer = universities.get(0);

        // Überprüfen Sie vor dem Erstellen eines Moduls, ob dies erlaubt ist
        assertTrue(client.isCreateModuleAllowed());

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
    public void test_create_5_modules_and_get_all() throws IOException, InterruptedException {
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

        // Fügen Sie eine Verzögerung hinzu, um sicherzustellen, dass der Server den Zustand aktualisiert hat
        Thread.sleep(2000);

        // Überprüfen Sie die Verfügbarkeit aller Links nach dem Erstellen der Partneruniversität
        client.start();
        System.out.println("Verfügbare Links nach dem Erstellen der Partneruniversität: ");
        System.out.println("GET_ALL_PARTNER_UNIVERSITIES: " + client.isGetAllPartnerUniversitiesAllowed());
        System.out.println("CREATE_PARTNER_UNIVERSITY: " + client.isCreatePartnerUniversityAllowed());
        System.out.println("GET_ALL_MODULES: " + client.isGetAllModulesAllowed());
        System.out.println("CREATE_MODULE: " + client.isCreateModuleAllowed());

        // Verify that fetching all partner universities is allowed
        assertTrue(client.isGetAllPartnerUniversitiesAllowed());

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());

        var universities = client.partnerUniversityData();
        assertFalse(universities.isEmpty(), "No universities found");

        var universityFromServer = universities.get(0);

        for (int i = 0; i < 5; i++) {
            // Überprüfen Sie vor dem Erstellen eines Moduls, ob dies erlaubt ist
            assertTrue(client.isCreateModuleAllowed());

            var module = new ModuleClientModel();
            module.setModuleName("Test Module " + i);
            module.setSemester(1);
            module.setCreditPoints(5);
            module.setPartnerUniversityId(universityFromServer.getId());

            client.createModule(module);
            assertEquals(201, client.getLastStatusCode());
        }

        // Fügen Sie eine Verzögerung hinzu, um sicherzustellen, dass der Server den Zustand aktualisiert hat
        Thread.sleep(2000);

        // Verify that fetching all modules is allowed
        assertTrue(client.isGetAllModulesAllowed());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        assertEquals(5, client.moduleData().size());
    }
}
