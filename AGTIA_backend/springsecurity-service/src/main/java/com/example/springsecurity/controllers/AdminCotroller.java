package com.example.springsecurity.controllers;


import com.example.springsecurity.models.Admin;
import com.example.springsecurity.models.ERole;
import com.example.springsecurity.models.Role;
import com.example.springsecurity.models.User;
import com.example.springsecurity.payload.request.SignupRequest;
import com.example.springsecurity.payload.response.MessageResponse;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.repository.UserRepository;

import com.example.springsecurity.services.IserviceAdmin;
import com.example.springsecurity.services.Userservice;
import com.example.springsecurity.utils.EmailService;
import com.example.springsecurity.utils.ImageUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/Admin")
public class AdminCotroller {

    @Autowired
    IserviceAdmin iserviceAdmin;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Userservice userservice;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
   private EmailService emailService;
  @Autowired
  private JavaMailSender javaMailSender;




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



    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, HttpServletRequest request) throws MessagingException, IOException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        Admin admin = new Admin(signUpRequest.getUsername(), signUpRequest.getEmail(),signUpRequest.getPhoneNumber(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();


                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

        admin.setRoles(roles);
        userRepository.save(admin);

        // Convertir l'image en base64
        String imagePath = "D:/AGTIA_backend/springsecurity-service/upload-dir/agtialogo.png";
        String base64Image = ImageUtil.encodeImageToBase64(imagePath);
        //mail confirmation
        String from ="admin@gmail.com" ;
        String to = signUpRequest.getEmail();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Complete Registration!");
        helper.setFrom(from);
        helper.setTo(to);

        String htmlContent = "<html><body style='font-family: Arial, sans-serif; line-height: 1.6; background-color: #f6f6f6; padding: 20px;'>"
                + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border-radius: 10px; background-color: #ffffff; box-shadow: 0 0 10px rgba(0,0,0,0.1);'>"
                + "<h2 style='text-align: center; color: #4A90E2;'>Welcome to Agtia!</h2>"  // Couleur bleue
                + "<p style='text-align: center;'><img src='data:image/png;base64," + base64Image + "' alt='Agtia Logo' style='max-width: 150px; height: auto;'/></p>"
                + "<p>Dear " + signUpRequest.getUsername() + ",</p>"
                + "<p>Thank you for registering with Agtia. Please confirm your email address by clicking the button below:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href=\"http://localhost:8050/auth/confirme?email=" + signUpRequest.getEmail()
                + "\" style='background-color: #4A90E2; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>VERIFY</a>"  // Couleur bleue
                + "</div>"
                + "<p>If you did not create an account, please ignore this email.</p>"
                + "<p>Best regards,<br>The Agtia Team</p>"
                + "<hr style='border: none; border-top: 1px solid #f0f0f0; margin: 20px 0;'/>"
                + "<p style='text-align: center; font-size: 0.9em; color: #777;'>"
                + "If you have any questions, feel free to <a href='mailto:support@agtia.com' style='color: #4A90E2; text-decoration: none;'>contact us</a>."  // Couleur bleue
                + "</p>"
                + "</div>"
                + "</body></html>";

        helper.setText(htmlContent, true);
        javaMailSender.send(message);


        return ResponseEntity.ok(new MessageResponse("Admin registered successfully! check your email for confirmation"));
    }



}
