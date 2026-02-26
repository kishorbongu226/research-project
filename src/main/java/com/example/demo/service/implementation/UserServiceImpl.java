// package com.example.demo.service.implementation;

// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.example.demo.entity.UserEntity;
// import com.example.demo.io.UserRequest;
// import com.example.demo.io.UserResponse;
// import com.example.demo.repository.UserRepository;
// import com.example.demo.service.UserService;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class UserServiceImpl implements UserService{

//     private final UserRepository userRepository;
//     private final PasswordEncoder passwordEncoder;

//     @Override
//     public UserResponse createUser(UserRequest request) {
//         UserEntity newUser = convertToEntity(request);
//         newUser = userRepository.save(newUser);
//         return convertToResponse(newUser);

//     }

    

//     @Override
//     public List<UserResponse> readUsers() {
//         return userRepository.findAll()
//                     .stream()
//                     .map(user -> convertToResponse(user))
//                     .collect(Collectors.toList());
//     }

//     @Override
//     public void deleteUser(String id) {
//         UserEntity existingUser = userRepository.findByUserId(id)
//                     .orElseThrow(() -> new UsernameNotFoundException("user not found"));
//         userRepository.delete(existingUser);            
//     }

//     private UserEntity convertToEntity(UserRequest request) {
//         return UserEntity.builder()
//                 .userId(UUID.randomUUID().toString())
//                 .email(request.getEmail())
//                 .password(passwordEncoder.encode(request.getPassword()))
//                 .build();

//     }

//     private UserResponse convertToResponse(UserEntity newUser) {
//         return UserResponse.builder()
                       
//                         .email(newUser.getEmail())
//                         .userId(newUser.getUserId())
//                         .createdAt(newUser.getCreatedAt())
//                         .updatedAt(newUser.getUpdatedAt())
                       
//                         .build();

//     }
    
// }