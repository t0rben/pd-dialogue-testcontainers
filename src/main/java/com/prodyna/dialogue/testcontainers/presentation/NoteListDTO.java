package com.prodyna.dialogue.testcontainers.presentation;

import java.util.List;
import java.util.Objects;

public class NoteListDTO {

    private List<NoteOutgoingDTO> notes;

    public List<NoteOutgoingDTO> getNotes() {

        return notes;
    }

    public void setNotes(List<NoteOutgoingDTO> notes) {

        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof NoteListDTO)) return false;
        NoteListDTO that = (NoteListDTO) o;
        return Objects.equals(getNotes(), that.getNotes());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNotes());
    }

    @Override
    public String toString() {

        return "NoteListDTO{" +
                "notes=" + notes +
                '}';
    }
}
