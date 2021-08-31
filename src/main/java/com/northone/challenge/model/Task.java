package com.northone.challenge.model;

import com.querydsl.core.annotations.QueryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "TASKS")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@QueryEntity
/**
 * The domain model for the Task Entity
 */
public class Task {
    @Id
    @Schema(hidden = true)
    private String id;

    @NonNull
    private String description;
    @NonNull
    private String title;
    @NonNull
    private LocalDateTime dueDate;
    @NonNull
    private Status status;

    @CreatedDate
    @Schema(hidden = true)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Schema(hidden = true)
    private LocalDateTime lastModifiedDate;

}


