package com.northone.challenge;

import com.northone.challenge.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class ChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
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
			return Arrays.stream(Status.values()).
					filter(enumInstance -> Integer.valueOf(enumInstance.getCode()).equals(source)).
					findFirst().
					orElse(Status.UNKOWN);
		}
	}

	@Bean
	public MongoCustomConversions customConversions(){
		return new MongoCustomConversions(Arrays.asList(new StatusWriteConverter(),
				new StatusReadConverter()));
	}

}
