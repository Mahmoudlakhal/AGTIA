package com.example.gestiontask.repository;

import com.example.gestiontask.models.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Commentaire, Long> {
}
