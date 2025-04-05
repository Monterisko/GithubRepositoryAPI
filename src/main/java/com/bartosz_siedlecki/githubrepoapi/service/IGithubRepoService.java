package com.bartosz_siedlecki.githubrepoapi.service;

import com.bartosz_siedlecki.githubrepoapi.dto.Repository;

import java.util.List;

public interface IGithubRepoService {
    List<Repository> getRepositoriesWithoutForksFromUser(String username);
}
