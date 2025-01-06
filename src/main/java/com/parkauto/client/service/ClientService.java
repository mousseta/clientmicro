package com.parkauto.client.service;

import com.parkauto.client.entity.Client;
import com.parkauto.client.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Créer ou mettre à jour un client
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    // Récupérer un client par son ID
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    // Récupérer tous les clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Supprimer un client par son ID
    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    // Trouver un client par email
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    // Trouver un client par téléphone
    public Optional<Client> findByTelephone(String telephone) {
        return clientRepository.findByTelephone(telephone);
    }
}
