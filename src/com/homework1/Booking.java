package com.homework1;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;
    private String note;

    public Booking(LocalDateTime startTime, LocalDateTime endTime, String name, String note) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.note = note;
    }

    public Booking() {}

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", name='" + name + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
