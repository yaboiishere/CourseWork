package com.homework1;

import java.time.LocalTime;

public class Booking {
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;
    private String note;

    Booking(LocalTime startTime, LocalTime endTime, String name, String note) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.note = note;
    }

    Booking() {
    }

    LocalTime getStartTime() {
        return startTime;
    }

    LocalTime getEndTime() {
        return endTime;
    }

    public String getName() {
        return name;
    }

    String getNote() {
        return note;
    }

    void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    void setName(String name) {
        this.name = name;
    }

    void setNote(String note) {
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
