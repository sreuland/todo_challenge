package net.sample.challenge.model;

import com.querydsl.core.annotations.QueryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

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
    @TextIndexed
    private String description;

    @NonNull
    @TextIndexed
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

    // transient, only used for text search results
    @TextScore
    Float score;

}


