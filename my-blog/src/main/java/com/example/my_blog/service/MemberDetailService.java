package com.example.my_blog.service;


import com.example.my_blog.domain.Member;
import com.example.my_blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public Member loadUserByUsername(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(("Not found : "+email)));
    }
}
