package com.example.gestionuser.services;


import com.example.gestionuser.models.chefProjet;

import java.util.HashMap;
import java.util.List;

public interface IserviceChefProjet {

    chefProjet addChefProjet(chefProjet chefProjet);
    List<chefProjet> getALLChefProjets();
    chefProjet getChefProjetByID(Long id);
    chefProjet UpdateChefProjet(chefProjet chefProjet,Long id);
    public HashMap<String,String> DeleteChefProjet(Long id);
}
