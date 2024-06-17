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
    }

    private void logPermission(String permission, boolean isAllowed) {
        System.out.println(permission + ": " + (isAllowed ? "allowed" : "not allowed"));
    }

    private PartnerUniversityClientModel createTestPartnerUniversity(int index) {
        var pu = new PartnerUniversityClientModel();
        pu.setUniversityName("Test University " + index);
        pu.setCountry("Test Country");
        pu.setDepartmentName("Test Department");
        pu.setWebsiteUrl("http://testuniversity" + index + ".com");
        pu.setContactPerson("John Doe " + index);
        pu.setOutgoingStudents(10);
        pu.setIncomingStudents(5);
        pu.setNextSpringSemesterStart("2024-02-01");
        pu.setNextAutumnSemesterStart("2024-09-01");
        return pu;
    }

    @Test
    public void test_dispatcher_is_available() throws IOException {
        client.start();
        System.out.println("Dispatcher Status Code: " + client.getLastStatusCode());
        assertEquals(200, client.getLastStatusCode(), "Dispatcher should be available with status code 200.");
    }

    @Test
    public void test_dispatcher_is_get_all_partner_university_allowed() throws IOException {
        client.start();
        boolean isAllowed = client.isGetAllPartnerUniversitiesAllowed();
        logPermission("isGetAllPartnerUniversitiesAllowed", isAllowed);
        assertTrue(isAllowed, "Getting all partner universities should be allowed.");
    }

    @Test
    public void test_is_create_partnerUniversity_allowed() throws IOException {
        client.start();
        boolean isAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Creating a partner university should be allowed.");
    }

    @Test
    public void test_create_partner_university() throws IOException {
        client.start();
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
        client.start();

        for (int i = 0; i < 5; i++) {
            PartnerUniversityClientModel pu = createTestPartnerUniversity(i);

            boolean isAllowed = client.isCreatePartnerUniversityAllowed();
            logPermission("isCreatePartnerUniversityAllowed", isAllowed);
            assertTrue(isAllowed, "Creating partner university should be allowed.");

            client.createPartnerUniversity(pu);
            System.out.println("Create Partner University " + i + " Status Code: " + client.getLastStatusCode());
            assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

            client.start();
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
        client.start();
        PartnerUniversityClientModel initialUniversity = createTestPartnerUniversity(0);

        boolean isCreateAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isCreateAllowed);
        assertTrue(isCreateAllowed, "Creating partner university should be allowed.");

        client.createPartnerUniversity(initialUniversity);
        System.out.println("Create Initial Partner University Status Code: " + client.getLastStatusCode());
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        client.getAllPartnerUniversities();
        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().get(0);

        PartnerUniversityClientModel updatedUniversity = new PartnerUniversityClientModel();
        updatedUniversity.setUniversityName("Updated University");
        updatedUniversity.setCountry("Updated Country");
        updatedUniversity.setDepartmentName("Updated Department");
        updatedUniversity.setWebsiteUrl("http://updateduniversity.com");
        updatedUniversity.setContactPerson("John Smith");
        updatedUniversity.setOutgoingStudents(20);
        updatedUniversity.setIncomingStudents(10);
        updatedUniversity.setNextSpringSemesterStart("2024-02-01");
        updatedUniversity.setNextAutumnSemesterStart("2024-09-01");

        boolean isUpdateAllowed = client.getAvailableLinks().contains("updatePartnerUniversity ->");
        logPermission("isUpdatePartnerUniversityAllowed", isUpdateAllowed);
        assertTrue(isUpdateAllowed, "Updating partner university should be allowed.");

        client.updatePartnerUniversity(createdUniversity.getSelfLink().getUrl(), updatedUniversity);
        System.out.println("Update Partner University Status Code: " + client.getLastStatusCode());
        assertEquals(204, client.getLastStatusCode(), "Updating partner university should return status code 204.");

        client.getSinglePartnerUniversity(createdUniversity.getSelfLink().getUrl());
        PartnerUniversityClientModel updatedUniversityFromServer = client.partnerUniversityData().get(0);

        assertEquals("Updated University", updatedUniversityFromServer.getUniversityName(), "University name should be updated.");
        assertEquals("Updated Country", updatedUniversityFromServer.getCountry(), "Country should be updated.");
        assertEquals("Updated Department", updatedUniversityFromServer.getDepartmentName(), "Department name should be updated.");
        assertEquals("http://updateduniversity.com", updatedUniversityFromServer.getWebsiteUrl(), "Website URL should be updated.");
        assertEquals("John Smith", updatedUniversityFromServer.getContactPerson(), "Contact person should be updated.");
        assertEquals(20, updatedUniversityFromServer.getOutgoingStudents(), "Outgoing students count should be updated.");
        assertEquals(10, updatedUniversityFromServer.getIncomingStudents(), "Incoming students count should be updated.");
    }

    @Test
    public void test_create_partner_university_and_module() throws IOException {
        client.start();
        PartnerUniversityClientModel partnerUniversity = createTestPartnerUniversity(0);

        boolean isCreateUniversityAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isCreateUniversityAllowed);
        assertTrue(isCreateUniversityAllowed, "Creating partner university should be allowed.");

        client.createPartnerUniversity(partnerUniversity);
        System.out.println("Create Partner University Status Code: " + client.getLastStatusCode());
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        client.getAllPartnerUniversities();
        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> "Test University 0".equals(u.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created Partner University not found"));

        assertEquals("Test University 0", createdUniversity.getUniversityName(), "Created university name should match.");

        ModuleClientModel module = new ModuleClientModel();
        module.setModuleName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setPartnerUniversityId(createdUniversity.getId());

        boolean isCreateModuleAllowed = client.isCreateModuleAllowed();
        logPermission("isCreateModuleAllowed", isCreateModuleAllowed);
        assertTrue(isCreateModuleAllowed, "Creating module should be allowed.");

        client.createModule(module, createdUniversity.getId());
        System.out.println("Create Module Status Code: " + client.getLastStatusCode());
        assertEquals(201, client.getLastStatusCode(), "Creating module should return status code 201.");

        client.getAllModules(createdUniversity.getId());
        ModuleClientModel createdModule = client.moduleData().stream()
                .filter(m -> "Test Module".equals(m.getModuleName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created Module not found"));

        assertEquals("Test Module", createdModule.getModuleName(), "Module name should match.");
        assertEquals(1, createdModule.getSemester(), "Semester should match.");
        assertEquals(5, createdModule.getCreditPoints(), "Credit points should match.");
        assertEquals(createdUniversity.getId(), createdModule.getPartnerUniversityId(), "Partner university ID should match.");
    }

    @Test
    public void test_delete_partner_university() throws IOException {
        client.start();

        // Verify initial available links
        System.out.println("Initial available links: " + client.getAvailableLinks());

        // Create a new partner university
        PartnerUniversityClientModel university = new PartnerUniversityClientModel();
        university.setUniversityName("Test University");
        university.setCountry("Test Country");
        university.setDepartmentName("Test Department");
        university.setWebsiteUrl("http://testuniversity.com");
        university.setContactPerson("John Doe");
        university.setOutgoingStudents(10);
        university.setIncomingStudents(5);
        university.setNextSpringSemesterStart("2024-02-01");
        university.setNextAutumnSemesterStart("2024-09-01");

        boolean isCreateAllowed = client.isCreatePartnerUniversityAllowed();
        logPermission("isCreatePartnerUniversityAllowed", isCreateAllowed);
        assertTrue(isCreateAllowed, "Creating partner university should be allowed.");

        client.createPartnerUniversity(university);
        System.out.println("Create Partner University Status Code: " + client.getLastStatusCode());
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        // Refresh available links
        client.start();
        System.out.println("Available links after creation: " + client.getAvailableLinks());

        client.getAllPartnerUniversities();
        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().get(0);

        // Check if delete is allowed now
        boolean isDeleteAllowed = client.isDeletePartnerUniversityAllowed();
        logPermission("isDeletePartnerUniversityAllowed", isDeleteAllowed);
        assertTrue(isDeleteAllowed, "Deleting partner university should be allowed.");

        // Perform the delete operation
        client.deletePartnerUniversity(createdUniversity.getSelfLink().getUrl());
        System.out.println("Delete Partner University Status Code: " + client.getLastStatusCode());
        assertEquals(204, client.getLastStatusCode(), "Deleting partner university should return status code 204.");

        // Verify the university has been deleted
        client.getAllPartnerUniversities();
        assertTrue(client.partnerUniversityData().isEmpty(), "Partner university list should be empty after deletion.");
    }


}
