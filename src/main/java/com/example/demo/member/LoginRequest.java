package com.example.demo.member;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank(message = "아이디를 입력하세요")
    private String userID;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String pwd;

    public LoginRequest(){

    }

    public String getUserID(){
        return userID;
    }
    public void setUserID(String userID){
        this.userID = userID;
    }
    public String getPwd(){
        return pwd;
    }
    public void setPwd(String pwd){
        this.pwd = pwd;
    }
}
