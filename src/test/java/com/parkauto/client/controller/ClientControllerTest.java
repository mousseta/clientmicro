package com.parkauto.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkauto.client.entity.Client;
import com.parkauto.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Client client;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .idClient(1L)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .telephone("0123456789")
                .adresse("123 Rue Principale")
                .build();
    }

    @Test
    void testSaveClient() throws Exception {
        when(clientService.saveClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/clients/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient", is(1)))
                .andExpect(jsonPath("$.nom", is("Dupont")))
                .andExpect(jsonPath("$.email", is("jean.dupont@example.com")));

        verify(clientService, times(1)).saveClient(any(Client.class));
    }

    @Test
    void testGetClientById() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient", is(1)))
                .andExpect(jsonPath("$.nom", is("Dupont")))
                .andExpect(jsonPath("$.email", is("jean.dupont@example.com")));

        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void testGetAllClients() throws Exception {
        when(clientService.getAllClients()).thenReturn(Collections.singletonList(client));

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // Vérifie qu'il y a 1 élément dans la liste
                .andExpect(jsonPath("$[0].idClient", is(1))) // Vérifie les détails du premier client
                .andExpect(jsonPath("$[0].nom", is("Dupont")));

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void testDeleteClientById() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(Optional.of(client));
        doNothing().when(clientService).deleteClientById(1L);

        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClientById(1L);
    }

    @Test
    void testFindByEmail() throws Exception {
        when(clientService.findByEmail("jean.dupont@example.com")).thenReturn(Optional.of(client));

        mockMvc.perform(get("/clients/email/jean.dupont@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient", is(1)))
                .andExpect(jsonPath("$.email", is("jean.dupont@example.com")));

        verify(clientService, times(1)).findByEmail("jean.dupont@example.com");
    }

    @Test
    void testFindByTelephone() throws Exception {
        when(clientService.findByTelephone("0123456789")).thenReturn(Optional.of(client));

        mockMvc.perform(get("/clients/telephone/0123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idClient", is(1)))
                .andExpect(jsonPath("$.telephone", is("0123456789")));

        verify(clientService, times(1)).findByTelephone("0123456789");
    }
}
