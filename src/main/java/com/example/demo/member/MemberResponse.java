package com.example.demo.member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonPropertyOrder({"id", "userID", "userName", "email"})

@Getter 
@Setter 
@NoArgsConstructor 
public class MemberResponse {
    private Long id;
    private String userID;
    private String userName;
    private String email;


    public static MemberResponse from(Member member){
        MemberResponse res = new MemberResponse();
        res.setId(member.getId());
        res.setUserID(member.getUserID());
        res.setUserName(member.getUserName());
        res.setEmail(member.getEmail());
        return res;
    }


}
