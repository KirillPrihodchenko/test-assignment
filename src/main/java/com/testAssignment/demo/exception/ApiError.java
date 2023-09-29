package com.testAssignment.demo.exception;

import java.time.LocalDateTime;

public record ApiError(
        int statusCode,
        String path,
        String message,
        LocalDateTime localDateTime) { }