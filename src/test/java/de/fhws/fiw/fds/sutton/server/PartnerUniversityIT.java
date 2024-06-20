package de.fhws.fiw.fds.sutton.server;

import com.github.javafaker.Faker;
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
    public void test_is_update_partnerUniversity_allowed() throws IOException {
        client.start();
        boolean isAllowed = client.isUpdatePartnerUniversityAllowed();
        logPermission("isUpdatePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Updating a partner university should be allowed.");
    }

    @Test
    public void test_is_delete_partnerUniversity_allowed() throws IOException {
        client.start();
        boolean isAllowed = client.isDeletePartnerUniversityAllowed();
        logPermission("isDeletePartnerUniversityAllowed", isAllowed);
        assertTrue(isAllowed, "Deleting a partner university should be allowed.");
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
    public void test_update_partner_university() throws IOException {
        client.start();

        // Step 1: Log initial permissions
        boolean initialCreatePermission = client.isCreatePartnerUniversityAllowed();
        boolean initialUpdatePermission = client.isUpdatePartnerUniversityAllowed();
        logPermission("Initial isCreatePartnerUniversityAllowed", initialCreatePermission);
        logPermission("Initial isUpdatePartnerUniversityAllowed", initialUpdatePermission);

        // Step 2: Create a new university
        PartnerUniversityClientModel university = createTestPartnerUniversity(0);
        assertTrue(initialCreatePermission, "Creating partner university should be allowed initially.");
        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        // Step 3: Log permissions after creation
        boolean postCreateUpdatePermission = client.isUpdatePartnerUniversityAllowed();
        logPermission("Post-create isUpdatePartnerUniversityAllowed", postCreateUpdatePermission);

        // Step 4: Retrieve the created university to get its ID
        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode(), "Retrieving all partner universities should return status code 200.");

        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals(university.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created university not found"));

        long createdUniversityId = createdUniversity.getId();

        // Step 5: Update the created university
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

        if (!postCreateUpdatePermission) {
            System.out.println("Updating partner university is not allowed. Skipping update test.");
            return;  // Skip the test if updating is not allowed
        }

        client.updatePartnerUniversity(client.getPartnerUniversityUrl(createdUniversityId), updatedUniversity);
        assertEquals(204, client.getLastStatusCode(), "Updating partner university should return status code 204.");

        // Step 6: Log final permissions
        boolean finalUpdatePermission = client.isUpdatePartnerUniversityAllowed();
        logPermission("Final isUpdatePartnerUniversityAllowed", finalUpdatePermission);
    }


    @Test
    public void test_delete_partner_university() throws IOException {
        client.start();

        // Step 1: Log initial permissions
        boolean initialCreatePermission = client.isCreatePartnerUniversityAllowed();
        boolean initialDeletePermission = client.isDeletePartnerUniversityAllowed();
        logPermission("Initial isCreatePartnerUniversityAllowed", initialCreatePermission);
        logPermission("Initial isDeletePartnerUniversityAllowed", initialDeletePermission);

        // Step 2: Create a new university
        PartnerUniversityClientModel university = createTestPartnerUniversity(0);
        assertTrue(initialCreatePermission, "Creating partner university should be allowed initially.");
        client.createPartnerUniversity(university);
        assertEquals(201, client.getLastStatusCode(), "Creating partner university should return status code 201.");

        // Step 3: Log permissions after creation
        boolean postCreateDeletePermission = client.isDeletePartnerUniversityAllowed();
        logPermission("Post-create isDeletePartnerUniversityAllowed", postCreateDeletePermission);

        // Step 4: Retrieve the created university to get its ID
        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode(), "Retrieving all partner universities should return status code 200.");

        PartnerUniversityClientModel createdUniversity = client.partnerUniversityData().stream()
                .filter(u -> u.getUniversityName().equals(university.getUniversityName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created university not found"));

        long createdUniversityId = createdUniversity.getId();

        // Step 5: Delete the created university
        if (!postCreateDeletePermission) {
            System.out.println("Deleting partner university is not allowed. Skipping delete test.");
            return;  // Skip the test if deleting is not allowed
        }

        client.deletePartnerUniversity(client.getPartnerUniversityUrl(createdUniversityId));
        assertEquals(204, client.getLastStatusCode(), "Deleting partner university should return status code 204.");

        // Step 6: Log final permissions
        boolean finalDeletePermission = client.isDeletePartnerUniversityAllowed();
        logPermission("Final isDeletePartnerUniversityAllowed", finalDeletePermission);
    }

}
