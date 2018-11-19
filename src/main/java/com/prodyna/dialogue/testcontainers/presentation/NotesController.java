package com.prodyna.dialogue.testcontainers.presentation;

import com.prodyna.dialogue.testcontainers.business.NoteService;
import com.prodyna.dialogue.testcontainers.persistence.entity.Note;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class NotesController {

    private static final String ROOTPATH = "/notes";

    @Autowired
    private NoteService noteService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = ROOTPATH)
    public ResponseEntity<NoteListDTO> getAllNotes() {

        NoteListDTO noteListDTO = new NoteListDTO();

        final List<NoteOutgoingDTO> result = noteService.getAllNotes().stream().map(note -> modelMapper.map(note, NoteOutgoingDTO.class)).collect(Collectors.toList());

        noteListDTO.setNotes(result);

        return ResponseEntity.ok(noteListDTO);
    }

    @PostMapping(path = ROOTPATH)
    public ResponseEntity<NoteOutgoingDTO> createNote(@RequestBody NoteIncomingDTO noteIncomingDTO) {

        final Note note = modelMapper.map(noteIncomingDTO, Note.class);
        Note savedNode = noteService.createNote(note);

        NoteOutgoingDTO noteOutgoingDTO = modelMapper.map(savedNode, NoteOutgoingDTO.class);

        return ResponseEntity.ok(noteOutgoingDTO);
    }

    @GetMapping(path = ROOTPATH + "/{id}")
    public ResponseEntity<NoteOutgoingDTO> getNote(@PathVariable(name = "id") String id) {

        final Optional<Note> optionalNote = noteService.getNote(id);

        if (optionalNote.isPresent()) {

            return ResponseEntity.ok(modelMapper.map(optionalNote.get(), NoteOutgoingDTO.class));

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = ROOTPATH + "/{id}")
    public ResponseEntity<NoteOutgoingDTO> updateNote(@PathVariable(name = "id") String id, @RequestBody NoteIncomingDTO noteIncomingDTO) {

        final Optional<Note> optionalNote = noteService.getNote(id);

        if (optionalNote.isPresent()) {

            final Note note = optionalNote.get();
            note.setContent(noteIncomingDTO.getContent());
            return ResponseEntity.ok(modelMapper.map(noteService.updateNote(note), NoteOutgoingDTO.class));

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping(path = ROOTPATH + "/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable(name = "id") String id) {

        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

}
