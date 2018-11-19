package com.prodyna.dialogue.testcontainers.persistence.repository;

import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

}
