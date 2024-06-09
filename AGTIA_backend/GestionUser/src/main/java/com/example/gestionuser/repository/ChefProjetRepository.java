package com.example.gestionuser.repository;

import com.example.gestionuser.models.chefProjet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ChefProjetRepository extends JpaRepository<chefProjet, Long> {
}
