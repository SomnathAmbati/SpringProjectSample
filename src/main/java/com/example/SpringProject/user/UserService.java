package com.example.SpringProject.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SpringProject.common.AppEnums.RoleType;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserService  {
  
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO register(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(RoleType.USER);
        user.setName(dto.getName());
        User saved = userRepository.save(user);
        return new UserDTO(saved.getName(),saved.getEmail(),saved.getPassword());
    }
    
    
}








