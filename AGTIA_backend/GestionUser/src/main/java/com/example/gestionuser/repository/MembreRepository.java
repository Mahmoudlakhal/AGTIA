package com.example.gestionuser.repository;

import com.example.gestionuser.models.MembreEquipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembreRepository extends JpaRepository<MembreEquipe, Long> {
}
