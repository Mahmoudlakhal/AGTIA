package com.example.springsecurity.controllers;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.example.springsecurity.models.*;
import com.example.springsecurity.security.services.UserDetailsServiceImpl;
import com.example.springsecurity.services.Userservice;

import com.example.springsecurity.utils.EmailService;
import com.example.springsecurity.utils.ImageUtil;
import com.example.springsecurity.utils.StorageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.springsecurity.exception.TokenRefreshException;
import com.example.springsecurity.payload.request.LoginRequest;
import com.example.springsecurity.payload.request.SignupRequest;
import com.example.springsecurity.payload.request.TokenRefreshRequest;
import com.example.springsecurity.payload.response.JwtResponse;
import com.example.springsecurity.payload.response.MessageResponse;
import com.example.springsecurity.payload.response.TokenRefreshResponse;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.security.jwt.JwtUtils;
import com.example.springsecurity.security.services.RefreshTokenService;
import com.example.springsecurity.security.services.UserDetailsImpl;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
public class AuthController {
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

  @Autowired
  JwtUtils jwtUtils;
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Autowired
  private StorageService storage;

  @GetMapping("/all")
  public List<User> getAllusers() {
    return userRepository.findAll();
  }


  @GetMapping("/confirme")
  public ResponseEntity<?> confirme (@RequestParam String email){

    User u= userRepository.findByEmail(email);
    if(u != null){
         u.setConfirm(true);
         userRepository.save(u);
        return ResponseEntity.ok(new MessageResponse(" user confirmé "));
    }
    else{
    return ResponseEntity.ok(new MessageResponse(" ERROR "));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest ) {
   try {
    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    Optional<User> u = userRepository.findByUsername(loginRequest.getUsername());
    if(u.isPresent()){
    if( u.get().getConfirm() == true) {

      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      String jwt = jwtUtils.generateJwtToken(userDetails);

      List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
              .collect(Collectors.toList());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getImage(),
                roles));}
    else {
      return ResponseEntity.ok(new MessageResponse("user n,a pas confirmé"));
      //throw new RuntimeException("user n'a pas confirmé");
    }}
    else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body("USER NOT FOUND");

    }
   }
    catch (AuthenticationException ae){
        return ResponseEntity.ok(new MessageResponse("Invalid userName or Password"));
    }

  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid SignupRequest signUpRequest, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws MessagingException, IOException {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),signUpRequest.getPhoneNumber(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "provider":
            Role providerRole = roleRepository.findByName(ERole.ROLE_PROVIDER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(providerRole);

            break;
          case "customer":
            Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(customerRole);
            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);

    String original=storage.store(file,300,300);
    user.setImage(original);

    userRepository.save(user);


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

    return ResponseEntity.ok(new MessageResponse("User registered successfully! check your email for confirmation"));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }
  
  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
    Long userId = userDetails.getId();
    refreshTokenService.deleteByUserId(userId);
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  }



  // Méthode pour générer un code de vérification aléatoire
  private String generateVerificationCode() {
    Random random = new Random();
    int code = 100000 + random.nextInt(900000);
    return String.valueOf(code);
  }


  @PostMapping("/forgetpassword")
  public HashMap<String, String> resetPassword(String email) throws MessagingException {
    HashMap message = new HashMap();
    User userexisting = userRepository.findByEmail(email);

    if (userexisting == null) {
      message.put("user", "user not found");
      return message;
    }
    UUID token = UUID.randomUUID();
    userexisting.setPasswordResetToken(token.toString());
    userexisting.setId(userexisting.getId());
    String from = "admin@gmail.com";
    String to = userexisting.getEmail();
    MimeMessage messagee = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(messagee);
    helper.setSubject("Forget Password!");
    helper.setFrom(from);
    helper.setTo(to);
    // helper.setText("<HTML><body><a href=\"http://localhost:4200/resetpass/"+userexisting.getPasswordResetToken()+"\">Reset-now</a></body></HTML>", true);
    helper.setText("votre code est : " + userexisting.getPasswordResetToken(),true);
    javaMailSender.send(messagee);
    userRepository.saveAndFlush(userexisting);
    message.put("user", "user found, check your email");

    return message;
  }

  @PostMapping("/savePassword/{passwordResetToken}")
  public HashMap<String, String> savePassword(@PathVariable String passwordResetToken, String newPassword) {
    User userexisting = userRepository.findByPasswordResetToken(passwordResetToken);
    HashMap<String, String> message = new HashMap<>();

    if (userexisting != null) {
      userexisting.setId(userexisting.getId());
      userexisting.setPassword( encoder.encode(newPassword));
      userexisting.setPasswordResetToken(null);
      userRepository.save(userexisting);
      message.put("resetpassword", "processed");
      return message;
    } else {
      message.put("resetpassword", "failed");
      return message;
    }
  }

  @PutMapping("/changepass")
  public ResponseEntity<?> changePassword(String oldPassword,  String newPassword, @RequestParam("token") String token) {
    // Vérifier si le token est valide et récupérer l'utilisateur correspondant
    String username = jwtUtils.getUserNameFromJwtToken(token);
    User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

    // Vérifier si l'ancien mot de passe est correct
    if (!encoder.matches(oldPassword, user.getPassword())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Incorrect old password"));
    }

    // Vérifier si le nouveau mot de passe est différent de l'ancien mot de passe
    if (newPassword.equals(oldPassword)) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: New password must be different from old password!"));
    }

    // Changer le mot de passe
    user.setPassword(encoder.encode(newPassword));
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
  }




  @GetMapping("/file/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    Resource file = storage.loadFile(filename);
    HttpHeaders headers = new HttpHeaders();
    Map<String, String> extensionToContentType = new HashMap<>();

    extensionToContentType.put("pdf", "application/pdf");
    extensionToContentType.put("jpg", "image/jpeg");
    extensionToContentType.put ("jpeg", "image/jpeg");
    extensionToContentType.put ("png", "image/png");
    extensionToContentType.put("ppt", "application/vnd.ms-powerpoint");
    extensionToContentType.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

    String fileExtension = FilenameUtils.getExtension(filename);
    String contentType = extensionToContentType.getOrDefault(fileExtension. toLowerCase(),
            MediaType.APPLICATION_OCTET_STREAM_VALUE);
    headers.setContentType(MediaType.parseMediaType(contentType));
    return ResponseEntity.ok().headers(headers).body (file);
  }
}

