package com.example.my_blog.dto;

import com.example.my_blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    private String title;

    private String content;

    public Article toEntity(){
        return Article.builder()
                .content(content)
                .title(title)
                .build();
    }

}
