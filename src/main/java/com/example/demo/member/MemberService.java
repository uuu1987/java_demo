package com.example.demo.member;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor 
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;

    @Transactional 
    public void signup(Member mm){
          log.debug("signup 진입, userID={}", mm.getUserID());

          if (memberRepository.existsByUserID(mm.getUserID())){
               log.warn("중복 가입 시도: userID={}", mm.getUserID());
               throw new DuplicateUserException("이미 존재하는 아이디입니다.");
          }


          BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
          String hashed = passwordEncoder.encode(mm.getPwd());
          mm.setPwd(hashed);
          memberRepository.save(mm);

          log.info("회원가입 완료: userID={}", mm.getUserID());
    }

    public void login(String userID, String pwd){
          log.debug("login 진입, userID={}", userID);

          Authentication authRequest = new UsernamePasswordAuthenticationToken(userID, pwd);

          Authentication authResult;

          try{
               authResult = authenticationManager.authenticate(authRequest);
          }catch (Exception e){
               log.warn("로그인 실패: userID={}", userID);
               throw new LoginFailException("아이디 또는 비밀번호가 일치하지 않습니다.");
          }

          //검증성공
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          context.setAuthentication(authResult);
          SecurityContextHolder.setContext(context);

          log.info("로그인 성공: userID={}", userID);
    }

    public Member findByUserID(String userID){
          return memberRepository.findByUserID(userID);
    }

}
