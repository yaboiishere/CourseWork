package com.homework1;

import javax.management.InvalidAttributeValueException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

class Calendar {
    private final Set<LocalDate> holidaySet = new HashSet<>();
    private final Map<LocalDate, Set<Booking>> bookings = new HashMap<>();

    Calendar() {
    }

    Calendar(Set<LocalDate> holidaySet, Map<LocalDate, Set<Booking>> bookings) {
        this.holidaySet.addAll(holidaySet);
        this.bookings.putAll(bookings);
    }

    Set<LocalDate> getHolidaySet() {
        return holidaySet;
    }

    Map<LocalDate, Set<Booking>> getBookings() {
        return bookings;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "holidaySet=" + holidaySet +
                ", bookings=" + bookings +
                '}';
    }

    boolean book(LocalDate date, LocalTime startTime, LocalTime endTime, String name, String note) {
        return CalendarWriter.addBooking(this, date, new Booking(startTime, endTime, name, note));
    }

    boolean unbook(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return CalendarWriter.removeBooking(this, date, startTime, endTime);
    }

    void agenda(LocalDate date) {
        System.out.println(CalendarReader.agenda(this, date));
    }

    void change(LocalDate date, LocalTime startTime, BookingFields option, String value) throws InvalidAttributeValueException {
        CalendarWriter.updateBooking(this, date, startTime, option, value);
    }

    void find(String searchString) {
        System.out.println(CalendarReader.findBookings(this, searchString));
    }

    boolean holiday(LocalDate date) {
        return holidaySet.add(date);
    }

    void busyDays(LocalDate startDate, LocalDate endDate) {
        System.out.println(CalendarReader.weekdayAnalytics(this, startDate, endDate));
    }

    void findSlot(LocalDate startDate, Integer hours) {
        try {
            System.out.println(CalendarReader.findSlot(this, startDate, hours));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void findSlotWith(LocalDate startDate, Integer hours, String[] filePaths) throws XMLStreamException, IOException {
        List<Calendar> calendars = new ArrayList<>();
        calendars.add(this);
        for (String filePath : filePaths) calendars.add(XmlReader.readCalendar(filePath));
        System.out.println(CalendarReader.findSlotInCalendars(calendars, startDate, hours));
    }

    void merge(String[] filePaths) throws XMLStreamException, IOException {
        List<Calendar> calendars = new ArrayList<>();
        calendars.add(this);
        for (String filePath : filePaths) calendars.add(XmlReader.readCalendar(filePath));
        if (CalendarWriter.merge(calendars) != null) System.out.println("Calendars merged successfully");
        else System.out.println("Calendars could not be merged");
    }
}
