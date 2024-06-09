package com.example.gestionuser.services.serviceImpl;

import com.example.gestionuser.models.MembreEquipe;
import com.example.gestionuser.models.User;
import com.example.gestionuser.repository.AdminRepository;
import com.example.gestionuser.repository.MembreRepository;
import com.example.gestionuser.services.IserviceMembre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class serviceMembre implements IserviceMembre {

    private final MembreRepository membreRepository;

    public serviceMembre(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    @Override
    public MembreEquipe addMembreEquipe(MembreEquipe membreEquipe) {
        membreRepository.save(membreEquipe);
        return membreEquipe;
    }

    @Override
    public List<MembreEquipe> getALLMembreEquipes() {
        return membreRepository.findAll();
    }

    @Override
    public MembreEquipe getMembreEquipeByID(Long id) {
        return membreRepository.findById(id).orElseThrow(() -> new RuntimeException("Membre Not Found"));    }

    @Override
    public MembreEquipe UpdateMembreEquipe(MembreEquipe membreEquipe, Long id) {
        return null;
    }

    @Override
    public HashMap<String,String> DeleteMembreEquipe(Long id) {
        MembreEquipe membreEquipe = this.getMembreEquipeByID(id);
        HashMap message = new HashMap<>();
        if (membreEquipe != null ) {
            try {
                membreRepository.deleteById(id);
                message.put("Etat","Membre supprimé");
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
