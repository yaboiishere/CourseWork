package com.homework1;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

abstract class CalendarReader {
    static List<Booking> agenda(Calendar calendar, LocalDate date) {
        if (calendar.getBookings().containsKey(date))
            return calendar.getBookings().get(date).stream().sorted(Comparator.comparing(Booking::getStartTime)).collect(Collectors.toList());

        return null;
    }

    static Set<Booking> findBookings(Calendar calendar, String searchString) {
        return calendar.getBookings().values().stream().flatMap(Set::stream).filter(booking -> booking.getName().contains(searchString)).collect(Collectors.toSet());
    }

    static Map<DayOfWeek, Integer> weekdayAnalytics(Calendar calendar, LocalDate startDate, LocalDate endDate) {
        return calendar.getBookings().entrySet().stream().filter(entry -> entry.getKey().isAfter(startDate) && entry.getKey().isBefore(endDate)).collect(Collectors.toMap(entry -> entry.getKey().getDayOfWeek(), entry -> entry.getValue().size()));
    }

    static LocalTime findSlot(Calendar calendar, LocalDate fromDate, int hours) throws Exception {
        LocalTime fromTime = fromDate.atStartOfDay().plusHours(8).toLocalTime();
        LocalTime toTime = fromTime.plusHours(hours);
        while (true) {
            if (toTime.getHour() > 17) break;
            if (calendar.getBookings().containsKey(fromDate)) {
                List<Booking> bookings = calendar.getBookings().get(fromDate).stream().sorted(Comparator.comparing(Booking::getStartTime)).collect(Collectors.toList());
                System.out.println(bookings);

                for (Booking booking : bookings) {
                    if (toTime.isBefore(booking.getStartTime()) || toTime.equals(booking.getStartTime())) {
                        return fromTime;
                    }
                    if (fromTime.isAfter(booking.getEndTime()) || fromTime.equals(booking.getEndTime())) {
                        return fromTime;
                    }
                    fromTime = booking.getEndTime();
                    toTime = fromTime.plusHours(hours);
                }
            }
        }
        throw new Exception("No slot found");
    }
}