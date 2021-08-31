package com.northone.challenge.model;

import com.querydsl.core.annotations.QueryEntity;
import io.swagger.annotations.ApiModel;
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
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}


