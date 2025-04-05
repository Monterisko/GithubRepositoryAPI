package com.bartosz_siedlecki.githubrepoapi.service;

import com.bartosz_siedlecki.githubrepoapi.dto.Branch;
import com.bartosz_siedlecki.githubrepoapi.dto.Repository;
import com.bartosz_siedlecki.githubrepoapi.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GithubRepoService implements IGithubRepoService {
    private final RestTemplate restTemplate;
    private final String githubApiUrl;

    public GithubRepoService(RestTemplate restTemplate,
                                 @Value("${github.api.url:https://api.github.com}") String githubApiUrl) {
        this.restTemplate = restTemplate;
        this.githubApiUrl = githubApiUrl;
    }

    @Override
    public List<Repository> getRepositoriesWithoutForksFromUser(String username) {
        try {
            String repoURL = String.format("%s/users/%s/repos", githubApiUrl, username);
            ResponseEntity<GithubRepo[]> response = restTemplate.getForEntity(repoURL, GithubRepo[].class);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFound("User " + username + " not found");
            }
            return Arrays.stream(Objects.requireNonNull(response.getBody())).filter(repository -> !repository.isFork()).map(repo -> {
                String branchURL = String.format("%s/repos/%s/%s/branches", githubApiUrl, repo.getOwner().getLogin(), repo.getName());
                ResponseEntity<GithubBranch[]> branchResponse = restTemplate.getForEntity(branchURL, GithubBranch[].class);
                List<Branch> branches = Arrays.stream(Objects.requireNonNull(branchResponse.getBody())).map(branch -> new Branch(branch.getName(), branch.getCommit().getSha())).toList();
                return new Repository(repo.getName(), repo.getOwner().getLogin(), branches);
            }).toList();
        }
        catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFound("User " + username + " not found");
        }
    }

    private static class GithubRepo {
        private String name;
        private boolean fork;
        private Owner owner;

        public String getName() {
            return name;
        }

        public boolean isFork() {
            return fork;
        }

        public Owner getOwner() {
            return owner;
        }
    }

    private static class Owner {
        private String login;

        public String getLogin() {
            return login;
        }
    }

    private static class GithubBranch {
        private String name;
        private Commit commit;

        public String getName() {
            return name;
        }

        public Commit getCommit() {
            return commit;
        }
    }

    private static class Commit {
        private String sha;

        public String getSha() {
            return sha;
        }
    }
}
