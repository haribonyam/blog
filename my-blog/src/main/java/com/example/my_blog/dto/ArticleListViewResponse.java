package com.example.my_blog.dto;

import com.example.my_blog.domain.Article;
import lombok.Getter;

@Getter
public class ArticleListViewResponse {
    private Long id;
    private String content;
    private String title;

    public ArticleListViewResponse(Article article){
        this.id = article.getId();
        this.content = article.getContent();
        this.title = article.getTitle();
    }
}
