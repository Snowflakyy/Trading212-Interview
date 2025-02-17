package com.project.trading212.backend.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomError {
    private String timestamp;
    private String errorCode;
    private String errorMessage;
    private String description;
}
