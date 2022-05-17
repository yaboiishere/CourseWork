package com.homework1;

import javax.management.InvalidAttributeValueException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public abstract class CalendarWriter {
    static boolean addBooking(Calendar calendar, LocalDate date, Booking booking) {
        if (calendar.getHolidaySet().contains(date)) return false;
        if (calendar.getBookings().containsKey(date)) calendar.getBookings().get(date).add(booking);
        else {
            Set<Booking> bookingSet = new java.util.HashSet<>();
            bookingSet.add(booking);
            calendar.getBookings().put(date, bookingSet);
            return true;
        }

        return false;
    }

    static boolean removeBooking(Calendar calendar, LocalDate date, Booking booking) {
        if (calendar.getBookings().containsKey(date)) return calendar.getBookings().get(date).remove(booking);
        return false;
    }

    static boolean removeBooking(Calendar calendar, LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (calendar.getBookings().containsKey(date))
            return calendar
                    .getBookings()
                    .get(date)
                    .removeIf(booking -> booking.getStartTime().equals(startTime) && booking.getEndTime().equals(endTime));

        return false;
    }

    public static void setHoliday(Calendar calendar, LocalDate date) {
        calendar.getHolidaySet().add(date);
    }

    static void updateBooking(Calendar calendar, LocalDate date, LocalTime startTime, BookingFields option, String value) throws InvalidAttributeValueException {
        if (value == null) throw new InvalidAttributeValueException("Value cannot be null");
        if (value == "") throw new InvalidAttributeValueException("Value cannot be empty");
        if (calendar.getBookings().containsKey(date)) for (Booking booking : calendar.getBookings().get(date))
            if (booking.getStartTime().equals(startTime)) switch (option) {
                case name -> booking.setName(value);
                case note -> booking.setNote(value);
                case startTime -> {
                    if (LocalTime.parse(value).isAfter(booking.getEndTime()))
                        throw new InvalidAttributeValueException("Start time cannot be after end time");
                    booking.setStartTime(LocalTime.parse(value));
                }
                case endTime -> {
                    if (LocalTime.parse(value).isBefore(booking.getStartTime()))
                        throw new InvalidAttributeValueException("End time must be after start time");
                    booking.setEndTime(LocalTime.parse(value));
                }
                default -> {
                }
            }
    }
}
