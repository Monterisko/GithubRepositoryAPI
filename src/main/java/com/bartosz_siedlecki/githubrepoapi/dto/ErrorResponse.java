package com.bartosz_siedlecki.githubrepoapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(@JsonProperty("status") int status,
                            @JsonProperty("message") String message) {
}
