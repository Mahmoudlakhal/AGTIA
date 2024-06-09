package com.example.springsecurity.services;



import com.example.springsecurity.models.ChefProjet;
import com.example.springsecurity.models.ERole;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface IserviceChefProjet {

    ChefProjet addChefProjet(ChefProjet chefProjet);
    List<ChefProjet> getALLChefProjets();
    ChefProjet getChefProjetByID(Long id);
    ChefProjet UpdateChefProjet(ChefProjet chefProjet,Long id,  Set<ERole> roleNames);
    public HashMap<String,String> DeleteChefProjet(Long id);
}
