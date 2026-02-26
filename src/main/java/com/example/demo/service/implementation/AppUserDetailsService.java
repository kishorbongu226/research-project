// package com.example.demo.service.implementation;

// import java.util.Collections;

// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.example.demo.entity.UserEntity;
// import com.example.demo.repository.UserRepository;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class AppUserDetailsService implements UserDetailsService{


//     private final UserRepository userRepository;


//     @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//         UserEntity existingUser = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException("email not found for the email"+email));

//         return new User(existingUser.getEmail(),existingUser.getPassword(),Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));

//     }

    
// }