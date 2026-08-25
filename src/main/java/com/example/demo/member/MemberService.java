package com.example.demo.member;

import java.util.regex.Pattern;

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
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final AuthenticationManager authenticationManager;


    public MemberService(MemberRepository memberRepository, AuthenticationManager authenticationManager) {
        this.memberRepository = memberRepository;
        this.authenticationManager = authenticationManager;
    }   

  
    public void signup(Member mm){
          if (mm.getUserID() == null || mm.getUserID().isEmpty()){
               throw new IllegalArgumentException( "아이디를 입력해주세요.44444");
          }
          if (mm.getPwd() == null || mm.getPwd().isEmpty() || mm.getPwd().length() < 8){
               throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
          }   
          if (mm.getUserName() == null || mm.getUserName().isEmpty()){
               throw new IllegalArgumentException( "이름을 입력해주세요.");
          }


          if (mm.getEmail() == null || mm.getEmail().isEmpty() || !Pattern.matches(EMAIL_REGEX, mm.getEmail())){
               throw new IllegalArgumentException("올바른 이메일 주소를 입력해주세요.");
          }
          

          if (memberRepository.existsByUserID(mm.getUserID())){
               throw new DuplicateUserException("이미 존재하는 아이디입니다.");
          }


          BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
          String hashed = passwordEncoder.encode(mm.getPwd());
          mm.setPwd(hashed);
          memberRepository.save(mm);
          //return null;
          

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

}
