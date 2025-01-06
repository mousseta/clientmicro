package com.parkauto.client.repository;

import com.parkauto.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Trouver un client par son email
    Optional<Client> findByEmail(String email);

    // Trouver un client par son numéro de téléphone
    Optional<Client> findByTelephone(String telephone);
}
