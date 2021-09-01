package net.sample.challenge;

import net.sample.challenge.model.Status;
import net.sample.challenge.model.Task;
import net.sample.challenge.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@SpringBootApplication
@Slf4j
@EnableMongoAuditing
public class ChallengeApplication {

	Logger LOG = LoggerFactory.getLogger(ChallengeApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

	@Bean
	/**
	 * Config to remove some spring data repo methods from published API Docs that aren't necessary.
	 */
	public OpenApiCustomiser operationIdCustomiser() {
		return openApi -> {
			Set<String> keys = openApi.getPaths().keySet().stream()
					.filter("/api/tasks/search/findAllBy"::equals)
					.collect(toSet());
			LOG.info("remove repo doc methods from API docs {}", keys);
			keys.forEach(key -> openApi.getPaths().remove(key));
		};
	}


	@Bean
	/**
	 * runs once at app start time for init tasks.
	 */
	ApplicationRunner init(TaskRepository repository, MongoTemplate mongoTemplate) {
		return args -> {
			TextIndexDefinition textIndex = new TextIndexDefinitionBuilder()
					.onField("title")
					.onField("description")
					.build();
			mongoTemplate.indexOps(Task.class).ensureIndex(textIndex);
			LOG.info("finished defining index for tasks collection for in-memory mongodb.");

			repository.save(new Task("Need to do Something #1", "Task #1", LocalDateTime.now().plus(120, ChronoUnit.MINUTES), Status.INPROGRESS));
			repository.save(new Task("Need to do Something #2", "Task #2", LocalDateTime.now().minus(240, ChronoUnit.MINUTES), Status.DONE));
			repository.save(new Task("Need to do Something #3", "Task #3", LocalDateTime.now().plus(240, ChronoUnit.MINUTES), Status.PENDING));
			LOG.info("finished seeding tasks for in-memory mongodb.");

		};
	}

	@Bean
	/**
	 * config spring data rest to include entity ids in json responses
	 */
	RepositoryRestConfigurer repositoryRestConfigurer() {
		return RepositoryRestConfigurer.withConfig(config -> {
			config.exposeIdsFor(Task.class);
		});
	}

	@WritingConverter
	/**
	 * java to mongo json Task doc for Status
	 */
	public static class StatusWriteConverter implements Converter<Status, Integer> {
		@Override
		public Integer convert(Status source) {
			return source.getCode();
		}
	}

	@ReadingConverter
	/**
	 * mongo json Task doc Status to java
	 */
	public static class StatusReadConverter implements Converter<Integer, Status> {
		@Override
		public Status convert(Integer source) {
			return Arrays.stream(Status.values())
					.filter(enumInstance -> Integer.valueOf(enumInstance.getCode()).equals(source))
					.findFirst()
					.orElse(Status.UNKOWN);
		}
	}

	@Bean
	/**
	 * load the custom mongo converts into container
	 */
	public MongoCustomConversions customConversions(){
		return new MongoCustomConversions(Arrays.asList(new StatusWriteConverter(),
				new StatusReadConverter()));
	}

}
