package com.parkauto.client.integration;

import com.parkauto.client.entity.Client;
import com.parkauto.client.repository.ClientRepository;
import com.parkauto.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        // Reset database
        clientRepository.deleteAll();

        // Initialize a client with unique email and telephone
        client = Client.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont+" + UUID.randomUUID() + "@example.com") // Unique email
                .telephone("0123456789" + UUID.randomUUID().toString().substring(0, 4)) // Unique telephone
                .adresse("123 Rue Principale")
                .build();
    }

    @Test
    void testRepositorySaveAndFind() {
        // Save client using repository
        Client savedClient = clientRepository.save(client);

        // Verify client is saved
        Optional<Client> foundClient = clientRepository.findById(savedClient.getIdClient());
        assertTrue(foundClient.isPresent());
        assertEquals(savedClient.getEmail(), foundClient.get().getEmail());

        // Verify client is retrievable by email
        Optional<Client> clientByEmail = clientRepository.findByEmail(client.getEmail());
        assertTrue(clientByEmail.isPresent());
        assertEquals(client.getTelephone(), clientByEmail.get().getTelephone());
    }

    @Test
    void testServiceSaveAndRetrieve() {
        // Save client using service
        Client savedClient = clientService.saveClient(client);

        // Verify client is saved and retrievable by ID
        Optional<Client> foundClient = clientService.getClientById(savedClient.getIdClient());
        assertTrue(foundClient.isPresent());
        assertEquals(client.getEmail(), foundClient.get().getEmail());

        // Verify all clients retrieval
        List<Client> allClients = clientService.getAllClients();
        assertEquals(1, allClients.size());
        assertEquals(client.getEmail(), allClients.get(0).getEmail());
    }

    @Test
    void testServiceDeleteClient() {
        // Save client using service
        Client savedClient = clientService.saveClient(client);

        // Delete client by ID
        clientService.deleteClientById(savedClient.getIdClient());

        // Verify client is deleted
        Optional<Client> deletedClient = clientService.getClientById(savedClient.getIdClient());
        assertFalse(deletedClient.isPresent());
    }

    @Test
    void testServiceFindByEmailAndTelephone() {
        // Save client using service
        clientService.saveClient(client);

        // Find client by email
        Optional<Client> foundByEmail = clientService.findByEmail(client.getEmail());
        assertTrue(foundByEmail.isPresent());
        assertEquals(client.getNom(), foundByEmail.get().getNom());

        // Find client by telephone
        Optional<Client> foundByTelephone = clientService.findByTelephone(client.getTelephone());
        assertTrue(foundByTelephone.isPresent());
        assertEquals(client.getAdresse(), foundByTelephone.get().getAdresse());
    }
}
