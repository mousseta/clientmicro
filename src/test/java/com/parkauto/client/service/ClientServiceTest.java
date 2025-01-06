package com.parkauto.client.service;

import com.parkauto.client.entity.Client;
import com.parkauto.client.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void testSaveClient() {
        when(clientRepository.save(client)).thenReturn(client);

        Client savedClient = clientService.saveClient(client);

        assertNotNull(savedClient);
        assertEquals(client.getIdClient(), savedClient.getIdClient());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testGetClientById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.getClientById(1L);

        assertTrue(foundClient.isPresent());
        assertEquals(client.getIdClient(), foundClient.get().getIdClient());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = Arrays.asList(client, client);
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> allClients = clientService.getAllClients();

        assertEquals(2, allClients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testDeleteClientById() {
        doNothing().when(clientRepository).deleteById(1L);

        clientService.deleteClientById(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByEmail() {
        when(clientRepository.findByEmail(client.getEmail())).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.findByEmail(client.getEmail());

        assertTrue(foundClient.isPresent());
        assertEquals(client.getEmail(), foundClient.get().getEmail());
        verify(clientRepository, times(1)).findByEmail(client.getEmail());
    }

    @Test
    void testFindByTelephone() {
        when(clientRepository.findByTelephone(client.getTelephone())).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.findByTelephone(client.getTelephone());

        assertTrue(foundClient.isPresent());
        assertEquals(client.getTelephone(), foundClient.get().getTelephone());
        verify(clientRepository, times(1)).findByTelephone(client.getTelephone());
    }
}

