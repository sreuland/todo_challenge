package com.northone.challenge.controller;

import com.northone.challenge.model.Task;
import com.northone.challenge.repository.TaskRepository;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Todo Search API", description = "Search API for Todos")
@RequestMapping("/api")
public class SearchController {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/tasks/search")
    @Operation(summary="Search for tasks by multiple fields at same time")
    @PageableAsQueryParam
    RepresentationModel<?> searchTasks(
            @QuerydslPredicate(root = Task.class)  Predicate predicate,
            @Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<Task> assembler) {
        var matches = taskRepository.findAll(predicate, pageable);
        return matches.isEmpty() ? assembler.toEmptyModel(matches ,Task.class) :
                assembler.toModel(matches);
    }

}
