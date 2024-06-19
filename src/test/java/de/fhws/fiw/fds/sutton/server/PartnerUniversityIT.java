package de.fhws.fiw.fds.sutton.server;

import com.github.javafaker.Faker;
import de.fhws.fiw.fds.suttondemo.client.models.ModuleClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.rest.DemoRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PartnerUniversityIT {
    private final Faker faker = new Faker();
    private DemoRestClient client;

    @BeforeEach
    public void setUp() throws IOException {
        this.client = new DemoRestClient();
        this.client.resetDatabase();
        this.client.start();
    }

    private void logPermission(String permission, boolean isAllowed) {
        System.out.println(permission + ": " + (isAllowed ? "allowed" : "not allowed"));
    }

    private PartnerUniversityClientModel createTestPartnerUniversity(int index) {
        PartnerUniversityClientModel pu = new PartnerUniversityClientModel();
        pu.setUniversityName("Test University " + index);
        pu.setCountry("Test Country");
        pu.setDepartmentName("Test Department");
        pu.setWebsiteUrl("http://testuniversity" + index + ".com");
        pu.setContactPerson("John Doe");
        pu.setOutgoingStudents(10);
        pu.setIncomingStudents(5);
        pu.setNextSpringSemesterStart("2024-02-01");
        pu.setNextAutumnSemesterStart("2024-09-01");
        return pu;
    }

    @Test
    public void testDispatcherLinks() throws IOException {
        client.start();  // Dies sollte die Links initialisieren und den Dispatcher starten

        // Überprüfen Sie, ob die Links korrekt gesetzt wurden
        assertTrue(client.isCreatePartnerUniversityAllowed());
        assertTrue(client.isGetAllPartnerUniversitiesAllowed());
        assertTrue(client.isUpdatePartnerUniversityAllowed());
        assertTrue(client.isDeletePartnerUniversityAllowed());
        assertTrue(client.isGetAllModulesAllowed());
        assertTrue(client.isCreateModuleAllowed());
        assertTrue(client.isGetSingleModuleAllowed());
        assertTrue(client.isUpdateModuleAllowed());
        assertTrue(client.isDeleteModuleAllowed());

        // Zusätzliche Assertions für Pagination und Sorting Links
        String availableLinks = client.getAvailableLinks();
        assertTrue(availableLinks.contains("self_partner_universities ->"));
        assertTrue(availableLinks.contains("next_partner_universities ->"));
        assertTrue(availableLinks.contains("prev_partner_universities ->"));
        assertTrue(availableLinks.contains("sort_asc_partner_universities ->"));
        assertTrue(availableLinks.contains("sort_desc_partner_universities ->"));
        assertTrue(availableLinks.contains("self_modules_for_university ->"));
        assertTrue(availableLinks.contains("next_modules_for_university ->"));
        assertTrue(availableLinks.contains("prev_modules_for_university ->"));
        assertTrue(availableLinks.contains("sort_asc_modules_for_university ->"));
        assertTrue(availableLinks.contains("sort_desc_modules_for_university ->"));
    }

    @Test
    public void test_dispatcher_is_available() throws IOException {
        System.out.println("Dispatcher Status Code: " + client.getLastStatusCode());
        assertEquals(200, client.getLastStatusCode(), "Dispatcher should be available with status code 200.");
    }

    @Test
    public void test_dispatcher_is_get_all_partner_university_allowed() throws IOException {
        boolean isAllowed = client.isGetAllPartnerUniversitiesAllowed();
        logPermission("isGetAllPartnerUniversitiesAllowed", isAllowed);
        assertTrue(isAllowed, "Getting all partner universities should be allowed.");
    }

    @Test
    public void test_is_create_partnerUniversity_allowed() throws IOException {
        boolean isAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a partner university should be allowed.");
    }

    @Test
    public void test_is_update_partnerUniversity_allowed() throws IOException {
        boolean isAllowed = client.isUpdatePartnerUniversityAllowed();
        logPermission("isUpdatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Updating a partner university should be allowed.");
    }

    @Test
    public void test_is_delete_partnerUniversity_allowed() throws IOException {
        boolean isAllowed = client.isDeletePartnerUniversityAllowed();
        logPermission("isDeletePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Deleting a partner university should be allowed.");
    }

    @Test
    public void test_create_partner_university() throws IOException {
        PartnerUniversityClientModel pu = createTestPartnerUniversity(0);

        boolean isAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a partner university should be allowed.");

        client.createPartnerUniversity(pu);
        System.out.println("Create Partner University Status Code: " + client.getLastStatusCode());
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");
    }

    @Test
    public void test_create_5_partner_universities_and_get_all() throws IOException {
        for (int i = 0; i < 5; i++) {
            PartnerUniversityClientModel pu = createTestPartnerUniversity(i);

            boolean isAllowed = client.isCreatePartnerUniversityAllowed();
            logPermission("isCreatePartnerUniversityAllowed before creating university " + i, isAllowed);
            assertTrue(isAllowed, "Creating partner university should be allowed before creating university " + i);

            client.createPartnerUniversity(pu);
            System.out.println("Create Partner University " + i + " Status Code: " + client.getLastStatusCode());
            assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201 after creating university " + i);

            client.start(); // Neustarten des Clients nach jeder Erstellung

            isAllowed = client.isCreatePartnerUniversityAllowed();
            logPermission("isCreatePartnerUniversityAllowed after creating university " + i, isAllowed);
            assertTrue(isAllowed, "Creating partner university should be allowed after creating university " + i);
        }

        boolean isAllowed = client.isGetAllPartnerUniversitiesAllowed();
        logPermission("isGetAllPartnerUniversitiesAllowed", isAllowed);
        assertTrue(isAllowed, "Getting all partner universities should be allowed.");

        client.getAllPartnerUniversities();
        System.out.println("Get All Partner Universities Status Code: " + client.getLastStatusCode());
        assertEquals(200, client.getLastStatusCode(), "Getting all partner universities should return status code 200.");
        assertEquals(5, client.partnerUniversityData().size(), "There should be 5 partner universities.");

        client.setPartnerUniversityCursor(0);
        PartnerUniversityClientModel firstUniversity = client.partnerUniversityData().get(0);
        client.getSinglePartnerUniversity(firstUniversity.getSelfLink().getUrl());
        System.out.println("Get Single Partner University Status Code: " + client.getLastStatusCode());
        assertEquals(200, client.getLastStatusCode(), "Getting single partner university should return status code 200.");
    }

    @Test
    public void test_update_partner_university() throws IOException {
        // Initial permission checks
        boolean initialCreatePermission = client.isCreatePartnerUniversityAllowed();
        boolean initialUpdatePermission = client.isUpdatePartnerUniversityAllowed();
        logPermission("Initial isCreatePartnerUniversityAllowed", initialCreatePermission);
        logPermission("Initial isUpdatePartnerUniversityAllowed", initialUpdatePermission);

        // Ensure that creation is allowed before proceeding
        assertTrue(initialCreatePermission, "Creating partner university should be allowed initially.");

        // Create a new university
        PartnerUniversityClientModel university = createTestPartnerUniversity(0);
        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        // Ensure that the dispatcher and available links are refreshed
        client.start();

        // Retrieve all partner universities to get the created university's ID
        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode(), "Retrieving all partner universities should return status code 200.");

        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals(university.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created university not found"));

        long createdUniversityId = createdUniversity.getId();

        // Prepare updated university data
        PartnerUniversityClientModel updatedUniversity = new PartnerUniversityClientModel();
        updatedUniversity.setUniversityName("UPDATED NAME");
        updatedUniversity.setCountry(createdUniversity.getCountry());
        updatedUniversity.setDepartmentName(createdUniversity.getDepartmentName());
        updatedUniversity.setWebsiteUrl(createdUniversity.getWebsiteUrl());
        updatedUniversity.setContactPerson(createdUniversity.getContactPerson());
        updatedUniversity.setOutgoingStudents(createdUniversity.getOutgoingStudents());
        updatedUniversity.setIncomingStudents(createdUniversity.getIncomingStudents());
        updatedUniversity.setNextSpringSemesterStart(createdUniversity.getNextSpringSemesterStart());
        updatedUniversity.setNextAutumnSemesterStart(createdUniversity.getNextAutumnSemesterStart());

        // Check if updating is allowed after creation
        boolean postCreateUpdatePermission = client.isUpdatePartnerUniversityAllowed();
        logPermission("Post-create isUpdatePartnerUniversityAllowed", postCreateUpdatePermission);

        // If updating is not allowed, log the available links for debugging
        if (!postCreateUpdatePermission) {
            System.out.println("Available links: " + client.getAvailableLinks());
        }

        // Ensure that updating is allowed
        assertTrue(postCreateUpdatePermission, "Updating partner university should be allowed after creating it.");

        // Perform the update operation
        client.updatePartnerUniversity(client.getPartnerUniversityUrl(createdUniversityId), updatedUniversity);
        assertEquals(204, client.getLastStatusCode(), "Updating partner university should return status code 204.");

        // Verify the final update permission
        boolean finalUpdatePermission = client.isUpdatePartnerUniversityAllowed();
        logPermission("Final isUpdatePartnerUniversityAllowed", finalUpdatePermission);
    }

    @Test
    public void test_delete_partner_university() throws IOException {
        client.start();

        PartnerUniversityClientModel university = createTestPartnerUniversity(0);

        assertTrue(client.isCreatePartnerUniversityAllowed(), "Creating partner university should be allowed.");
        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode(), "Retrieving all partner universities should return status code 200.");

        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals(university.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created university not found"));

        long createdUniversityId = createdUniversity.getId();

        client.deletePartnerUniversity(client.getPartnerUniversityUrl(createdUniversityId));
        assertEquals(204, client.getLastStatusCode(), "Deleting partner university should return status code 204.");

        // Check if the partner university no longer exists
        try {
            client.getSinglePartnerUniversity(createdUniversityId);
            fail("Partner university should no longer exist.");
        } catch (IOException e) {
            // Expected exception, as the university should be deleted
            assertTrue(e.getMessage().contains("404"), "Expected a 404 status code.");
        }
    }

    @Test
    public void test_delete_partner_university_and_modules() throws IOException {
        PartnerUniversityClientModel university = createTestPartnerUniversity(0);
        boolean isAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a partner university should be allowed.");
        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        client.start();

        client.getAllPartnerUniversities();
        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals(university.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created university not found"));

        long universityId = createdUniversity.getId();

        isAllowed = client.isCreateModuleAllowed();
        logPermission("isCreateModuleAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a module should be allowed.");

        for (int i = 0; i < 3; i++) {
            ModuleClientModel module = new ModuleClientModel();
            module.setModuleName("Test Module " + i);
            module.setPartnerUniversityId(universityId);
            assertNotNull(module, "Module should be created.");
            client.createModule(module, universityId);
            assertEquals(201, client.getLastStatusCode(), "Creating module should return status code 201.");
        }

        client.getAllModules(universityId);
        assertEquals(200, client.getLastStatusCode(), "Getting all modules should return status code 200.");
        assertEquals(3, client.moduleData().size(), "There should be 3 modules.");

        client.deletePartnerUniversity(client.getPartnerUniversityUrl(universityId));
        assertEquals(204, client.getLastStatusCode(), "Deleting partner university should return status code 204.");

        client.getAllPartnerUniversities();
        boolean universityExists = client.partnerUniversityData().stream()
                .anyMatch(u -> u.getId() == universityId);
        assertFalse(universityExists, "Deleted partner university should not exist in the university list.");

        client.getAllModules(universityId);
        assertEquals(404, client.getLastStatusCode(), "Getting all modules should return status code 404, as the university is deleted.");
        assertTrue(client.moduleData().isEmpty(), "The module list should be empty since the university is deleted.");
    }
}
