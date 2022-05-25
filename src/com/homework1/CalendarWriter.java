package com.homework1;

import javax.management.InvalidAttributeValueException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
        if (value.equals("")) throw new InvalidAttributeValueException("Value cannot be empty");
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

    static Calendar merge(List<Calendar> calendars) {
        if (calendars.size() < 2) return null;
        Calendar rootCalendar = calendars.get(0);
        Map<LocalDate, Set<Booking>> rootBookings = rootCalendar.getBookings();
        for (int i = 1; i < calendars.size(); i++) {
            Map<LocalDate, Set<Booking>> bookings = calendars.get(i).getBookings();

            bookings.keySet().forEach((date) -> {
                if (rootBookings.containsKey(date)) {
                    Set<Booking> rootBookingSet = rootBookings.get(date);
                    Set<Booking> bookingSet = bookings.get(date);
                    List<Booking> removeFromRoot = new java.util.ArrayList<>();
                    List<Booking> addToRoot = new java.util.ArrayList<>();
                    for (Booking rootBooking : rootBookingSet)
                        for (Booking booking : bookingSet)
                            if (booking.getStartTime().isAfter(rootBooking.getStartTime()) && booking.getStartTime().isBefore(rootBooking.getEndTime())
                                    || booking.getEndTime().isAfter(rootBooking.getStartTime()) && booking.getEndTime().isBefore(rootBooking.getEndTime())
                                    || booking.getStartTime().isBefore(rootBooking.getStartTime()) && booking.getEndTime().isAfter(rootBooking.getEndTime())
                                    || booking.getStartTime().isAfter(rootBooking.getStartTime()) && booking.getEndTime().isBefore(rootBooking.getEndTime())) {
                                System.out.println("Incoming booking\n" + booking + "\noverlaps with current booking\n" + rootBooking);
                                System.out.println("Pick booking to keep! 1: for incoming, 2: for current");
                                while (true) {
                                    Scanner scanner = new Scanner(System.in);
                                    int choice = scanner.nextInt();
                                    boolean success = false;
                                    switch (choice) {
                                        case 1 -> {
                                            removeFromRoot.add(rootBooking);
                                            addToRoot.add(booking);
                                            success = true;
                                        }
                                        case 2 -> success = true;
                                        default -> System.out.println("Invalid choice!");
                                    }
                                    if (success) break;
                                }
                            }
                    removeFromRoot.forEach(rootBookingSet::remove);
                    rootBookingSet.addAll(addToRoot);
                    removeFromRoot.removeIf((booking) -> true);
                    addToRoot.removeIf((booking) -> true);
                } else rootBookings.put(date, bookings.get(date));
            });
            rootCalendar.getHolidaySet().addAll(calendars.get(i).getHolidaySet());
            calendars.clear();
        }

        return rootCalendar;
    }
}
