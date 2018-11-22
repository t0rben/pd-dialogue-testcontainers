package com.prodyna.dialogue.testcontainers.business;

import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import com.prodyna.dialogue.testcontainers.persistence.repository.NoteRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@MockBean(classes = {NoteRepository.class, MongoTemplate.class})
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ActiveProfiles("test")
public class NoteServiceMockTest {

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
        Optional<Note> optionalNote = noteService.getNote("id");
        Mockito.verify(noteRepository).findById("id");
        Assert.assertTrue(optionalNote.isPresent());
    }

    @Test
    public void getNoteCacheTest() {

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

    @Test
    public void getStatistics() {

    }
}