package com.northone.challenge.repository;

import com.northone.challenge.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigInteger;

@RepositoryRestResource(collectionResourceRel = "tasks", path = "tasks")
public interface TaskRepository extends MongoRepository<Task, BigInteger> {
}
