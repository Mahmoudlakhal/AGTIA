package com.example.gestionuser.services;

import com.example.gestionuser.models.User;

import java.util.HashMap;
import java.util.List;

public interface UserIservice {

    User addUser(User user);
    List<User> getALLUsers();
    User getUserByID(Long id);
    User UpdateUser(User user,Long id);
    HashMap<String,String> DeleteUser(Long id);
}
