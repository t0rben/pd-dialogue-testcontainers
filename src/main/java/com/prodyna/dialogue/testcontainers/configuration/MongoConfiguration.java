package com.prodyna.dialogue.testcontainers.configuration;

import com.prodyna.dialogue.testcontainers.persistence.repository.NoteRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = NoteRepository.class)
public class MongoConfiguration {

}
