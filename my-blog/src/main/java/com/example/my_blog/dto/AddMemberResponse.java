package com.example.my_blog.dto;

import com.example.my_blog.domain.Member;
import lombok.Getter;

@Getter
public class AddMemberResponse {
    private String email;
    private String nickname;

    public AddMemberResponse(Member member){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
