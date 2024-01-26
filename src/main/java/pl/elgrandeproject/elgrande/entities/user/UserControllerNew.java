package pl.elgrandeproject.elgrande.entities.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user")
public class UserControllerNew {

    @GetMapping
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("hi user");
    }
}
