package com.example.SpringProject.user;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
    
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // REGISTER
    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO dto) {
    	
        return userService.register(dto);
    }

    // LOGIN (Basic Auth)
    @GetMapping("/login")
    public String login(Authentication authentication) {
        return "Login successful for " + authentication.getName();
    }
}




