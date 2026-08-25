package com.example.demo.member;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberUserDetailsService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException{
        Member member = memberRepository.findByUserID(userID);
        if(member == null){
            throw new UsernameNotFoundException("존재하지 않는 아이디 :" + userID);            
        }

        return User.builder().username(member.getUserID()).password(member.getPwd()).roles("USER").build();
    }
}
