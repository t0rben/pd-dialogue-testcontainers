package com.prodyna.dialogue.testcontainers.presentation;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class NoteIncomingDTO {

    @NotEmpty
    private String content;

    public String getContent() {

        return content;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof NoteIncomingDTO)) return false;
        NoteIncomingDTO that = (NoteIncomingDTO) o;
        return Objects.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getContent());
    }

    @Override
    public String toString() {

        return "NoteIncomingDTO{" +
                "content='" + content + '\'' +
                '}';
    }
}
