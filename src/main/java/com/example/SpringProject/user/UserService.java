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

        User saved = userRepository.save(user);
        UserDTO result = new UserDTO();
        result.setEmail(saved.getEmail());
        result.setPassword(saved.getPassword());
        return result;
    }
}