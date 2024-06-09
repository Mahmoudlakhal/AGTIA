package com.example.springsecurity.controllers;



import com.example.springsecurity.models.UpdateUserRolesDTO;
import com.example.springsecurity.models.User;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.services.Imp.UserserviceImp;
import com.example.springsecurity.utils.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequestMapping(value = "/user")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storage;
   @Autowired
  private UserserviceImp userserviceImp;

    @GetMapping("/all")
    public List<User> getAllusers() {
        return userRepository.findAll();
    }


    @PostMapping("/save")
    public User saveuser(@RequestBody User u) {
        return userRepository.save(u);
    }

    @GetMapping ("/getone/{id}")
    public User getOneUser(@PathVariable Long id){
        return userRepository.findById(id).orElse(null);
    }


    @PutMapping("/update/{Id}")
    public User update(@RequestBody User u, @PathVariable Long Id) {
        User u1 = userRepository.findById(Id).orElse(null);
        if (u1!= null) {
            u.setId(Id);
            return userRepository.saveAndFlush(u);
        }
        else{
            throw new RuntimeException("FAIL!");
        }
    }

    @DeleteMapping("/delete/{Id}")
    public HashMap<String,String> deleteUser(@PathVariable Long Id) {
        HashMap message= new HashMap();
        try{
            userRepository.deleteById(Id);
            message.put("etat","user deleted");
            return message;
        }
        catch (Exception e) {
            message.put("etat","user not deleted");
            return message;
        }
    }


    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storage.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PutMapping("/roles/{userId}")
    public ResponseEntity<?> assignRoleChef(@PathVariable Long userId) {
        try {
            User updatedUser = userserviceImp.assignRoleChef(userId);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/roles")
    public ResponseEntity<?> updateUserRoles(@PathVariable Long userId, @RequestBody UpdateUserRolesDTO updateUserRolesDTO) {
        try {
            if (updateUserRolesDTO.getRoles() == null) {
                return ResponseEntity.badRequest().body("Roles cannot be null");
            }

            User updatedUser = userserviceImp.updateUserRoles(userId, updateUserRolesDTO.getRoles());
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
