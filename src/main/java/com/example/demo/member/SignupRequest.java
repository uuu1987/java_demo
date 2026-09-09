package com.example.demo.member;

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
    @NotBlank(message = "아이디를 입력하세요")
    private String userID;
    
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, message = "8자 이상")
    private String pwd;

    @NotBlank(message = "이름을 입력하세요.")
    private String userName;
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "올바른 이메일이 아닙니다.")
    private String email;

}
