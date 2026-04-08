package com.example.demo.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.ProfessorEntity;
import com.example.demo.entity.StudentEntity;
import com.example.demo.io.LoginRequest;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1.0/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String userId = request.getUserID() == null ? "" : request.getUserID().trim();
        String password = request.getPassword() == null ? "" : request.getPassword();
        String role = request.getRole() == null ? "" : request.getRole().trim();

        if (userId.isEmpty() || password.isEmpty() || role.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "All fields are required."));
        }

        if ("Admin".equalsIgnoreCase(role)) {
            Optional<ProfessorEntity> professor = professorRepository.findByOfficialEmail(userId);
            if (professor.isEmpty()) {
                professor = professorRepository.findByRegisterNo(userId);
            }

            if (professor.isEmpty() || !password.equals(professor.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid username or password."));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful.",
                    "role", "Admin",
                    "userID", professor.get().getOfficialEmail() != null
                            ? professor.get().getOfficialEmail()
                            : professor.get().getRegisterNo()));
        }

        if ("End-User".equalsIgnoreCase(role)) {
            Optional<StudentEntity> student = studentRepository.findByRegisterNo(userId);

            if (student.isEmpty() || !password.equals(student.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid username or password."));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful.",
                    "role", "End-User",
                    "userID", student.get().getRegisterNo()));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "You are not allowed to login with this account."));
    }
}
