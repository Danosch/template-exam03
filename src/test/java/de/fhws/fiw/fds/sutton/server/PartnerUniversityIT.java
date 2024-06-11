package de.fhws.fiw.fds.sutton.server;

import com.github.javafaker.Faker;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.rest.DemoRestClient;
import org.junit.jupiter.api.BeforeAll;
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
    public void test_create_partner_university_is_create_partner_university_allowed() throws IOException {
        client.start();
        assertTrue(client.isCreatePartnerUniversityAllowed());
    }

    @Test
    public void test_create_partner_university() throws IOException {
        client.start();

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
    }

    @Test
    public void test_create_partner_university_and_get_new_partner_university() throws IOException {
        client.start();

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
        assertTrue(client.isGetAllPartnerUniversitiesAllowed());

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());

        var universityFromServer = client.partnerUniversityData().get(0);
        assertEquals("Test University", universityFromServer.getUniversityName());
    }

    @Test
    public void test_create_5_partner_universities_and_get_all() throws IOException {
        for (int i = 0; i < 5; i++) {
            client.start();

            var university = new PartnerUniversityClientModel();
            university.setUniversityName("Test University " + i);
            university.setCountry("Germany");
            university.setDepartmentName("Computer Science");
            university.setWebsiteUrl("http://testuniversity" + i + ".com");
            university.setContactPerson("John Doe");
            university.setOutgoingStudents(10);
            university.setIncomingStudents(5);
            university.setNextSpringSemesterStart("2024-04-01");
            university.setNextAutumnSemesterStart("2024-10-01");

            client.createPartnerUniversity(university);
            assertEquals(201, client.getLastStatusCode());
        }

        client.start();
        assertTrue(client.isGetAllPartnerUniversitiesAllowed());

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());
        assertEquals(5, client.partnerUniversityData().size());
    }
}