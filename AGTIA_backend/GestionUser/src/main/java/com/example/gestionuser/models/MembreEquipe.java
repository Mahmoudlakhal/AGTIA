package com.example.gestionuser.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Table(name = "membreEquipe")
@DiscriminatorValue("Membre")

public class MembreEquipe extends User{
   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;*/

}
