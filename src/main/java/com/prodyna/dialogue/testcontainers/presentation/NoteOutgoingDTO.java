package com.prodyna.dialogue.testcontainers.presentation;

import java.util.Date;
import java.util.Objects;

public class NoteOutgoingDTO {

    private String id;

    private String content;

    private Long version;

    private Date createdDate;

    private Date lastModifiedDate;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public Long getVersion() {

        return version;
    }

    public void setVersion(Long version) {

        this.version = version;
    }

    public Date getCreatedDate() {

        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {

        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {

        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {

        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof NoteOutgoingDTO)) return false;
        NoteOutgoingDTO that = (NoteOutgoingDTO) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getContent(), that.getContent()) &&
                Objects.equals(getVersion(), that.getVersion()) &&
                Objects.equals(getCreatedDate(), that.getCreatedDate()) &&
                Objects.equals(getLastModifiedDate(), that.getLastModifiedDate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getContent(), getVersion(), getCreatedDate(), getLastModifiedDate());
    }

    @Override
    public String toString() {

        return "NoteOutgoingDTO{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", version=" + version +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}