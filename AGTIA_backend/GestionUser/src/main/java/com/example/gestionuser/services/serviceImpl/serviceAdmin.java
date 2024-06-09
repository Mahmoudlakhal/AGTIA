package com.example.gestionuser.services.serviceImpl;

import com.example.gestionuser.models.Admin;
import com.example.gestionuser.repository.AdminRepository;
import com.example.gestionuser.services.IserviceAdmin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class serviceAdmin implements IserviceAdmin {
    private final AdminRepository adminRepository;

    public serviceAdmin(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin addAdmin(Admin admin) {
        adminRepository.save(admin);
        return admin;
    }

    @Override
    public List<Admin> getALLAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin getAdminByID(Long id) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        return optionalAdmin.orElse(null);
    }

    @Override
    public Admin UpdateAdmin(Admin admin, Long id) {
        return null;
    }

    @Override
    public HashMap<String,String> DeleteAdmin(Long id) {
        Admin admin = this.getAdminByID(id);
        HashMap message = new HashMap<>();
        if (admin != null ) {
            try {
                adminRepository.deleteById(id);
                message.put("Etat","Admin supprimé");
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
