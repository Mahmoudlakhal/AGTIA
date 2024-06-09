package com.example.gestionuser.controller;

import com.example.gestionuser.models.Admin;
import com.example.gestionuser.models.chefProjet;
import com.example.gestionuser.services.IserviceChefProjet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/chefprojet")
public class ChefProjetController {

    @Autowired
    IserviceChefProjet iserviceChefProjet;

    @PostMapping(value = "/Add_chef")
    chefProjet addChef(@RequestBody chefProjet chef){
        return iserviceChefProjet.addChefProjet(chef);
    }

    @GetMapping("/getAllchefProjet")
    public List<chefProjet> getAllchefProjets() {
        return iserviceChefProjet.getALLChefProjets();
    }

    @GetMapping("/getchefchefProjetById/{id}")
    chefProjet getAdminById(@PathVariable Long id){
        return iserviceChefProjet.getChefProjetByID(id);
    }

    @DeleteMapping("/DeleteChef/{id}")
    HashMap<String,String> DeleteChef(@PathVariable Long id){
        return iserviceChefProjet.DeleteChefProjet(id);
    }


}
