package com.example.springsecurity.repository;

import com.example.springsecurity.models.MembreEquipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembreRepository extends JpaRepository<MembreEquipe, Long> {
}
