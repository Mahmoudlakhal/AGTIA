package com.example.gestiontask.controller;

import com.example.gestiontask.client_config.UserProjet_RestClient;
import com.example.gestiontask.models.Commentaire;
import com.example.gestiontask.models.Task;
import com.example.gestiontask.models.User;
import com.example.gestiontask.repository.CommentRepository;
import com.example.gestiontask.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Commentaire")
@CrossOrigin(origins ="http://localhost:4200")
public class CommentaireController {


    @Autowired
    private CommentRepository commentaireRepository;

    @Autowired
    private UserProjet_RestClient userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/{userId}/{taskId}")
    public ResponseEntity<?> addCommentaireParUser(@PathVariable Long userId,
                                                   @PathVariable Long taskId,
                                                   @RequestBody Commentaire commentaire) {
        try {
            // Log avant de récupérer l'utilisateur
            System.out.println("Récupération de l'utilisateur avec ID: " + userId);
            User user = userRepository.findUserById(userId);
            if (user == null || user.getId() == null) {
                // Utilisateur non trouvé ou ID non valide
                System.out.println("Utilisateur non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }

            // Log avant de récupérer la tâche
            System.out.println("Récupération de la tâche avec ID: " + taskId);
            Optional<Task> optionalTask = taskRepository.findById(taskId);
            if (!optionalTask.isPresent()) {
                // Tâche non trouvée
                System.out.println("Tâche non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tâche non trouvée");
            }

            Task task = optionalTask.get();
            commentaire.setId_user(user.getId());
            commentaire.setUser(user);
            commentaire.setTask(task);
            System.out.println("Enregistrement du commentaire : " + commentaire);
            commentaireRepository.save(commentaire);
            return ResponseEntity.ok("Commentaire ajouté avec succès");
        } catch (Exception e) {
            // Log l'erreur pour le debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur");
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentaire(@PathVariable Long id) {
        Optional<Commentaire> optionalCommentaire = commentaireRepository.findById(id);
        if (optionalCommentaire.isPresent()) {
            commentaireRepository.delete(optionalCommentaire.get());
            return ResponseEntity.ok("Commentaire supprimé avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommentaire(@PathVariable Long id, @RequestBody Commentaire updatedCommentaire) {
        Optional<Commentaire> optionalCommentaire = commentaireRepository.findById(id);
        if (optionalCommentaire.isPresent()) {
            Commentaire commentaire = optionalCommentaire.get();
            commentaire.setDescription(updatedCommentaire.getDescription());
            commentaireRepository.save(commentaire);
            return ResponseEntity.ok("Commentaire mis à jour avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
