package com.it355.movie_management.dtos.comment;

import java.time.OffsetDateTime;

public record CommentResponseDto(
        Long id,
        String comment,
        OffsetDateTime createdAt,
        Long userId,
        String username
) {}