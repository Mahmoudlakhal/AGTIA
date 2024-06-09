package com.example.springsecurity.services.Imp;


import com.example.springsecurity.models.ERole;
import com.example.springsecurity.models.Role;
import com.example.springsecurity.models.User;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.services.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserserviceImp implements Userservice {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User createUser(User User) {
        return userDao.save(User);
    }

    @Override
    public User updateUser(User User) {
        return userDao.save(User);
    }

    @Override
    public List<User> getAllUser() {
        return userDao.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
   userDao.deleteById(id);
    }


    @Transactional
    public User assignRoleChef(Long userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Role roleChef = roleRepository.findByName(ERole.ROLE_CHEF)
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_CHEF"));

        Set<Role> roles = new HashSet<>();
        roles.add(roleChef);

        user.setRoles(roles);
        return userDao.save(user);
    }

    @Transactional
    public User updateUserRoles(Long userId, Set<ERole> newRoles) {
        if (newRoles == null) {
            throw new IllegalArgumentException("Roles cannot be null");
        }

        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Set<Role> roles = new HashSet<>();
        for ( ERole role : newRoles) {
            Role roleEntity = roleRepository.findByName(role)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role));
            roles.add(roleEntity);
        }

        user.setRoles(roles);
        return userDao.save(user);
    }


}
