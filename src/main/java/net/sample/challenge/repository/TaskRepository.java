package net.sample.challenge.repository;

import net.sample.challenge.model.QTask;
import net.sample.challenge.model.Task;
import com.querydsl.core.types.dsl.StringPath;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@Tag(name="Todo Task Entity", description = "CRUD API for Tasks")
@RepositoryRestResource(collectionResourceRel = "tasks", path = "tasks")
public interface TaskRepository extends MongoRepository<Task, String>,
        QuerydslPredicateExecutor<Task> , QuerydslBinderCustomizer<QTask> {

    @Override
    default void customize(QuerydslBindings bindings, QTask task) {
        bindings.excluding(task.createdDate, task.id, task.lastModifiedDate, task.score, task.dueDate);
        bindings.bind(String.class)
                .first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }

    @Operation(hidden = true)
    @Hidden
    Page<Task> findAllBy(TextCriteria criteria, Pageable pageable);
}
