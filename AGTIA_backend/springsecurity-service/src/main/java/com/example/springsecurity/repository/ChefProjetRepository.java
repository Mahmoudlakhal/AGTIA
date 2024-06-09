package com.example.springsecurity.repository;

import com.example.springsecurity.models.ChefProjet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ChefProjetRepository extends JpaRepository<ChefProjet, Long> {
}
