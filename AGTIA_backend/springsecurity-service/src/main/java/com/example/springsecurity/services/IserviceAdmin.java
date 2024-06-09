package com.example.springsecurity.services;


import com.example.springsecurity.models.Admin;
import com.example.springsecurity.models.ERole;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface IserviceAdmin {

    Admin addAdmin(Admin admin);
    List<Admin> getALLAdmins();
    Admin getAdminByID(Long id);
    Admin UpdateAdmin(Admin admin,Long id);
    public HashMap<String,String> DeleteAdmin(Long id);
}
