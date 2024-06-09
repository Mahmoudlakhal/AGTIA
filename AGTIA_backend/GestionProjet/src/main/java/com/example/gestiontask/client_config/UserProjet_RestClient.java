package com.example.gestiontask.client_config;

import com.example.gestiontask.models.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
@FeignClient(name = "SECURITY-SERVICE")
public interface UserProjet_RestClient {

    @GetMapping("/user/getone/{id}")
    @CircuitBreaker(name = "security_service", fallbackMethod = "defaultUserProjet")
    User findUserById(@PathVariable Long id);

    @PutMapping("/user/roles/{id}")
    @CircuitBreaker(name = "security_service", fallbackMethod = "defaultUserProjet")
    User updateRole(@PathVariable Long id);

    default User defaultUserProjet(Long id, Throwable e){
        User user = new User();
        user.setId(id);
        user.setUsername("Not valable");
        return user;
    }
}

