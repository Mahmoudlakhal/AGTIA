package com.example.springsecurity.controllers;

import com.example.springsecurity.models.Admin;
import com.example.springsecurity.models.ERole;
import com.example.springsecurity.models.MembreEquipe;
import com.example.springsecurity.models.Role;
import com.example.springsecurity.payload.request.SignupRequest;
import com.example.springsecurity.payload.response.MessageResponse;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.services.IserviceMembre;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/membreEquipe")
public class MembreController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    Userservice userservice;
    @Autowired
    IserviceMembre iserviceMembre;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping(value = "/addMembre")
    MembreEquipe addMembre(@RequestBody MembreEquipe membre){
        return iserviceMembre.addMembreEquipe(membre);
    }

    @GetMapping("/getAllMembreEquipe")
    public List<MembreEquipe> getAllMembreEquipe() {
        return iserviceMembre.getALLMembreEquipes();
    }

    @GetMapping("/getchefchefProjetById/{id}")
    MembreEquipe getMembreEquipeById(@PathVariable Long id){
        return iserviceMembre.getMembreEquipeByID(id);
    }

    @DeleteMapping("/DeleteMembre/{id}")
    HashMap<String,String> DeleteMembre(@PathVariable Long id){
        return iserviceMembre.DeleteMembreEquipe(id);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, HttpServletRequest request) throws MessagingException, IOException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        MembreEquipe membreEquipe = new MembreEquipe(signUpRequest.getUsername(), signUpRequest.getEmail(),signUpRequest.getPhoneNumber(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();


        Role adminRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(adminRole);

        membreEquipe.setRoles(roles);
        userRepository.save(membreEquipe);

        // Convertir l'image en base64
        String imagePath = "D:/AGTIA_backend/springsecurity-service/upload-dir/image.png";
        String base64Image = ImageUtil.encodeImageToBase64(imagePath);
        //mail confirmation
        String from ="admin@gmail.com" ;
        String to = signUpRequest.getEmail();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Complete Registration!");
        helper.setFrom(from);
        helper.setTo(to);

        String htmlContent = "<html><body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #f0f0f0; border-radius: 10px; background-color: #ffffff;'>"
                + "<h2 style='text-align: center; color: #4CAF50;'>Welcome to Our Service!</h2>"
                + "<p>Dear " + signUpRequest.getUsername() + ",</p>"
                + "<p>Thank you for registering. Please confirm your email address by clicking the button below:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href=\"http://localhost:8050/auth/confirme?email=" + signUpRequest.getEmail()
                + "\" style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>VERIFY</a>"
                + "</div>"
                + "<p>If you did not create an account, please ignore this email.</p>"
                + "<p>Best regards,<br>Your Company Name</p>"
                + "<hr style='border: none; border-top: 1px solid #f0f0f0;'/>"
                + "<p style='font-size: 0.9em; color: #777;'>If you have any questions, feel free to <a href='mailto:support@yourcompany.com'>contact us</a>.</p>"
                + "<img src='data:image/png;base64," + base64Image + "' alt='Company Logo' style='max-width: 100%; height: auto;'/>"
                + "</div>"
                + "</body></html>";

        helper.setText(htmlContent, true);
        javaMailSender.send(message);

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully! check your email for confirmation"));
    }

}
