package com.example.my_blog.service;

import com.example.my_blog.domain.Member;
import com.example.my_blog.dto.AddMemberRequest;
import com.example.my_blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //Security 설정 후 비밀번호 암호화 HS256
    public Member save(AddMemberRequest request) {
        Member member = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .build();
        memberRepository.save(member);

        return member;
    }

    public String isDuplicatedNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent() ? "NNNN" : "NNNY";
    }

    public String isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email).isPresent() ? "NNNN" : "NNNY";
    }

    public Member findByNickname(String nickname) {

        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("Not found : "+ nickname));
    }
}
