package com.example.springsecurity.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Admin")

public class Admin extends User{

  /*  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   */

    public Admin(String username, String email, String phoneNumber, String password) {
        super(username, email, phoneNumber, password);
    }
}
