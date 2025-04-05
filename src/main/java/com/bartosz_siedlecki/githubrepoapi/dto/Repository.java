package com.bartosz_siedlecki.githubrepoapi.dto;

import java.util.List;

public record Repository(String repositoryName, String owner, List<Branch> branches) {
}
