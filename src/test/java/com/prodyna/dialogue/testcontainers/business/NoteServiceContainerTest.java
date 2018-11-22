package com.prodyna.dialogue.testcontainers.business;

import com.prodyna.dialogue.testcontainers.AbstractDependencies;
import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import com.prodyna.dialogue.testcontainers.presentation.NoteStatisticsDTO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NoteServiceContainerTest extends AbstractDependencies {

    // Für den Dialog würde ich die Container-Initialisierung in der gleichen Klasse machen.
    // Sonst wird es während dem Vortrag schnell unübersichtlich

    @Autowired
    private NoteService noteService;

    @Before
    public void setUp() {
        Note note = new Note();
        note.setContent("Test");
        noteService.createNote(note);
    }

    @After
    public void tearDown() {
        // cleanups and resets
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
