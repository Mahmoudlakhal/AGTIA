package com.example.gestionuser.services.serviceImpl;

import com.example.gestionuser.models.chefProjet;
import com.example.gestionuser.repository.ChefProjetRepository;
import com.example.gestionuser.services.IserviceChefProjet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class serviceChefProjet implements IserviceChefProjet {

   private final ChefProjetRepository chefProjetRepository;

    public serviceChefProjet(ChefProjetRepository chefProjetRepository) {
        this.chefProjetRepository = chefProjetRepository;
    }

    @Override
    public chefProjet addChefProjet(chefProjet chefProjet) {
        chefProjetRepository.save(chefProjet);
        return chefProjet;
    }

    @Override
    public List<chefProjet> getALLChefProjets() {
        return chefProjetRepository.findAll();
    }

    @Override
    public chefProjet getChefProjetByID(Long id) {
        return chefProjetRepository.findById(id).orElseThrow(() -> new RuntimeException("Chef project Not Found"));
    }

    @Override
    public chefProjet UpdateChefProjet(chefProjet chefProjet, Long id) {
        return null;
    }

    @Override
    public HashMap<String,String> DeleteChefProjet(Long id) {
        chefProjet chefProjet = this.getChefProjetByID(id);
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
