package de.fhws.fiw.fds.sutton.server;

import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.rest.DemoRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PartnerUniversityIT {
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
    public void test_dispatcher_is_get_all_partner_universities_allowed() throws IOException {
        client.start();
        assertTrue(client.isGetAllPartnerUniversitiesAllowed());
    }

    @Test
    public void test_create_5_partner_universities_and_get_all() throws IOException {
        client.start();
        for (int i = 0; i < 5; i++) {
            assertTrue(client.isCreatePartnerUniversityAllowed(), "Create Partner University should be allowed");
            createTestUniversity("Test University " + i);
            assertEquals(201, client.getLastStatusCode(), "Expected status code 201 after creating a partner university");
        }

        client.start();
        assertTrue(client.isGetAllPartnerUniversitiesAllowed());

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());
        assertEquals(5, client.partnerUniversityData().size());
    }

    @Test
    public void test_create_partner_university_is_create_partner_university_allowed() throws IOException {
        client.start();
        assertTrue(client.isCreatePartnerUniversityAllowed());
    }

    @Test
    public void test_create_partner_university() throws IOException {
        client.start();
        assertTrue(client.isCreatePartnerUniversityAllowed(), "Create Partner University should be allowed");
        createTestUniversity("Test University");
        assertEquals(201, client.getLastStatusCode());
    }

    @Test
    public void test_update_partner_university() throws IOException {
        client.start();
        assertTrue(client.isCreatePartnerUniversityAllowed(), "Create Partner University should be allowed");
        createTestUniversity("Test University");

        client.start();
        client.getAllPartnerUniversities();
        List<PartnerUniversityClientModel> universities = client.partnerUniversityData();
        assertFalse(universities.isEmpty());

        PartnerUniversityClientModel createdUniversity = universities.get(0);
        createdUniversity.setDepartmentName("Mathematics");

        client.updatePartnerUniversity(client.getPartnerUniversityUrl(createdUniversity.getId()), createdUniversity);
        assertEquals(200, client.getLastStatusCode());

        client.start();
        client.getAllPartnerUniversities();
        universities = client.partnerUniversityData();
        PartnerUniversityClientModel updatedUniversity = universities.stream()
                .filter(u -> u.getId() == createdUniversity.getId())
                .findFirst()
                .orElse(null);
        assertNotNull(updatedUniversity);
        assertEquals("Mathematics", updatedUniversity.getDepartmentName());
    }

    @Test
    public void test_delete_partner_university() throws IOException {
        client.start();
        assertTrue(client.isCreatePartnerUniversityAllowed(), "Create Partner University should be allowed");
        createTestUniversity("Test University");

        client.start();
        client.getAllPartnerUniversities();
        List<PartnerUniversityClientModel> universities = client.partnerUniversityData();
        assertFalse(universities.isEmpty());

        PartnerUniversityClientModel createdUniversity = universities.get(0);

        client.deletePartnerUniversity(client.getPartnerUniversityUrl(createdUniversity.getId()));
        assertEquals(204, client.getLastStatusCode());

        client.start();
        assertTrue(client.isGetAllPartnerUniversitiesAllowed(), "Get All Partner Universities should be allowed after deletion");
        client.getAllPartnerUniversities();
        universities = client.partnerUniversityData();
        assertTrue(universities.isEmpty());
    }

    private void createTestUniversity(String name) throws IOException {
        var university = new PartnerUniversityClientModel();
        university.setUniversityName(name);
        university.setCountry("Germany");
        university.setDepartmentName("Computer Science");
        university.setWebsiteUrl("http://" + name.replaceAll(" ", "").toLowerCase() + ".com");
        university.setContactPerson("John Doe");
        university.setOutgoingStudents(10);
        university.setIncomingStudents(5);
        university.setNextSpringSemesterStart("2024-04-01");
        university.setNextAutumnSemesterStart("2024-10-01");

        client.createPartnerUniversity(university);
    }
}
