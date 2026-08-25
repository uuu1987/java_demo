package com.example.demo.member;

public class LoginFailException extends RuntimeException{
    public LoginFailException(String msg)
    {
        super(msg);
    }

}
