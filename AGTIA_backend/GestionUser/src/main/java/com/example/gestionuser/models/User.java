package com.example.gestionuser.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String image;
    private String email;
    private String password;
    private String userName;

    @Enumerated(EnumType.STRING)
    private functionalite functionalite;

    private String phone;
    @Enumerated(EnumType.STRING)
    private previlege prevelige;
    @Enumerated(EnumType.STRING)
    private roleInProject roleInProject;


}
