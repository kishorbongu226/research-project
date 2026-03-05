
package com.example.demo.service.implementation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ProfessorEntity;
import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;

    // ✅ Add Logger
    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

         logger.info("Attempting to load user by username: {}", username);

    // 1️⃣ Try to find student
    Optional<StudentEntity> optionalStudent = studentRepository.findByRegisterNo(username);
    if (optionalStudent.isPresent()) {
        StudentEntity student = optionalStudent.get();
        logger.info("Student found: {} with role {}", student.getRegisterNo(), student.getRole());

        return User.builder()
                .username(student.getRegisterNo())
                .password("{noop}" + student.getPassword())
                .roles(student.getRole().toString()) // Spring adds ROLE_ automatically
                .build();
    }

    // 2️⃣ Try to find professor
    Optional<ProfessorEntity> optionalProfessor = professorRepository.findByRegisterNo(username);
    if (optionalProfessor.isPresent()) {
        ProfessorEntity professor = optionalProfessor.get();
        logger.info("Professor found: {} with role {}", professor.getRegisterNo(), professor.getRole());

        return User.builder()
                .username(professor.getRegisterNo())
                .password("{noop}" + professor.getPassword())
                .roles(professor.getRole().toString())
                .build();
    }

    // 3️⃣ User not found
    logger.warn("User not found in Student or Professor: {}", username);
    throw new UsernameNotFoundException("User Not Found");

}
}