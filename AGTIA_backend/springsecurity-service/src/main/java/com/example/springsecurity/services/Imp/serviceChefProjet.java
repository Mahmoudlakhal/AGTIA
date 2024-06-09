package com.example.springsecurity.services.Imp;


import com.example.springsecurity.models.ChefProjet;
import com.example.springsecurity.models.ERole;
import com.example.springsecurity.models.Role;
import com.example.springsecurity.repository.ChefProjetRepository;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.services.IserviceChefProjet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class serviceChefProjet implements IserviceChefProjet {

   private final ChefProjetRepository chefProjetRepository;

    public serviceChefProjet(ChefProjetRepository chefProjetRepository) {
        this.chefProjetRepository = chefProjetRepository;
    }
   @Autowired
    RoleRepository roleRepository;
    @Override
    public ChefProjet addChefProjet(ChefProjet chefProjet) {
        chefProjetRepository.save(chefProjet);
        return chefProjet;
    }

    @Override
    public List<ChefProjet> getALLChefProjets() {
        return chefProjetRepository.findAll();
    }

    @Override
    public ChefProjet getChefProjetByID(Long id) {
        return chefProjetRepository.findById(id).orElseThrow(() -> new RuntimeException("Chef project Not Found"));
    }

    @Override
    public ChefProjet UpdateChefProjet(ChefProjet chefProjet, Long id,  @RequestParam Set<ERole> roleNames) {
        ChefProjet chefProjet1_update = this.getChefProjetByID(id);
        if (chefProjet1_update != null) {
            // Mise à jour des champs individuels s'ils ne sont pas null
            chefProjet1_update.setUsername(chefProjet.getUsername() != null ? chefProjet.getUsername() : chefProjet1_update.getUsername());
            chefProjet1_update.setEmail(chefProjet.getEmail() != null ? chefProjet.getEmail() : chefProjet1_update.getEmail());
            chefProjet1_update.setPassword(chefProjet.getPassword() != null ? chefProjet.getPassword() : chefProjet1_update.getPassword());
            chefProjet1_update.setPhoneNumber(chefProjet.getPhoneNumber() != null ? chefProjet.getPhoneNumber() : chefProjet1_update.getPhoneNumber());

            // Mise à jour des rôles
            Set<Role> roles = new HashSet<>();
            for (ERole roleName : roleNames) {
                Role role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found"));
                roles.add(role);
            }
            chefProjet1_update.setRoles(roles);

            return chefProjetRepository.save(chefProjet1_update);
        } else {
            throw new RuntimeException("Erreur : ChefProjet non trouvé");
        }
    }


    @Override
    public HashMap<String,String> DeleteChefProjet(Long id) {
        ChefProjet chefProjet = this.getChefProjetByID(id);
        HashMap message = new HashMap<>();
        if (chefProjet != null ) {
            try {
                chefProjetRepository.deleteById(id);
                message.put("Etat","Chef Projet supprimé");
                return message;
            }
            catch (Exception e){
                message.put("Etat",""+e.getMessage());
                return message;
            }

        }
        else {
            message.put("Etat","Not Found");
            return message;
            //throw new RuntimeException(" Not Found");
        }

    }
}
