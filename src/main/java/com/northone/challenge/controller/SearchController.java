package com.northone.challenge.controller;

import com.northone.challenge.model.Task;
import com.northone.challenge.repository.TaskRepository;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

@RestController
@Tag(name = "Todo Search API", description = "Search API for Todos")
@RequestMapping("/api")
public class SearchController {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping(value="/tasks/search")
    @Operation(summary="Search for tasks by full text index.")
    @PageableAsQueryParam
    RepresentationModel<?> searchTasks(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<Task> assembler,
            @Parameter(name="text", description = "search across all text fields of tasks")
                 @RequestParam(value="text", required = true) String text ) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(text);
        var matches = taskRepository.findAllBy(criteria, pageable);

        return matches.isEmpty() ? assembler.toEmptyModel(matches ,Task.class) :
            assembler.toModel(matches);
    }

    @GetMapping("/tasks/query")
    @Operation(summary="Search for tasks by multiple fields at same time. Text fields use 'contains' matching, all other fields use exact matching.")
    @PageableAsQueryParam
    RepresentationModel<?> queryTasks(
            @QuerydslPredicate(root = Task.class)  Predicate predicate,
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<Task> assembler) {
        var matches= taskRepository.findAll(predicate, pageable);

        return matches.isEmpty() ? assembler.toEmptyModel(matches ,Task.class) :
                assembler.toModel(matches);
    }
}
