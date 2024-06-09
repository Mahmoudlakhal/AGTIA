package com.example.gestionuser.services.serviceImpl;


import com.example.gestionuser.models.User;
import com.example.gestionuser.repository.UserRepository;
import com.example.gestionuser.services.UserIservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class serviceUserImpl implements UserIservice {
    @Autowired
    public UserRepository userRepository;

    @Override
    public User addUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> getALLUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByID(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    @Override
    public User UpdateUser(User user, Long id) {
        User user_update = this.getUserByID(id);
        if (user_update != null) {

            user.setId(id);
            user.setFirstName(user.getFirstName() == null ? user_update.getFirstName() : user.getFirstName());
            user.setLastName(user.getLastName() == null ? user_update.getLastName() : user.getLastName());
            user.setEmail(user.getEmail() == null ? user_update.getEmail() : user.getEmail());
            user.setPassword(user.getPassword() == null ? user_update.getPassword() : user.getPassword());
            user.setImage(user.getImage() == null ? user_update.getImage() : user.getImage());
            user.setFunctionalite(user.getFunctionalite() == null ? user_update.getFunctionalite() : user.getFunctionalite());
            user.setPhone(user.getPhone() == null ? user_update.getPhone() : user.getPhone());
            user.setPhone(user.getPhone() == null ? user_update.getPhone() : user.getPhone());
            user.setRoleInProject(user.getRoleInProject() == null ? user_update.getRoleInProject() : user.getRoleInProject());

            return userRepository.save(user);
        }
        else{
            throw new RuntimeException("error ");
        }    }

    @Override
    public HashMap<String, String> DeleteUser(Long id) {
        User user = this.getUserByID(id);
        HashMap message = new HashMap<>();
        if (user != null ) {
            try {
                userRepository.deleteById(id);
                message.put("Etat","User supprimé");
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
