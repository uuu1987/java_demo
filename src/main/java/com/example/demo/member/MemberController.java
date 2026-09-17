package com.example.demo.member;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor 
public class MemberController {
  
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "아이디/비밀번호/이름/이메일을 받아 신규 회원을 등록한다.")
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

    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인하고 세션을 발급한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "400", description = "아이디/비밀번호 입력 오류"),
        @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 불일치")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest mm, HttpSession session){
        memberService.login(mm.getUserID(), mm.getPwd());
        session.setAttribute("userID", mm.getUserID());
        return ResponseEntity.status(200).body("로그인 성공");

    }


    @Operation(summary = "마이페이지", description = "로그인된 마이페이지를 본다")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "마이페이지 접근 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping("/mypage")
    public ResponseEntity<?> mypage(HttpSession session){
        String userID = (String) session.getAttribute("userID");

        Member m = memberService.findByUserID(userID);
        return ResponseEntity.status(200).body(MemberResponse.from(m));


    }

    @Operation(summary = "로그아웃", description = "로그인된걸 로그아웃을 시킨다")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 상태가 아님")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
     
            session.invalidate();
            return ResponseEntity.status(200).body("로그아웃 성공"); 
      
    }

}