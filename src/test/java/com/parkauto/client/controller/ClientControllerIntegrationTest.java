package com.parkauto.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkauto.client.entity.Client;
import com.parkauto.client.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ClientControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Client client;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Nettoyage et configuration initiale de la base de données
        clientRepository.deleteAll();

        client = Client.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .telephone("0123456789")
                .adresse("123 Rue Principale")
                .build();

        clientRepository.save(client);
    }

    @Test
    void testSaveClient() throws Exception {
        Client newClient = Client.builder()
                .nom("Martin")
                .prenom("Paul")
                .email("paul.martin@example.com")
                .telephone("0987654321")
                .adresse("456 Rue Secondaire")
                .build();

        ResultActions response = mockMvc.perform(post("/clients/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newClient)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient").exists())
                .andExpect(jsonPath("$.nom", is("Martin")))
                .andExpect(jsonPath("$.email", is("paul.martin@example.com")));
    }

    @Test
    void testGetClientById() throws Exception {
        mockMvc.perform(get("/clients/" + client.getIdClient()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient", is(client.getIdClient().intValue())))
                .andExpect(jsonPath("$.nom", is(client.getNom())))
                .andExpect(jsonPath("$.email", is(client.getEmail())));
    }

    @Test
    void testGetAllClients() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom", is(client.getNom())));
    }

    @Test
    void testDeleteClientById() throws Exception {
        mockMvc.perform(delete("/clients/" + client.getIdClient()))
                .andExpect(status().isNoContent());

        Optional<Client> deletedClient = clientRepository.findById(client.getIdClient());
        assert (deletedClient.isEmpty());
    }

    @Test
    void testFindByEmail() throws Exception {
        mockMvc.perform(get("/clients/email/" + client.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient", is(client.getIdClient().intValue())))
                .andExpect(jsonPath("$.email", is(client.getEmail())));
    }

    @Test
    void testFindByTelephone() throws Exception {
        mockMvc.perform(get("/clients/telephone/" + client.getTelephone()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient", is(client.getIdClient().intValue())))
                .andExpect(jsonPath("$.telephone", is(client.getTelephone())));
    }
}
