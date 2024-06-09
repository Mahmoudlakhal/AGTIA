package com.example.gestiontask.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projets")

public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cle_projet = generateVerificationCode();

    private String title;
    private String description;
    private String icon = null;

    @Enumerated(EnumType.STRING)
    private typeProjet typeProjet;

    private LocalDate date_creation;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("projet")
    @JsonIncludeProperties("title")
    private List<Task> tasks;



    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    //@Transient parce que UserProjet n'est pas etre instance dans une autre classe donc n'eté pas criée dans la DB
    @Transient
    private User user;

    @Column(name = "user_id")
    private Long id_user;

    @ElementCollection
    private List<Long> membresIds;

}
