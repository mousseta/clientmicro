package com.parkauto.client.repository;

import com.parkauto.client.entity.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .telephone("0123456789")
                .adresse("123 Rue Principale")
                .build();
    }

    @Test
    void testCreateClient() {
        Client savedClient = clientRepository.save(client);
        assertNotNull(savedClient.getIdClient());
        assertEquals(client.getEmail(), savedClient.getEmail());
    }

    @Test
    void testFindClientByEmail() {
        clientRepository.save(client);
        Optional<Client> foundClient = clientRepository.findByEmail(client.getEmail());
        assertTrue(foundClient.isPresent());
        assertEquals(client.getEmail(), foundClient.get().getEmail());
    }

    @Test
    void testFindClientByTelephone() {
        clientRepository.save(client);
        Optional<Client> foundClient = clientRepository.findByTelephone(client.getTelephone());
        assertTrue(foundClient.isPresent());
        assertEquals(client.getTelephone(), foundClient.get().getTelephone());
    }

    @Test
    void testUpdateClient() {
        Client savedClient = clientRepository.save(client);
        savedClient.setAdresse("456 Avenue Modifiée");
        Client updatedClient = clientRepository.save(savedClient);

        assertEquals("456 Avenue Modifiée", updatedClient.getAdresse());
    }

    @Test
    void testDeleteClient() {
        Client savedClient = clientRepository.save(client);
        Long id = savedClient.getIdClient();

        clientRepository.deleteById(id);

        Optional<Client> deletedClient = clientRepository.findById(id);
        assertFalse(deletedClient.isPresent());
    }

    @Test
    void testFindById() {
        Client savedClient = clientRepository.save(client);
        Optional<Client> foundClient = clientRepository.findById(savedClient.getIdClient());

        assertTrue(foundClient.isPresent());
        assertEquals(savedClient.getNom(), foundClient.get().getNom());
    }
}
