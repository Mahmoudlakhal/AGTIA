package com.example.gestiontask.controller;

import com.example.gestiontask.client_config.UserProjet_RestClient;
import com.example.gestiontask.models.Projet;
import com.example.gestiontask.repository.ProjetRepository;
import com.example.gestiontask.services.ProjetIservice;
import com.example.gestiontask.utils.StorgeService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projet")

public class ProjetController {
    @Autowired
    private ProjetIservice projetIservice;
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private StorgeService storgeService;


    @Autowired
    private UserProjet_RestClient userClient;
   /* @PostMapping("/addProjet")
    Projet addProjet(@RequestBody Projet projet){
        return projetIservice.addProjet(projet);
    }*/

   /* @PostMapping("/addProject")
    Projet addProject(@RequestBody Projet projet){
        return projetIservice.addProjet(projet);
    }*/

    @PostMapping("/addProjet/{id_user}")
    Projet addProject(Projet projet,
                     @RequestParam("file")MultipartFile file
                    ,@PathVariable Long id_user ){
        String original=storgeService.storee(file,300,300);
        projet.setIcon(original);
        return projetIservice.addProject(projet ,id_user);
    }

    @PostMapping("/addProjet")
    Projet addProjet(Projet projet,
                     @RequestParam("file")MultipartFile file
            ){

        return projetIservice.addProjet(projet);
    }

    @DeleteMapping("/DeleteProjet/{id}")
    HashMap<String,String> DeleteProjet(@PathVariable Long id){
        return projetIservice.DeleteProjet(id);
    }


    @PutMapping("/UpdateProjet/{id}")
    Projet updateProjet(@RequestBody Projet projet,@PathVariable Long id){
        return projetIservice.updateProjet(projet,id);
    }

    @GetMapping("/getAllProjet")
    public List<Projet> getAllProjet() {
        return projetIservice.getAllProjet();
    }

    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storgeService.loadFile(filename);
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

    @GetMapping("/getProjetById/{id}")
    Projet getProjetById(@PathVariable Long id)
    {
        return projetIservice.getProjetById(id);
    }

    @PutMapping("/affecter/{projetId}")
    public ResponseEntity<Projet> affecterMembresAuProjet(@PathVariable Long projetId, @RequestParam List<Long> listeIdsMembres) {
        Projet projet = projetIservice.affectMembre(projetId, listeIdsMembres);
        if (projet != null) {
            return ResponseEntity.ok(projet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
