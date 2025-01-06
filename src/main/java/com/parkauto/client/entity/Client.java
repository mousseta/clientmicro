package com.parkauto.client.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CLIENT")
@Data // Génère automatiquement les getters, setters, equals, hashCode et toString
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Builder // Permet de construire des objets en utilisant un pattern Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDCLIENT")
    private Long idClient;

    @Column(name = "NOM", nullable = false)
    private String nom;

    @Column(name = "PRENOM", nullable = false)
    private String prenom;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "TELEPHONE", nullable = false)
    private String telephone;

    @Column(name = "ADRESSE", nullable = false)
    private String adresse;
}
