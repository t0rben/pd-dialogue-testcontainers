package com.prodyna.dialogue.testcontainers.business;

import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import com.prodyna.dialogue.testcontainers.persistence.repository.NoteRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.util.Optional;

@SpringBootTest
@ContextConfiguration(initializers = NoteServiceRedisTest.Initializer.class)
@MockBean(classes = NoteRepository.class)
@RunWith(SpringRunner.class)
public class NoteServiceRedisTest {

    public static GenericContainer redis = new GenericContainer("redis:5.0.1-alpine").withExposedPorts(6379);

    @BeforeClass
    public static void startRedis() {

        redis.start();
    }

    @AfterClass
    public static void stopRedis() {

        redis.stop();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            TestPropertyValues.of(
                    "spring.redis.host:" + redis.getContainerIpAddress(),
                    "spring.redis.port:" + redis.getMappedPort(6379)
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void reset() {

        cacheManager.getCache("notes").clear();
    }

    @Test
    public void getNote() {

        Note note = new Note();
        note.setId("id");

        Mockito.when(noteRepository.findById("id")).thenReturn(Optional.of(note));

        noteService.getNote("id");

        Mockito.verify(noteRepository).findById("id");

        Assert.assertEquals(note, cacheManager.getCache("notes").get("id").get());

    }

    @Test
    public void createNote() {

        final Note note = new Note();
        note.setId("id");

        Mockito.when(noteRepository.save(note)).thenReturn(note);

        noteService.createNote(note);

        Mockito.verify(noteRepository).save(note);

    }

    @Test
    public void updateNote() {

        final Note note = new Note();
        note.setId("id");

        Mockito.when(noteRepository.save(note)).thenReturn(note);

        noteService.updateNote(note);

        Mockito.verify(noteRepository).save(note);

    }

    @Test
    public void deleteNote() {

        cacheManager.getCache("notes").put("id", new Note());

        noteService.deleteNote("id");

        Mockito.verify(noteRepository).deleteById(Mockito.anyString());

        Assert.assertEquals(null, cacheManager.getCache("notes").get("id"));

    }

    @Test
    public void getAllNotes() {

        noteService.getAllNotes();

        Mockito.verify(noteRepository).findAll();

    }
}