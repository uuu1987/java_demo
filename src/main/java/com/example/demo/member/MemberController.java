package com.example.demo.member;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class MemberController {
  
    private final MemberService memberService;

    public MemberController(MemberService memberService){

        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Member m){
        
        try{
            memberService.signup(m);
            return ResponseEntity.status(200).body("회원가입 성공"+m.getUserID());
        } catch (DuplicateUserException e){
            return ResponseEntity.status(409).body("회원가입 실패: "+e.getMessage());  
            
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("회원가입 실패: "+e.getMessage());
        }
        /* 

        String success = memberService.signup(m);
        if (success == null){
            return ResponseEntity.status(201).body("회원가입 성공"+m.getUserID());
        }
        else if(success.equals("DUPLICATE")){
            return ResponseEntity.status(409).body("회원가입 실패: 이미 존재하는 아이디입니다.");         
        }
        else
        {
            return ResponseEntity.status(400).body("회원가입 실패: "+success);
        }
        */
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Member mm, HttpSession session){
       try{
            memberService.login(mm.getUserID(),mm.getPwd());
            session.setAttribute("userID", mm.getUserID());
            return ResponseEntity.status(200).body("로그인 성공");
       } catch (LoginFailException e){
            return ResponseEntity.status(401).body("로그인 실패" + e.getMessage());
       }
    }

    @GetMapping("/mypage")
    public ResponseEntity<String> mypage(HttpSession session){
        String userID = (String) session.getAttribute("userID");
        if (userID == null){
            return ResponseEntity.status(401).body("로그인 필요");
        }
        else{
            // 임시 검증용 — SecurityContext에 뭐가 들었는지 같이 확인
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();


            String securityInfo = (auth != null) ? auth.getName() + "/" + auth.isAuthenticated() : "SecurityContext 비어있음";
            return ResponseEntity.status(200).body(
                "마이페이지 접근 성공: " + userID + " | [SecurityContext] " + securityInfo
            );
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
        String userId = (String) session.getAttribute("userID");
        if (userId != null){
            session.invalidate();
            return ResponseEntity.status(200).body("로그아웃 성공"); 
        }else{
            return ResponseEntity.status(401).body("로그인 상태가 아닙니다.");
        }
    }

}