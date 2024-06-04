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
    public void test_dispatcher_is_get_all_modules_allowed() throws IOException {
        client.start();
        assertTrue(client.isGetAllModulesAllowed());
    }

    @Test
    public void test_create_module_is_allowed() throws IOException {
        client.start();
        assertTrue(client.isCreateModuleAllowed());
    }

    @Test
    public void test_create_module() throws IOException {
        client.start();

        var partnerUniversity = new PartnerUniversityClientModel();
        partnerUniversity.setName("Example University");
        partnerUniversity.setCountry("Example Country");

        client.createPartnerUniversity(partnerUniversity);
        assertEquals(201, client.getLastStatusCode());

        var module = new ModuleClientModel();
        module.setModuleName("Example Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setUniversityId(partnerUniversity.getId());

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());
    }

    @Test
    public void test_create_module_and_get_new_module() throws IOException {
        client.start();

        var partnerUniversity = new PartnerUniversityClientModel();
        partnerUniversity.setName("Example University");
        partnerUniversity.setCountry("Example Country");

        client.createPartnerUniversity(partnerUniversity);
        assertEquals(201, client.getLastStatusCode());

        var module = new ModuleClientModel();
        module.setModuleName("Example Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setUniversityId(partnerUniversity.getId());

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());
        assertTrue(client.isGetSingleModuleAllowed());

        client.getSingleModule();
        assertEquals(200, client.getLastStatusCode());

        var moduleFromServer = client.moduleData().get(0);
        assertEquals("Example Module", moduleFromServer.getModuleName());
    }

    @Test
    public void test_create_5_modules_and_get_all() throws IOException {
        var partnerUniversity = new PartnerUniversityClientModel();
        partnerUniversity.setName("Example University");
        partnerUniversity.setCountry("Example Country");

        client.createPartnerUniversity(partnerUniversity);
        assertEquals(201, client.getLastStatusCode());

        for (int i = 0; i < 5; i++) {
            client.start();

            var module = new ModuleClientModel();
            module.setModuleName(faker.educator().course());
            module.setSemester(faker.number().numberBetween(1, 8));
            module.setCreditPoints(faker.number().numberBetween(1, 10));
            module.setUniversityId(partnerUniversity.getId());

            client.createModule(module);
            assertEquals(201, client.getLastStatusCode());
        }

        client.start();
        assertTrue(client.isGetAllModulesAllowed());

        client.getAllModules();
        assertEquals(200, client.getLastStatusCode());
        assertEquals(5, client.moduleData().size());

        client.setModuleCursor(0);
        client.getSingleModule();
        assertEquals(200, client.getLastStatusCode());
    }

    @Test
    public void test_update_module() throws IOException {
        client.start();

        var partnerUniversity = new PartnerUniversityClientModel();
        partnerUniversity.setName("Example University");
        partnerUniversity.setCountry("Example Country");

        client.createPartnerUniversity(partnerUniversity);
        assertEquals(201, client.getLastStatusCode());

        var module = new ModuleClientModel();
        module.setModuleName("Example Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setUniversityId(partnerUniversity.getId());

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());
        assertTrue(client.isGetSingleModuleAllowed());

        client.getSingleModule();
        assertEquals(200, client.getLastStatusCode());

        var moduleFromServer = client.moduleData().get(0);
        moduleFromServer.setModuleName("Updated Module");

        client.updateModule(moduleFromServer.getSelfLink().getUrl(), moduleFromServer);
        assertEquals(200, client.getLastStatusCode());

        client.getSingleModule();
        assertEquals(200, client.getLastStatusCode());

        var updatedModuleFromServer = client.moduleData().get(0);
        assertEquals("Updated Module", updatedModuleFromServer.getModuleName());
    }

    @Test
    public void test_delete_module() throws IOException {
        client.start();

        var partnerUniversity = new PartnerUniversityClientModel();
        partnerUniversity.setName("Example University");
        partnerUniversity.setCountry("Example Country");

        client.createPartnerUniversity(partnerUniversity);
        assertEquals(201, client.getLastStatusCode());

        var module = new ModuleClientModel();
        module.setModuleName("Example Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setUniversityId(partnerUniversity.getId());

        client.createModule(module);
        assertEquals(201, client.getLastStatusCode());
        assertTrue(client.isGetSingleModuleAllowed());

        client.getSingleModule();
        assertEquals(200, client.getLastStatusCode());

        var moduleFromServer = client.moduleData().get(0);

        client.deleteModule(moduleFromServer.getSelfLink().getUrl());
        assertEquals(204, client.getLastStatusCode());
    }
}
