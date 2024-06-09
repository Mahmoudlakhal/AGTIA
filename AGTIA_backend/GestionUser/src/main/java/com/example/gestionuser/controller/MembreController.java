package com.example.gestionuser.controller;

import com.example.gestionuser.models.MembreEquipe;
import com.example.gestionuser.services.IserviceMembre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/membreEquipe")
public class MembreController {

    @Autowired
    IserviceMembre iserviceMembre;

    @PostMapping(value = "/addMembre")
    MembreEquipe addMembre(@RequestBody MembreEquipe membre){
        return iserviceMembre.addMembreEquipe(membre);
    }

    @GetMapping("/getAllMembreEquipe")
    public List<MembreEquipe> getAllMembreEquipe() {
        return iserviceMembre.getALLMembreEquipes();
    }

    @GetMapping("/getchefchefProjetById/{id}")
    MembreEquipe getMembreEquipeById(@PathVariable Long id){
        return iserviceMembre.getMembreEquipeByID(id);
    }

    @DeleteMapping("/DeleteMembre/{id}")
    HashMap<String,String> DeleteMembre(@PathVariable Long id){
        return iserviceMembre.DeleteMembreEquipe(id);
    }
}
