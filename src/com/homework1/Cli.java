package com.homework1;

import javax.management.InvalidAttributeValueException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

class Cli {
    private static Cli instance = null;
    private boolean running = true;
    private Calendar calendar = null;
    private String filePath = null;

    private Cli() {
    }

    static Cli getInstance() {
        if (Cli.instance == null) Cli.instance = new Cli();
        return Cli.instance;
    }

    void run() {
        while (running) {
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String[] commands = scanner.nextLine().split(" ");
            String command = commands[0];
            if (!command.equals("open") && calendar == null) {
                System.out.println("No calendar is open.");
                continue;
            }
            switch (command) {
                case "book":
                    if (commands.length != 6) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    LocalDate date = LocalDate.parse(commands[1]);
                    LocalTime startTime = LocalTime.parse(commands[2]);
                    LocalTime endTime = LocalTime.parse(commands[3]);
                    String name = commands[4];
                    String note = commands[5];

                    if (calendar.book(date, startTime, endTime, name, note))
                        System.out.println("Booking added successfully");
                    else
                        System.out.println("Booking could not be added");
                    break;
                case "unbook":
                    if (commands.length != 4) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    date = LocalDate.parse(commands[1]);
                    startTime = LocalTime.parse(commands[2]);
                    endTime = LocalTime.parse(commands[3]);

                    if (calendar.unbook(date, startTime, endTime))
                        System.out.println("Booking removed successfully");
                    else
                        System.out.println("Booking could not be removed");
                    break;
                case "agenda":
                    if (commands.length != 2) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    date = LocalDate.parse(commands[1]);
                    calendar.agenda(date);
                    break;
                case "change":
                    if (commands.length != 5) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    date = LocalDate.parse(commands[1]);
                    startTime = LocalTime.parse(commands[2]);
                    try {
                        calendar.change(date, startTime, BookingFields.valueOf(commands[3]), commands[4]);
                        System.out.println("Booking updated successfully");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid option");
                    } catch (InvalidAttributeValueException e) {
                        System.out.println("Invalid value");
                    }
                    break;

                case "find":
                    if (commands.length != 2) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    calendar.find(commands[1]);
                    break;
                case "holiday":
                    if (commands.length != 2) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    date = LocalDate.parse(commands[1]);
                    if (calendar.holiday(date))
                        System.out.println("Holiday added successfully");
                    else
                        System.out.println("Holiday could not be added");
                    break;
                case "busyDays":
                    if (commands.length != 3) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    date = LocalDate.parse(commands[1]);
                    LocalDate endDate = LocalDate.parse(commands[2]);

                    calendar.busyDays(date, endDate);
                    break;
                case "findSlot":
                    if (commands.length != 3) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    date = LocalDate.parse(commands[1]);
                    calendar.findSlot(date, Integer.parseInt(commands[2]));
                    break;
                case "open":
                    if (commands.length != 2) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    filePath = commands[1];
                    try {
                        calendar = XmlReader.readCalendar(filePath);
                    } catch (XMLStreamException e) {
                        System.out.println(e);
                        calendar = new Calendar();
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "close":
                    calendar = null;
                    System.out.println("Calendar closed");
                    break;
                case "save":
                    try {
                        XmlWriter.writeCalendar(calendar, filePath);
                    } catch (XMLStreamException e) {
                        System.out.println(e.getNestedException().toString());
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "saveas":
                    if (commands.length != 2) {
                        System.out.println("Invalid number of arguments");
                        continue;
                    }
                    try {
                        XmlWriter.writeCalendar(calendar, commands[1]);
                    } catch (XMLStreamException e) {
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "help":
                    System.out.println("The following commands are supported:");
                    System.out.println("open <file>: opens <file>");
                    System.out.println("close: closes the current calendar to file");
                    System.out.println("save: saves the current calendar to file");
                    System.out.println("saveas <file>: saves the current calendar to <file>");
                    System.out.println("help: shows this help");
                    System.out.println("exit: exits the program");
                    System.out.println();
                    System.out.println("book <date> <startTime> <endTime> <name> <note>: book a slot");
                    System.out.println("unbook <date> <startTime> <endTime>: unbook a slot");
                    System.out.println("agenda <date>: shows the agenda for <date>");
                    System.out.println("change <startTime> <endTime> <field> <value>: change a booking");
                    System.out.println("find <field> <value>: find a booking");
                    System.out.println("holiday <date>: add a holiday");
                    System.out.println("busyDays <startDate> <endDate>: show busy days");
                    System.out.println("findSlot <date> <duration>: find a slot");
                case "exit":
                    running = false;
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}