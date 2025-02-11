package com.example.my_blog.dto;

import com.example.my_blog.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private String email;
    private String nickname;

    @Builder
    public MemberResponse(Member member){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
