package com.example.my_blog.controller;

import com.example.my_blog.domain.Article;
import com.example.my_blog.domain.Member;
import com.example.my_blog.dto.AddMemberRequest;
import com.example.my_blog.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("addMember : 회원가입에 성공한다.")
    public void addMember() throws Exception {
        final String url = "/api/members";
        final String email = "example@email.com";
        final String password = "password";
        final String nickname = "nickname";
        final AddMemberRequest userRequest = new AddMemberRequest(email,password,nickname);

        final String request = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        );

        result.andExpect(status().isCreated());

        List<Member> members = memberRepository.findAll();

        assertThat(members.get(0).getEmail()).isEqualTo(email);
        assertThat(members.get(0).getNickname()).isEqualTo(nickname);
        assertThat(members.get(0).getPassword()).isEqualTo(password);
    }

    //duplicated --> "NNNN" : 중복 "NNNY" : 비중복
    @Test
    @DisplayName("checkNickname : 닉네임이 중복되지 않는 경우 검사에 성공한다.")
    public void checkNickname_NotDuplicated() throws Exception {
        final String url = "/api/members/duplicates/nickname/{nickname}";
        final String newNickname = "UniqueNickname";

        mockMvc.perform(get(url, newNickname))
                .andExpect(status().isOk())
                .andExpect(content().string("NNNY"));
    }
    @Test
    @DisplayName("findMember : 닉네임으로 회원정보 조회에 성공한다.")
    public void findMember() throws Exception{
        final String url = "/api/members/{nickname}";
        Member member = createDefaultMember();

        ResultActions result = mockMvc.perform(get(url,member.getNickname()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.nickname").value(member.getNickname()));

    }

    @Test
    @DisplayName("checkNickname : 닉네임이 중복된 경우 검사에 성공한다.")
    public void checkNickname_Duplicated() throws Exception {
        final String url = "/api/members/duplicates/nickname/{nickname}";

        Member member = createDefaultMember();

        mockMvc.perform(get(url, member.getNickname()))
                .andExpect(status().isOk())
                .andExpect(content().string("NNNN"));
    }


    @Test
    @DisplayName("checkEmail : 이메일이 중복되지 않는 경우 검사에 성공한다.")
    public void checkEmail_NotDuplicated() throws Exception {
        final String url = "/api/members/duplicates/email/{email}";
        final String newEmail = "example@newEamil.com";

        mockMvc.perform(get(url, newEmail))
                .andExpect(status().isOk())
                .andExpect(content().string("NNNY")); // 비중복 검증
    }

    @Test
    @DisplayName("checkEmail : 이메일이 중복된 경우 검사에 성공한다.")
    public void checkEmail_Duplicated() throws Exception {
        final String url = "/api/members/duplicates/email/{email}";

        Member member = createDefaultMember();

        mockMvc.perform(get(url, member.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("NNNN")); // 중복 검증
    }

    private Member createDefaultMember() {
        return memberRepository.save(
                Member.builder()
                        .password("password")
                        .email("example@email.com")
                        .nickname("nickname")
                        .build()
        );
    }
}