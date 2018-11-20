package com.prodyna.dialogue.testcontainers.persistence.repository;

import com.prodyna.dialogue.testcontainers.AbstractDependencies;
import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NoteRepositoryTest extends AbstractDependencies {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    public void test() {

        for (int i = 0; i < 1000; i++) {
            Note note = new Note();
            note.setContent(UUID.randomUUID().toString());
            noteRepository.save(note);
        }

        Assert.assertEquals(1000, noteRepository.count());

    }
}