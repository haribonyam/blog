package com.example.my_blog.dto;

import com.example.my_blog.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddMemberRequest {

    private String email;
    private String password;
    private String nickname;


}
