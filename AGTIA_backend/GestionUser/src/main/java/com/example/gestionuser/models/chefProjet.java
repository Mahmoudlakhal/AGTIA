package com.example.gestionuser.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chefProjet")
@DiscriminatorValue("Chef")

public class chefProjet extends User{
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  */


}
