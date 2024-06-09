package com.example.gestionuser.services;

import com.example.gestionuser.models.MembreEquipe;

import java.util.HashMap;
import java.util.List;

public interface IserviceMembre {

    MembreEquipe addMembreEquipe(MembreEquipe membreEquipe);
    List<MembreEquipe> getALLMembreEquipes();
    MembreEquipe getMembreEquipeByID(Long id);
    MembreEquipe UpdateMembreEquipe(MembreEquipe membreEquipe,Long id);
    HashMap<String,String> DeleteMembreEquipe(Long id);
}
