package com.prodyna.dialogue.testcontainers.persistence.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Document
public class Note implements Persistable<String> {

    @Id
    private String id;

    private String content;

    @Version
    private Long version;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    @Override
    public String getId() {

        return this.id;
    }

    @Override
    public boolean isNew() {

        return id == null;
    }

    public String getContent() {

        return content;
    }

    public Long getVersion() {

        return version;
    }

    public Date getCreatedDate() {

        return createdDate;
    }

    public Date getLastModifiedDate() {

        return lastModifiedDate;
    }

    public void setContent(String content) {

        this.content = content;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return Objects.equals(getId(), note.getId()) &&
                Objects.equals(getContent(), note.getContent()) &&
                Objects.equals(getVersion(), note.getVersion()) &&
                Objects.equals(getCreatedDate(), note.getCreatedDate()) &&
                Objects.equals(getLastModifiedDate(), note.getLastModifiedDate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getContent(), getVersion(), getCreatedDate(), getLastModifiedDate());
    }

    @Override
    public String toString() {

        return "Note{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", version=" + version +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
