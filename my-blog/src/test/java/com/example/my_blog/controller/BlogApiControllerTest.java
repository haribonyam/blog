package com.example.my_blog.controller;

import com.example.my_blog.domain.Article;
import com.example.my_blog.dto.AddArticleRequest;
import com.example.my_blog.dto.UpdateArticleRequest;
import com.example.my_blog.repository.BlogRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle : 아티클 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception{
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title,content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);

    }

    @DisplayName("findAllArticles : 아티클 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception{
        final String url = "/api/articles";
        Article savedArticle = createDefaultArticle();

        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON_VALUE));
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    @DisplayName("findArticle : 아티클 단건 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception{
        final String url = "/api/articles/{id}";
        Article saveArticle = createDefaultArticle();

        final ResultActions result = mockMvc.perform(get(url,saveArticle.getId()));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(saveArticle.getContent()))
                .andExpect(jsonPath("$.title").value(saveArticle.getTitle()));
    }

    @DisplayName("deleteArticle : 아티클 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception{
        final String url = "/api/articles/{id}";
        Article saveArticle = createDefaultArticle();

        mockMvc.perform(delete(url,saveArticle.getId()))
                .andExpect(status().isOk());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle : 아티클 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception{
        final String url = "/api/articles/{id}";
        final String newTitle = "New title";
        final String newContent = "New content";
        final UpdateArticleRequest userRequest = new UpdateArticleRequest(newTitle,newContent);

        Article savedArticle = createDefaultArticle();

        ResultActions result = mockMvc.perform(put(url,savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userRequest)));

        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getContent()).isEqualTo(newContent);
        assertThat(article.getTitle()).isEqualTo(newTitle);

    }




    private Article createDefaultArticle() {
        return blogRepository.save(
                Article.builder()
                        .title("title")
                        .content("content")
                        .build()
        );
    }
}
