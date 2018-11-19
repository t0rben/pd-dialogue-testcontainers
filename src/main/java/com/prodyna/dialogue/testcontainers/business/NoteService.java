package com.prodyna.dialogue.testcontainers.business;

import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import com.prodyna.dialogue.testcontainers.persistence.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Cacheable(value = "notes", key = "#id")
    public Optional<Note> getNote(String id) {

        return noteRepository.findById(id);
    }

    @CachePut(value = "notes", key = "#result.id")
    public Note createNote(Note note) {

        return noteRepository.save(note);
    }

    @CachePut(value = "notes", key = "#note.id")
    public Note updateNote(Note note) {

        return noteRepository.save(note);
    }

    @CacheEvict(value = "notes", key = "#id")
    public void deleteNote(String id) {

        noteRepository.deleteById(id);
    }

    public List<Note> getAllNotes() {

        return noteRepository.findAll();
    }

}
