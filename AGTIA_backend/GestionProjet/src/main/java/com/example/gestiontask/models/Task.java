package com.example.gestiontask.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Size(min = 3, max = 30)
    private String title;
    @Size(min = 3, max = 120)
    private String decription;
    private String title2;
    private String title3;

    private String piecejoint;
    @Enumerated(EnumType.STRING)
    private etat etat;
    @Enumerated(EnumType.STRING)
    private priority priority;
    @Enumerated(EnumType.STRING)
    private taskType taskType;
    @Enumerated(EnumType.STRING)
    private complexity complexity;
    private LocalDate TimeTask;
    private String messionStrat;
    private String messionEnd;
    private String Timepoinage;

   // @JsonIgnore
    @ManyToOne
    @JsonIgnoreProperties("tasks")
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @Transient
    private User user;

    @Column(name = "user_id")
    private Long id_user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("task")
    private List<Commentaire> comments = new ArrayList<>();
}
