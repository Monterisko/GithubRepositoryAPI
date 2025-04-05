package com.bartosz_siedlecki.githubrepoapi.controller;

import com.bartosz_siedlecki.githubrepoapi.dto.ErrorResponse;
import com.bartosz_siedlecki.githubrepoapi.dto.Repository;
import com.bartosz_siedlecki.githubrepoapi.exceptions.UserNotFound;
import com.bartosz_siedlecki.githubrepoapi.service.GithubRepoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github-repos")
public class GithubRepositoryController {
    private GithubRepoService githubRepoService;

    public GithubRepositoryController(GithubRepoService githubRepoService) {
        this.githubRepoService = githubRepoService;
    }

    @GetMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGithubRepo(@PathVariable String username) {
        try {
            List<Repository> repositories = githubRepoService.getRepositoriesWithoutForksFromUser(username);
            return ResponseEntity.ok(repositories);
        }
        catch (UserNotFound e) {
            ErrorResponse errorResponse = new ErrorResponse(404, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

}
