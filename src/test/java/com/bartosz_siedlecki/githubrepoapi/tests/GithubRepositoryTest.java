package com.bartosz_siedlecki.githubrepoapi.tests;

import com.bartosz_siedlecki.githubrepoapi.service.GithubRepoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.bartosz_siedlecki.githubrepoapi.GithubRepoApiApplication;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = GithubRepoApiApplication.class)
@AutoConfigureMockMvc
public class GithubRepositoryTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GithubRepoService githubRepoService;


    @AfterEach
    void tearDown() {
        reset(githubRepoService);
    }

    @Test
    public void whenUserNotFoundThenReturns404() throws Exception {
        mockMvc.perform(get("/api/github-repos/qMonteriskoq"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.message").value("User qMonteriskoq not found"));
    }

    @Test
    void whenValidRequestThenReturns200() throws Exception {
        mockMvc.perform(get("/api/github-repos/octocat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void whenRequestWithoutAcceptHeaderThenReturnsJsonByDefault() throws Exception {
        when(githubRepoService.getRepositoriesWithoutForksFromUser("user1"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/github-repos/user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
