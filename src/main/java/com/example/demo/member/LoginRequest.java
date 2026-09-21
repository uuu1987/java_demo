package com.example.demo.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class LoginRequest {
    
    @Schema(description = "로그인 아이디", example = "hong123")
    @NotBlank(message = "아이디를 입력하세요")
    private String userID;

    @Schema(description = "비밀번호", example = "pass1234")
    @NotBlank(message = "비밀번호를 입력하세요")
    private String pwd;


}
