package com.example.gestionuser.controller;

import com.example.gestionuser.models.Admin;
import com.example.gestionuser.services.IserviceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/Admin")
public class AdminCotroller {

    @Autowired
    IserviceAdmin iserviceAdmin;

    @PostMapping(value = "/Add_Admin")
    Admin addAdmin(@RequestBody Admin admin){
        return iserviceAdmin.addAdmin(admin);
    }

    @GetMapping("/getAllAdmin")
    public List<Admin> getAllAdmins() {
        return iserviceAdmin.getALLAdmins();
    }

    @GetMapping("/getAdminById/{id}")
    Admin getAdminById(@PathVariable Long id){
        return iserviceAdmin.getAdminByID(id);
    }

    @DeleteMapping("/DeleteAdmin/{id}")
    HashMap<String,String> DeleteAdmin(@PathVariable Long id){
        return iserviceAdmin.DeleteAdmin(id);
    }


}
