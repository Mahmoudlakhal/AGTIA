package com.example.gestiontask.services.ImpServies;

import com.example.gestiontask.client_config.UserProjet_RestClient;
import com.example.gestiontask.models.User;
import com.example.gestiontask.models.Projet;
import com.example.gestiontask.repository.ProjetRepository;
import com.example.gestiontask.services.ProjetIservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class serviceProjetImpl implements ProjetIservice {

    @Autowired
    ProjetRepository projetRepository;



    @Autowired
  UserProjet_RestClient userProjet_restClient;


    @Override
    public Projet addProject(Projet projet, Long id) {
        User user=userProjet_restClient.findUserById(id);
        if(user !=null){
            projet.setId_user(user.getId());
            projet.setUser(user);
            userProjet_restClient.updateRole(user.getId());
            return projetRepository.save(projet);
        }
        else{
            throw new RuntimeException("ERREUR");
        }
    }




    @Override
    public Projet addProjet(Projet projet) {
            return projetRepository.save(projet);
    }

    @Override
    public List<Projet> getAllProjet() {
        return projetRepository.findAll();
    }

    @Override
    public Projet getProjetById(Long id) {
        return projetRepository.findById(id).orElseThrow(() -> new RuntimeException("Project Not Found"));
    }

    @Override
    public HashMap<String, String> DeleteProjet(Long id) {
        Projet projet = this.getProjetById(id);
        HashMap message = new HashMap<>();
        if(projet != null){

            try {
                projetRepository.deleteById(id);
                message.put("Etat" ,"Projet Supprimé");
                return message;

            }catch (Exception e)

            {
                message.put("Etat" ," "+ e.getMessage());
                return message;

            }
        }
        else {
            message.put("Etat","Not Found");
            return message;
            //throw new RuntimeException(" Not Found");
        }
    }
    @Override
    public Projet updateProjet(Projet projet, Long id) {
        Projet projet_update = this.getProjetById(id);
        if (projet_update != null) {
            projet.setId(id);
            projet.setTitle(projet.getTitle() != null ? projet.getTitle() : projet_update.getTitle());
            projet.setDescription(projet.getDescription() != null ? projet.getDescription() : projet_update.getDescription());
            projet.setTypeProjet(projet.getTypeProjet() != null ? projet.getTypeProjet() : projet_update.getTypeProjet());
            projet.setDate_creation(projet.getDate_creation() != null ? projet.getDate_creation() : projet_update.getDate_creation());
            projet.setIcon(projet.getIcon() != null ? projet.getIcon() : projet_update.getIcon());

            // Mise à jour de la liste des membres seulement si elle est modifiée
            List<Long> nouveauxMembresIds = new ArrayList<>();
            List<Long> membresIds = projet.getMembresIds();
            if (membresIds != null && !membresIds.equals(projet_update.getMembresIds())) {
                for (Long membreId : membresIds) {
                    User user = userProjet_restClient.findUserById(membreId);
                    if (user != null) {
                        if (!nouveauxMembresIds.contains(membreId)) {
                            nouveauxMembresIds.add(membreId);
                        }
                        projet_update.setMembresIds(nouveauxMembresIds);
                    } else {
                        throw new RuntimeException("Membre avec l'identifiant " + membreId + " introuvable");
                    }
                }

                // Enregistrer les modifications dans le projet
                return projetRepository.save(projet_update);
            } else {
                // Aucune modification de la liste des membres, retourner le projet sans enregistrer
                return projet_update;
            }
        } else {
            throw new RuntimeException("Projet avec l'identifiant " + id + " introuvable");
        }
    }
    @Override
    public Projet affectMembre(Long projetId, List<Long> listeIdsMembres) {
        Projet projet = this.getProjetById(projetId);

        if (projet != null) {
            for (Long membreId : listeIdsMembres) {
                if (!projet.getMembresIds().contains(membreId)) {
                    User user = userProjet_restClient.findUserById(membreId);
                    if (user != null) {
                        projet.getMembresIds().add(membreId);
                    }
                    else {
                        throw new RuntimeException("mombre not found");
                    }
                }
            }
            // Enregistrer les modifications dans le projet
            return projetRepository.save(projet);
        } else {
            throw new RuntimeException("mombre not found");
        }
    }

}
