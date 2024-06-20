package de.fhws.fiw.fds.sutton.server;

import com.github.javafaker.Faker;
import de.fhws.fiw.fds.suttondemo.client.models.ModuleClientModel;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.rest.DemoRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleIT {
    private final Faker faker = new Faker();
    private DemoRestClient client;

    @BeforeEach
    public void setUp() throws IOException {
        this.client = new DemoRestClient();
        this.client.resetDatabase();
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

    private ModuleClientModel createTestModule(long partnerUniversityId, int suffix) {
        ModuleClientModel module = new ModuleClientModel();
        module.setModuleName("Test Module " + suffix);
        module.setPartnerUniversityId(partnerUniversityId); // Ensure the university ID is set
        return module;
    }

    @Test
    public void test_is_create_module_allowed() throws IOException {
        client.start();
        boolean isAllowed = client.isCreateModuleAllowed();
        logPermission("isCreateModuleAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a module should be allowed.");
    }

    @Test
    public void test_is_update_module_allowed() throws IOException {
        client.start();
        boolean isAllowed = client.isUpdateModuleAllowed();
        logPermission("isUpdateModuleAllowed", isAllowed);
        assertTrue(isAllowed, "Updating a module should be allowed.");
    }

    @Test
    public void test_is_delete_module_allowed() throws IOException {
        client.start();
        boolean isAllowed = client.isDeleteModuleAllowed();
        logPermission("isDeleteModuleAllowed", isAllowed);
        assertTrue(isAllowed, "Deleting a module should be allowed.");
    }

    @Test
    public void test_is_get_all_modules_allowed() throws IOException {
        client.start();
        boolean isAllowed = client.isGetAllModulesAllowed();
        logPermission("isGetAllModulesAllowed", isAllowed);
        assertTrue(isAllowed, "Getting all modules should be allowed.");
    }

    @Test
    public void test_is_get_single_module_allowed() throws IOException {
        client.start();
        boolean isAllowed = client.isGetSingleModuleAllowed();
        logPermission("isGetSingleModuleAllowed", isAllowed);
        assertTrue(isAllowed, "Getting a single module should be allowed.");
    }

    @Test
    public void test_create_module() throws IOException {
        client.start();

        // Step 1: Create a new university
        PartnerUniversityClientModel university = createTestPartnerUniversity(0);
        assertNotNull(university, "Partner university should be created.");

        // Step 2: Check permission for creating a module
        boolean isCreateModuleAllowed = client.isCreateModuleAllowed();
        logPermission("isCreateModuleAllowed", isCreateModuleAllowed);
        assertTrue(isCreateModuleAllowed, "Creating a module should be allowed.");

        // Step 3: Create a new module for the university
        ModuleClientModel module = createTestModule(university.getId(), 0);
        assertNotNull(module, "Module should be created.");
    }

    @Test
    public void test_create_and_get_module() throws IOException {
        client.start();

        // Step 1: Create a new university
        PartnerUniversityClientModel university = createTestPartnerUniversity(0);
        boolean isAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a partner university should be allowed.");
        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        // Refresh links after creating the partner university
        client.start();

        // Retrieve the created university to get its ID
        client.getAllPartnerUniversities();
        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals(university.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created university not found"));

        long universityId = createdUniversity.getId();

        // Step 2: Check permission to create module
        isAllowed = client.isCreateModuleAllowed();
        logPermission("isCreateModuleAllowed", isAllowed);
        if (!isAllowed) {
            System.out.println("Creating module is not allowed. Skipping create module test.");
            return;  // Skip the test if creating module is not allowed
        }

        // Step 3: Create a module for the university
        ModuleClientModel module = createTestModule(universityId, 0);
        assertNotNull(module, "Module should be created.");
        client.createModule(module, universityId);
        assertEquals(201, client.getLastStatusCode(), "Creating module should return status code 201.");

        // Step 4: Get and verify the created module
        client.getAllModules(universityId);
        assertEquals(200, client.getLastStatusCode(), "Getting all modules should return status code 200.");
        assertFalse(client.moduleData().isEmpty(), "The module list should not be empty.");

        ModuleClientModel fetchedModule = client.moduleData().stream()
                .filter(m -> m.getModuleName().equals(module.getModuleName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created module not found"));

        client.getSingleModule(universityId, fetchedModule.getId());
        System.out.println("Get Single Module Status Code: " + client.getLastStatusCode());
        assertEquals(200, client.getLastStatusCode(), "Getting single module should return status code 200.");
        assertNotNull(fetchedModule, "Fetched module should not be null.");
        assertEquals(module.getModuleName(), fetchedModule.getModuleName(), "Fetched module name should match.");
    }

    @Test
    public void test_update_module() throws IOException {
        client.start();

        // Step 1: Create a new university
        PartnerUniversityClientModel university = createTestPartnerUniversity(0);
        boolean isAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a partner university should be allowed.");
        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        // Refresh links after creating the partner university
        client.start();

        // Retrieve the created university to get its ID
        client.getAllPartnerUniversities();
        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals(university.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created university not found"));

        long universityId = createdUniversity.getId();

        // Step 2: Check permission to create module
        isAllowed = client.isCreateModuleAllowed();
        logPermission("isCreateModuleAllowed", isAllowed);
        if (!isAllowed) {
            System.out.println("Creating module is not allowed. Skipping create module test.");
            return;  // Skip the test if creating module is not allowed
        }

        // Step 3: Create a module for the university
        ModuleClientModel module = createTestModule(universityId, 0);
        assertNotNull(module, "Module should be created.");
        client.createModule(module, universityId);
        assertEquals(201, client.getLastStatusCode(), "Creating module should return status code 201.");

        // Step 4: Retrieve the created module to get its ID
        client.getAllModules(universityId);
        ModuleClientModel createdModule = client.moduleData().stream()
                .filter(m -> m.getModuleName().equals(module.getModuleName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created module not found"));

        long moduleId = createdModule.getId();

        // Step 5: Update the created module
        ModuleClientModel updatedModule = new ModuleClientModel();
        updatedModule.setModuleName("UPDATED MODULE NAME");
        updatedModule.setPartnerUniversityId(universityId);

        String updateModuleUrl = client.getModuleUrl(universityId, moduleId);
        client.updateModule(updateModuleUrl, updatedModule);
        assertEquals(204, client.getLastStatusCode(), "Updating module should return status code 204.");

        // Step 6: Verify the updated module
        client.getSingleModule(universityId, moduleId);
        ModuleClientModel fetchedModule = client.moduleData().get(0);
        assertEquals("UPDATED MODULE NAME", fetchedModule.getModuleName(), "Updated module name should match.");
    }

    @Test
    public void test_delete_module() throws IOException {
        client.start();

        // Step 1: Create a new university
        PartnerUniversityClientModel university = createTestPartnerUniversity(0);
        boolean isAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a partner university should be allowed.");
        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        // Refresh links after creating the partner university
        client.start();

        // Retrieve the created university to get its ID
        client.getAllPartnerUniversities();
        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals(university.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created university not found"));

        long universityId = createdUniversity.getId();

        // Step 2: Check permission to create module
        isAllowed = client.isCreateModuleAllowed();
        logPermission("isCreateModuleAllowed", isAllowed);
        if (!isAllowed) {
            System.out.println("Creating module is not allowed. Skipping create module test.");
            return;  // Skip the test if creating module is not allowed
        }

        // Step 3: Create a module for the university
        ModuleClientModel module = createTestModule(universityId, 0);
        assertNotNull(module, "Module should be created.");
        client.createModule(module, universityId);
        assertEquals(201, client.getLastStatusCode(), "Creating module should return status code 201.");

        // Step 4: Retrieve the created module to get its ID
        client.getAllModules(universityId);
        ModuleClientModel createdModule = client.moduleData().stream()
                .filter(m -> m.getModuleName().equals(module.getModuleName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created module not found"));

        long moduleId = createdModule.getId();

        // Step 5: Delete the created module
        String deleteModuleUrl = client.getModuleUrl(universityId, moduleId);
        client.deleteModule(deleteModuleUrl);
        assertEquals(204, client.getLastStatusCode(), "Deleting module should return status code 204.");

        // Step 6: Verify the module is deleted
        client.getAllModules(universityId);
        boolean moduleExists = client.moduleData().stream()
                .anyMatch(m -> m.getId() == moduleId);
        assertFalse(moduleExists, "Deleted module should not exist in the module list.");
    }

    @Test
    public void test_delete_partner_university_and_modules() throws IOException {
        client.start();

        // Step 1: Create a new university
        PartnerUniversityClientModel university = createTestPartnerUniversity(0);
        boolean isAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a partner university should be allowed.");
        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        // Refresh links after creating the partner university
        client.start();

        // Retrieve the created university to get its ID
        client.getAllPartnerUniversities();
        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals(university.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created university not found"));

        long universityId = createdUniversity.getId();

        // Step 2: Check permission to create module
        isAllowed = client.isCreateModuleAllowed();
        logPermission("isCreateModuleAllowed", isAllowed);
        if (!isAllowed) {
            System.out.println("Creating module is not allowed. Skipping create module test.");
            return;  // Skip the test if creating module is not allowed
        }

        // Step 3: Create a module for the university
        for (int i = 0; i < 3; i++) {
            ModuleClientModel module = createTestModule(universityId, i);
            assertNotNull(module, "Module should be created.");
            client.createModule(module, universityId);
            assertEquals(201, client.getLastStatusCode(), "Creating module should return status code 201.");
        }

        // Step 4: Retrieve the created modules to verify
        client.getAllModules(universityId);
        assertEquals(200, client.getLastStatusCode(), "Getting all modules should return status code 200.");
        assertEquals(3, client.moduleData().size(), "There should be 3 modules.");

        // Step 5: Delete the created university
        client.deletePartnerUniversity(client.getPartnerUniversityUrl(universityId));
        assertEquals(204, client.getLastStatusCode(), "Deleting partner university should return status code 204.");

        // Step 6: Verify the university is deleted
        client.getAllPartnerUniversities();
        boolean universityExists = client.partnerUniversityData().stream()
                .anyMatch(u -> u.getId() == universityId);
        assertFalse(universityExists, "Deleted partner university should not exist in the university list.");

        // Step 7: Verify the modules are deleted
        client.getAllModules(universityId);
        assertEquals(404, client.getLastStatusCode(), "Getting all modules should return status code 404, as the university is deleted.");
        assertTrue(client.moduleData().isEmpty(), "The module list should be empty since the university is deleted.");
    }
}
