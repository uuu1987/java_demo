package com.example.demo.member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonPropertyOrder({"id", "userID", "userName", "email"})

@Getter 
@Setter 
@NoArgsConstructor 
    public class MemberResponse {
    @Schema(description = "회원 id", example = "1")
    private Long id;
    @Schema(description = "로그인 아이디", example = "hong123")
    private String userID;
    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;
    @Schema(description = "이메일 주소", example = "hong123@example.com")
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
