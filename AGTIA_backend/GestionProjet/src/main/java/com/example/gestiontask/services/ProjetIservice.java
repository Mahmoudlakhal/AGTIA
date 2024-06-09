package com.example.gestiontask.services;

import com.example.gestiontask.models.Projet;

import java.util.HashMap;
import java.util.List;

public interface ProjetIservice {

    Projet addProject(Projet projet , Long id);
    List<Projet> getAllProjet();
    Projet getProjetById(Long id);
    HashMap<String,String> DeleteProjet(Long id);
    Projet updateProjet(Projet projet,Long id);

    Projet addProjet(Projet projet);

    Projet affectMembre(Long projetid, List<Long> listeIdsMembres);



}
