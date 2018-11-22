package com.prodyna.dialogue.testcontainers.business;

import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import com.prodyna.dialogue.testcontainers.presentation.NoteStatisticsDTO;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = NoteServiceContainerTest.Initializer.class)
public class NoteServiceContainerTest {

    @ClassRule
    public static GenericContainer mongo = new GenericContainer("mongo:3.6.9")
            .withCommand("mongod", "--port", "27017")
            .withExposedPorts(27017);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        String mongoUri = "mongodb://" + mongo.getContainerIpAddress() + ":" + mongo.getMappedPort(27017);

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.data.mongodb.uri:" + mongoUri)
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private NoteService noteService;

    @Before
    public void setUp() {
        Note note = new Note();
        note.setContent("Denk an die Milch");
        noteService.createNote(note);
    }

    @After
    public void tearDown() {
        mongoTemplate.remove(new Query(), "note");
    }

    @Test
    public void getAllNotes() {
        List<Note> notes = noteService.getAllNotes();
        Assert.assertNotNull(notes);
        Assert.assertEquals(1, notes.size());
    }

    @Test
    public void testNoteStatistics() {

        final NoteStatisticsDTO noteStatistics = noteService.getNoteStatistics();

        Assert.assertEquals(new Long(1), noteStatistics.getCount());
    }
}
