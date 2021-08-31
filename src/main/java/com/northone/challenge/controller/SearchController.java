package com.northone.challenge.controller;

import com.northone.challenge.model.Task;
import com.northone.challenge.repository.TaskRepository;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Search", description = "Search API for Todos", tags = { "Search" })
public class SearchController {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/api/tasks/search")
    @ApiOperation(value="Search for tasks", tags="Search")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "title", dataType = "string", paramType = "query", allowMultiple = false,
                    value = "Partial match of title", defaultValue="test"
            ),
            @ApiImplicitParam(
                    name = "description", dataType = "string", paramType = "query", allowMultiple = false,
                    value = "Partial match of description", defaultValue="test"
            ),
            @ApiImplicitParam(
                    name = "status", dataType = "string", paramType = "query", allowMultiple = false,
                    allowableValues = "PENDING, DONE, INPROGRESS", defaultValue="DONE",
                    value = "State of the task"
            )
    })
    RepresentationModel<?> searchTasks(
            @QuerydslPredicate(root = Task.class) Predicate predicate,
            Pageable pageable, PagedResourcesAssembler<Task> assembler) {
        var matches = taskRepository.findAll(predicate, pageable);
        return matches.isEmpty() ? assembler.toEmptyModel(matches ,Task.class) :
                assembler.toModel(matches);
    }

}
