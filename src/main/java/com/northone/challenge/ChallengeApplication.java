package com.northone.challenge;

import com.northone.challenge.model.Status;
import com.northone.challenge.model.Task;
import com.northone.challenge.repository.TaskRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@SpringBootApplication
@Slf4j
@EnableMongoAuditing
public class ChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

	@Bean
	ApplicationRunner init(TaskRepository repository) {
			return args -> {
			repository.save(new Task("Do Something #1", "Task #1", LocalDateTime.now().plus(120, ChronoUnit.MINUTES), Status.INPROGRESS));
			repository.save(new Task("Do Something #2", "Task #2", LocalDateTime.now().minus(240, ChronoUnit.MINUTES), Status.DONE));
			repository.save(new Task("Do Something #3", "Task #3", LocalDateTime.now().plus(240, ChronoUnit.MINUTES), Status.PENDING));
		};
	}

	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Todo API")
						.description("Challenge Todo API application")
						.version("v0.0.1"));
	}

	@Bean
	RepositoryRestConfigurer repositoryRestConfigurer() {
		return RepositoryRestConfigurer.withConfig(config -> {
			config.exposeIdsFor(Task.class);
		});
	}

	@WritingConverter
	public static class StatusWriteConverter implements Converter<Status, Integer> {
		@Override
		public Integer convert(Status source) {
			return source.getCode();
		}
	}

	@ReadingConverter
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
	public MongoCustomConversions customConversions(){
		return new MongoCustomConversions(Arrays.asList(new StatusWriteConverter(),
				new StatusReadConverter()));
	}

}
