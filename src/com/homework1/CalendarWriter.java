package com.homework1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public abstract class CalendarWriter {
    public static boolean addBooking(Calendar calendar, LocalDate date, Booking booking) {
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

    public static boolean removeBooking(Calendar calendar, LocalDate date, Booking booking) {
        if (calendar.getBookings().containsKey(date)) return calendar.getBookings().get(date).remove(booking);
        return false;
    }

    public static boolean removeBooking(Calendar calendar, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDate localDate = startTime.toLocalDate();
        if (calendar.getBookings().containsKey(localDate))
            return calendar
                    .getBookings()
                    .get(localDate)
                    .removeIf(booking -> booking.getStartTime().equals(startTime) && booking.getEndTime().equals(endTime));

        return false;
    }

    public static boolean removeBooking(Calendar calendar, LocalDate date, LocalDateTime startTime, LocalDateTime endTime) {
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
}
