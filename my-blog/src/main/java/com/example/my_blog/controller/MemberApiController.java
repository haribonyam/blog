package com.example.my_blog.controller;

import com.example.my_blog.domain.Member;
import com.example.my_blog.dto.AddMemberRequest;
import com.example.my_blog.dto.AddMemberResponse;
import com.example.my_blog.dto.MemberResponse;
import com.example.my_blog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<AddMemberResponse> addMember(@RequestBody AddMemberRequest request){

        Member member = memberService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AddMemberResponse(member));
    }
    @GetMapping("/members/{nickname}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable String nickname){
        Member member = memberService.findByNickname(nickname);
        return ResponseEntity.ok().body(MemberResponse.builder()
                .member(member)
                .build());
    }
    /**
     * 중복검사에 대한 고뇌
     * 1.uri -> 명사를 고집해야하는가
     * 너무 복잡해지는 것 같기도
     * 2.중복 혹은 비중복시 같은 status code에 다른 body를 보내 구분하기 or status code로 구분하기
     *
     * 추후 더 생각 후에 정하고 현재는 명사 uri and 다른 body로 진행
     * message : 중복 NNNN 비중복 NNNY
     * */
    @GetMapping("/members/duplicates/nickname/{nickname}")
    public ResponseEntity<String> checkNickname(@PathVariable(value ="nickname") String nickname){
       String message =  memberService.isDuplicatedNickname(nickname);

        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/members/duplicates/email/{email}")
    public ResponseEntity<String> checkEmail(@PathVariable(value = "email") String email){
       String message = memberService.isDuplicatedEmail(email);

        return ResponseEntity.ok().body(message);
    }

}
