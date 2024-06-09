package com.example.gestiontask.client_config;

import com.example.gestiontask.models.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

//depanage micro
//@FeignClient("SECURITY-SERVICE")
public interface ChefProjet_RestClient {

    @GetMapping("/user/getone/{id}")
    @CircuitBreaker(name = "security_service", fallbackMethod = "defaultUserProjet")
    User findUserById(@PathVariable Long id);

    @PutMapping("/user/roles{id}")
    //test
    @CircuitBreaker(name = "security_service",fallbackMethod = "defaultChefProjet")
    User updateRole(@PathVariable Long id);

    default User defaultChefProjet(Long id, Exception e){
        User chefProjet = new User();
        chefProjet.setId(id);
        chefProjet.setUsername("Not valable");
        return chefProjet;
    }



}
