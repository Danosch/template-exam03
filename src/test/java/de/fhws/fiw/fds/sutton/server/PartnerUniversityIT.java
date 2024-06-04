package de.fhws.fiw.fds.sutton.server;

import com.github.javafaker.Faker;
import de.fhws.fiw.fds.suttondemo.client.models.PartnerUniversityClientModel;
import de.fhws.fiw.fds.suttondemo.client.rest.DemoRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartnerUniversityIT {
    final private Faker faker = new Faker();
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
    public void test_dispatcher_is_get_all_partnerUniversities_allowed() throws IOException {
        client.start();
        assertTrue(client.isGetAllPartnerUniversitiesAllowed());
    }



    @Test
    public void test_create_partner_university_is_allowed() throws IOException {
        client.start();
        assertTrue(client.isCreatePartnerUniversityAllowed());
    }

    @Test
    public void test_create_partner_university() throws IOException {
        client.start();

        var partnerUniversity = new PartnerUniversityClientModel();
        partnerUniversity.setName("Example University");

        partnerUniversity.setCountry("Example Country");

        client.createPartnerUniversity(partnerUniversity);
        assertEquals(201, client.getLastStatusCode());
    }

    @Test
    public void test_create_partner_university_and_get_new_partner_university() throws IOException {
        client.start();

        var partnerUniversity = new PartnerUniversityClientModel();
        partnerUniversity.setName("Example University");

        partnerUniversity.setCountry("Example Country");

        client.createPartnerUniversity(partnerUniversity);
        assertEquals(201, client.getLastStatusCode());
        assertTrue(client.isGetSinglePartnerUniversityAllowed());

        client.getSinglePartnerUniversity();
        assertEquals(200, client.getLastStatusCode());

        var partnerUniversityFromServer = client.partnerUniversityData().get(0);
        assertEquals("Example University", partnerUniversityFromServer.getName());
    }

    @Test
    public void test_create_5_partner_universities_and_get_all() throws IOException {
        for (int i = 0; i < 5; i++) {
            client.start();

            var partnerUniversity = new PartnerUniversityClientModel();
            partnerUniversity.setName(faker.university().name());
            partnerUniversity.setCountry(faker.address().country());

            client.createPartnerUniversity(partnerUniversity);
            assertEquals(201, client.getLastStatusCode());
        }

        client.start();
        assertTrue(client.isGetAllPartnerUniversitiesAllowed());

        client.getAllPartnerUniversities();
        assertEquals(200, client.getLastStatusCode());
        assertEquals(5, client.partnerUniversityData().size());

        client.setPartnerUniversityCursor(0);
        client.getSinglePartnerUniversity();
        assertEquals(200, client.getLastStatusCode());
    }
}
