package com.homework1;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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

    static LocalDateTime findSlot(Calendar calendar, LocalDate fromDate, int hours) {
        LocalDateTime fromTime = fromDate.atStartOfDay().plusHours(8);
        LocalDateTime toTime = fromTime.plusHours(hours);


        while (true) if (calendar.getBookings().containsKey(fromTime.toLocalDate())) {
            List<Booking> bookings = calendar
                    .getBookings()
                    .get(fromTime.toLocalDate())
                    .stream()
                    .sorted(Comparator.comparing(Booking::getStartTime))
                    .toList();
            if (toTime.getHour() > 17) {
                fromTime = fromTime.plusDays(1).withHour(8);
                toTime = fromTime.plusHours(hours);
            }
            for (Booking booking : bookings) {
                LocalTime endTime = booking.getEndTime();
                if (toTime.toLocalTime().isBefore(booking.getStartTime()) || toTime.toLocalTime().equals(booking.getStartTime())
                        || fromTime.toLocalTime().isAfter(endTime) || fromTime.toLocalTime().equals(endTime))
                    return fromTime;

            }
            fromTime = fromTime.plusHours(1);
            toTime = fromTime.plusHours(hours);
        } else return fromTime;

    }

    static LocalDateTime findSlotInCalendars(List<Calendar> calendars, LocalDate fromDate, int hours) {
        List<LocalDateTime> times = null;
        LocalDateTime currentTime = fromDate.atStartOfDay().plusHours(8);
        while (true) {
            int index = 0;
            for (Calendar calendar : calendars) {
                LocalDateTime slot = findSlot(calendar, LocalDate.from(currentTime), hours);
                if (times == null) times = new ArrayList<>();
                times.add(index, slot);
                index++;
            }
            if (checkIfAllElementsEquals(times)) return times.get(0);
            currentTime = currentTime.plusHours(1);
            if (currentTime.getHour() > 17) currentTime = currentTime.plusDays(1).withHour(8);
            times = null;
        }
    }

    private static boolean checkIfAllElementsEquals(List<LocalDateTime> times) {
        return times != null && times.stream().allMatch(time -> time.equals(times.get(0)));
    }
}