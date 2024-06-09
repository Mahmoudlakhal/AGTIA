package com.example.gestionuser.controller;

import com.example.gestionuser.models.MembreEquipe;
import com.example.gestionuser.models.User;
import com.example.gestionuser.services.UserIservice;
import com.example.gestionuser.utils.StorgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserIservice iservice;
    @Autowired
    private StorgeService storgeService;


    @PostMapping(value = "/addUtilisateur")
    User addUser(User user, @RequestParam("file") MultipartFile file){
        String original=storgeService.storee(file,30,30);
        user.setImage(original);
        return iservice.addUser(user);
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUser() {
        return iservice.getALLUsers();
    }

    @GetMapping("/getUserById/{id}")
    User getUserById(@PathVariable Long id){
        return iservice.getUserByID(id);
    }

    @PutMapping("/UpdateUser/{id}")
    User updateUser(@RequestBody User user,@PathVariable Long id){
        return iservice.UpdateUser(user,id);
    }

    @DeleteMapping("/DeleteUser/{id}")
    HashMap<String,String> DeleteUser(@PathVariable Long id){
        return iservice.DeleteUser(id);
    }
}
