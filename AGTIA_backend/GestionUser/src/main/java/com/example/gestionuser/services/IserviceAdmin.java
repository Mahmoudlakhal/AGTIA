package com.example.gestionuser.services;

import com.example.gestionuser.models.Admin;

import java.util.HashMap;
import java.util.List;

public interface IserviceAdmin {

    Admin addAdmin(Admin admin);
    List<Admin> getALLAdmins();
    Admin getAdminByID(Long id);
    Admin UpdateAdmin(Admin admin,Long id);
    public HashMap<String,String> DeleteAdmin(Long id);
}
