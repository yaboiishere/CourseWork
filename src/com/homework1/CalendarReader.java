package com.homework1;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CalendarReader {
    public static List<Booking> agenda(Calendar calendar, LocalDate date) {
        if (calendar.getBookings().containsKey(date))
            return calendar
                    .getBookings()
                    .get(date)
                    .stream()
                    .sorted(Comparator.comparing(Booking::getStartTime))
                    .collect(Collectors.toList());

        return null;
    }

    public static Set<Booking> findBookings(Calendar calendar, String searchString) {
        return calendar
                .getBookings()
                .values()
                .stream()
                .flatMap(Set::stream)
                .filter(booking -> booking.getName().contains(searchString))
                .collect(Collectors.toSet());
    }

    public static Map<DayOfWeek, Integer> weekdayAnalytics(Calendar calendar, LocalDate startDate, LocalDate endDate) {
        return calendar
                .getBookings()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAfter(startDate) && entry.getKey().isBefore(endDate))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getDayOfWeek(),
                        entry -> entry.getValue().size()
                ));
    }

    public static LocalDateTime findSlot(Calendar calendar, LocalDate fromDate, int hours) {
        LocalDateTime fromTime = fromDate.atStartOfDay().plusHours(8);
        LocalDateTime toTime = fromTime.plusHours(hours);
        while (true) {
            if (toTime.getHour() > 17) {
                fromTime = fromTime.plusHours(15);
                continue;
            }

            if (calendar.getBookings().containsKey(fromTime.toLocalDate())) {
                Set<Booking> bookings = calendar.getBookings().get(fromTime.toLocalDate());
                for (Booking booking : bookings) {
                    if (booking.getStartTime().isAfter(toTime)) return fromTime;
                    if (booking.getEndTime().isBefore(fromTime)) return fromTime;
                }
            }

            fromTime = fromTime.plusHours(1);
            toTime = fromTime.plusHours(hours);
        }

    }
}
