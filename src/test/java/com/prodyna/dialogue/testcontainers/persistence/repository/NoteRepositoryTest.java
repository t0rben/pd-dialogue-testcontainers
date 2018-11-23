package com.prodyna.dialogue.testcontainers.persistence.repository;

import com.prodyna.dialogue.testcontainers.DockerImages;
import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = NoteRepositoryTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class NoteRepositoryTest {

    @ClassRule
    public static GenericContainer mongo = new GenericContainer(DockerImages.MONGO).withCommand("mongod", "--port", "27017").withExposedPorts(27017);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        String mongoUri = "mongodb://" + mongo.getContainerIpAddress() + ":" + mongo.getMappedPort(27017);

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            TestPropertyValues.of(
                    "spring.data.mongodb.uri:" + mongoUri
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private NoteRepository noteRepository;

    @Before
    public void resetDependencies() {

        noteRepository.deleteAll();
    }

    @Test
    public void writingTest() {

        for (int i = 0; i < 1000; i++) {
            Note note = new Note();
            note.setContent(UUID.randomUUID().toString());
            noteRepository.save(note);
        }

        Assert.assertEquals(1000, noteRepository.count());

    }
}