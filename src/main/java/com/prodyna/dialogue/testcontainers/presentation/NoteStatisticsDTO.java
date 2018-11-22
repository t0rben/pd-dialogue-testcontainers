package com.prodyna.dialogue.testcontainers.presentation;

public class NoteStatisticsDTO {

    private Long count;

    public Long getCount() {

        return count;
    }

    public void setCount(Long count) {

        this.count = count;
    }

    @Override
    public String toString() {

        return "NoteStatisticsDTO{" +
                "count=" + count +
                '}';
    }
}
