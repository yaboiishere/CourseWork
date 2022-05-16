package com.homework1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.homework1.CalendarWriter.removeBooking;

public class Calendar {
    private final Set<LocalDate> holidaySet = new HashSet<>();
    private Map<LocalDate, Set<Booking>> bookings;

    public Calendar() {
    }

    public Set<LocalDate> getHolidaySet() {
        return holidaySet;
    }

    public Map<LocalDate, Set<Booking>> getBookings() {
        return bookings;
    }

    public boolean book(LocalDate date, LocalDateTime startTime, LocalDateTime endTime, String name, String note) {
        return CalendarWriter.addBooking(this, date, new Booking(startTime, endTime, name, note));
    }

    public boolean unbook(LocalDate date, LocalDateTime startTime, LocalDateTime endTime) {
        return CalendarWriter.removeBooking(this, date, startTime, endTime);
    }

    public void agenda(LocalDate date) {
        System.out.println(CalendarReader.agenda(this, date));
    }

    public void change(){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void find(String searchString){
        System.out.println(CalendarReader.findBookings(this, searchString));
    }

    public boolean holiday(LocalDate date){
        return holidaySet.add(date);
    }

    public void busyDays(LocalDate startDate, LocalDate endDate){
        System.out.println(CalendarReader.weekdayAnalytics(this, startDate, endDate));
    }

    public void findslot(LocalDate startDate, Integer hours){
        System.out.println(CalendarReader.findSlot(this, startDate, hours));
    }
}
