package com.example.demo.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 

public class SignupRequest {
    @Schema(description = "로그인 아이디", example = "hong123")
    @NotBlank(message = "아이디를 입력하세요")
    private String userID;
    
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, message = "8자 이상")
    @Schema(description = "비밀번호 (8자 이상)", example = "pass1234")
    private String pwd;

    @NotBlank(message = "이름을 입력하세요.")
    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "올바른 이메일이 아닙니다.")
    @Schema(description = "이메일 주소", example = "hong123@example.com")
    private String email;

}
