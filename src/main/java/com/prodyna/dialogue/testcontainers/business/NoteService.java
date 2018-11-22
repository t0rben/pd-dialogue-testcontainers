package com.prodyna.dialogue.testcontainers.business;

import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import com.prodyna.dialogue.testcontainers.persistence.repository.NoteRepository;
import com.prodyna.dialogue.testcontainers.presentation.NoteStatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private MongoTemplate mongoTemplate;

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

    @CachePut(value = "notes", key = "#result.id")
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

    public NoteStatisticsDTO getNoteStatistics() {

        final TypedAggregation<Note> countAggregation = Aggregation.newAggregation(Note.class, Aggregation.count().as("count"));

        final AggregationResults<CountOutput> aggregate = mongoTemplate.aggregate(countAggregation, CountOutput.class);

        NoteStatisticsDTO noteStatisticsDTO = new NoteStatisticsDTO();
        noteStatisticsDTO.setCount(aggregate.getUniqueMappedResult().getCount());
        return  noteStatisticsDTO;
    }


}
