package com.northone.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDate;

@Document(collection = "TASKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    private BigInteger id;
    private String description;
    /**
     * dddd
     */
    private String title;
    private LocalDate dueDate;
    private Status status;
}


