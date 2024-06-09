package com.example.gestionuser.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Admin")
@DiscriminatorValue("Admin")

public class Admin extends User{

  /*  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   */
}
