package com.example.demo.member;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;


    public MemberService(MemberRepository memberRepository, AuthenticationManager authenticationManager) {
        this.memberRepository = memberRepository;
        this.authenticationManager = authenticationManager;
    }   

  
    public void signup(Member mm){

          if (memberRepository.existsByUserID(mm.getUserID())){
               throw new DuplicateUserException("이미 존재하는 아이디입니다.");
          }


          BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
          String hashed = passwordEncoder.encode(mm.getPwd());
          mm.setPwd(hashed);
          memberRepository.save(mm);

    }

    public void login(String userID, String pwd){
          Authentication authRequest = new UsernamePasswordAuthenticationToken(userID, pwd);

          Authentication authResult;

          try{
               authResult = authenticationManager.authenticate(authRequest);
          }catch (Exception e){
               throw new LoginFailException("아이디 또는 비밀번호가 일치하지 않습니다.");
          }

          //검증성공
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          context.setAuthentication(authResult);
          SecurityContextHolder.setContext(context);
     
    }

    public Member findByUserID(String userID){
          return memberRepository.findByUserID(userID);
    }

}
