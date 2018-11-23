package com.prodyna.dialogue.testcontainers.business;

import com.prodyna.dialogue.testcontainers.DockerImages;
import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import com.prodyna.dialogue.testcontainers.persistence.repository.NoteRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = NoteServiceContainerTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class NoteServiceContainerTest {

    @ClassRule
    public static GenericContainer mongo = new GenericContainer(DockerImages.MONGO)
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
    private NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void resetDependencies() {

        noteRepository.deleteAll();
        cacheManager.getCache("notes").clear();

    }

    private Note addRandnomNote() {

        Note note = new Note();
        note.setContent(UUID.randomUUID().toString());

        return noteRepository.save(note);
    }

    @Test
    public void getNote() {

        final Note note = addRandnomNote();

        Assert.assertEquals(note.getContent(), noteService.getNote(note.getId()).get().getContent());

        Assert.assertEquals(note.getContent(), ((Note) cacheManager.getCache("notes").get(note.getId()).get()).getContent());
    }

    @Test
    public void createNote() {

        Note note = new Note();
        note.setContent(UUID.randomUUID().toString());

        final Note savedNote = noteService.createNote(note);

        Assert.assertNotNull(savedNote.getId());

        Assert.assertEquals(note.getContent(), ((Note) cacheManager.getCache("notes").get(savedNote.getId()).get()).getContent());

    }

    @Test
    public void updateNote() {

        final Note note = addRandnomNote();

        note.setContent("blabla");
        final Note updatedNote = noteService.updateNote(note);

        Assert.assertNotNull(updatedNote.getId());
        Assert.assertEquals("blabla", updatedNote.getContent());

        Assert.assertEquals(note.getContent(), ((Note) cacheManager.getCache("notes").get(updatedNote.getId()).get()).getContent());

    }

    @Test
    public void deleteNote() {

        final Note note = addRandnomNote();

        noteService.deleteNote(note.getId());

        Assert.assertEquals(0L, noteRepository.count());
        Assert.assertNull(cacheManager.getCache("notes").get(note.getId()));
    }

    @Test
    public void getAllNotes() {

        addRandnomNote();
        addRandnomNote();
        addRandnomNote();

        Assert.assertEquals(3, noteService.getAllNotes().size());

    }

    @Test
    public void getNoteStatistics() {

        addRandnomNote();
        addRandnomNote();
        addRandnomNote();

        Assert.assertEquals(new Long(3), noteService.getNoteStatistics().getCount());

    }
}
