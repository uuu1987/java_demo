package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.member.DuplicateUserException;
import com.example.demo.member.LoginFailException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> globalExceptionHandler(IllegalArgumentException e){
        return ResponseEntity.status(400).body("오류"+e.getMessage());
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> duplicateUserException(DuplicateUserException e){
        return ResponseEntity.status(409).body("오류"+e.getMessage());
    }

    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<String> loginfailException(LoginFailException e){
        return ResponseEntity.status(401).body("오류"+e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationException(MethodArgumentNotValidException e){
          String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body("오류: " + message);
    }

}
