package com.example.my_blog.repository;

import com.example.my_blog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByNickname(String nickname);

    Optional<Object> findByEmail(String email);
}
