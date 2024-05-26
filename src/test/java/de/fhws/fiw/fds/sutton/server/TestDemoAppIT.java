package de.fhws.fiw.fds.sutton.server;


import com.github.javafaker.Faker;
import de.fhws.fiw.fds.suttondemo.client.models.PersonClientModel;
import de.fhws.fiw.fds.suttondemo.client.rest.DemoRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDemoAppIT {
    final private Faker faker = new Faker();
    private DemoRestClient client;

    @BeforeEach
    public void setUp() throws IOException{
       this.client = new DemoRestClient();
       this.client.resetDatabase();
    }

    @Test
    public void test_dispatcher_is_available() throws IOException {
        client.start();
        assertEquals(200, client.getLastStatusCode());
    }

    @Test
    public void test_dispatcher_is_get_all_persons_allowed() throws IOException {
        client.start();
        assertTrue(client.isGetAllPersonsAllowed());
    }

    @Test
    public void test_create_person_is_create_person_allowed() throws IOException {
        client.start();
        assertTrue(client.isCreatePersonAllowed());
    }

    @Test void test_create_person() throws IOException
    {
        client.start();

        var person = new PersonClientModel();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthDate(LocalDate.of( 1990, 1, 1));
        person.setEmailAddress("max.mustermann@thws.de");

        client.createPerson(person);
        assertEquals(201, client.getLastStatusCode());
    }

    @Test void test_create_person_and_get_new_person() throws IOException
    {
        client.start();

        var person = new PersonClientModel();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthDate(LocalDate.of( 1990, 1, 1));
        person.setEmailAddress("max.mustermann@thws.de");

        client.createPerson(person);
        assertEquals(201, client.getLastStatusCode());
        assertTrue( client.isGetSinglePersonAllowed() );

        client.getSinglePerson();
        assertEquals(200, client.getLastStatusCode());

        var personFromServer = client.personData().getFirst();
        assertEquals( "Mustermann", personFromServer.getLastName() );
    }

    @Test void test_create_5_person_and_get_all() throws IOException
    {
        /*
         * The next statements look strange, because we call the dispatcher in all
         * iterations. But this is how the client works. The dispatcher is the entry point
         * and we need to call it in order to get the URL to create a new person.
         */
        for( int i=0; i<5; i++ ) {
            client.start();

            var person = new PersonClientModel();
            person.setFirstName(faker.name().firstName());
            person.setLastName(faker.name().lastName());
            person.setBirthDate(LocalDate.of(1990, 1, 1));
            person.setEmailAddress(faker.internet().emailAddress());

            client.createPerson(person);
            assertEquals(201, client.getLastStatusCode());
        }

        /* Now we call the dispatcher to get the URL to get all persons */
        client.start();
        assertTrue( client.isGetAllPersonsAllowed() );

        client.getAllPersons();
        assertEquals(200, client.getLastStatusCode());
        assertEquals(5, client.personData().size());

        /* Set the cursor to the first person, not really necessary, but to make it clear here */
        client.setPersonCursor(0);
        client.getSinglePerson();
        assertEquals(200, client.getLastStatusCode());
    }
    @Test void test_create_many_persons_and_filter_by_name() throws IOException
    {
        /* Create 100 random people */
        client.fillDatabase();

        /* Create one person with a known name */
        var person = new PersonClientModel();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthDate(LocalDate.of( 1990, 1, 1));
        person.setEmailAddress("max.mustermann@thws.de");

        client.start();
        client.createPerson(person);
        assertEquals(201, client.getLastStatusCode());

        /* We need to start over because at the moment,only the dispatcher returns the filter query template */
        client.start();

        assertTrue( client.isGetByFirstNameAndLastNameAllowed() );
        client.getByFirstNameAndLastName( "Max", "Muster" );
        assertEquals( 1, client.personData().size() );
    }

    @Test
    public void test_next_page() throws IOException
    {
        this.client.fillDatabase();
        this.client.start();
        this.client.getAllPersons();
        assertTrue( this.client.isNextPageAvailable());
    }
}
