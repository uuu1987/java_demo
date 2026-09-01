package com.example.demo.member;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
public class MemberController {
  
    private final MemberService memberService;

    public MemberController(MemberService memberService){

        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest req){

        Member m = new Member();
        m.setUserID(req.getUserID());
        m.setUserName(req.getUserName());
        m.setPwd(req.getPwd());
        m.setEmail(req.getEmail());
 
        memberService.signup(m);
        return ResponseEntity.status(201).body("회원가입 성공"+m.getUserID());

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest mm, HttpSession session){
        memberService.login(mm.getUserID(), mm.getPwd());
        session.setAttribute("userID", mm.getUserID());
        return ResponseEntity.status(200).body("로그인 성공");

    }

    @GetMapping("/mypage")
    public ResponseEntity<?> mypage(HttpSession session){
        String userID = (String) session.getAttribute("userID");

        if (userID == null){
            return ResponseEntity.status(401).body("로그인 필요");
        }
        
        Member m = memberService.findByUserID(userID);
        MemberResponse res = new MemberResponse();
        res.setId(m.getId());
        res.setUserID(m.getUserID());
        res.setUserName(m.getUserName());
        res.setEmail(m.getEmail());

        return ResponseEntity.status(200).body(res);


        /*
        else{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();


            String securityInfo = (auth != null) ? auth.getName() + "/" + auth.isAuthenticated() : "SecurityContext 비어있음";
            return ResponseEntity.status(200).body(
                "마이페이지 접근 성공: " + userID + " | [SecurityContext] " + securityInfo
            );
        }
             */

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