package com.homework1;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract class XmlReader {
    private XmlReader() {
    }

    static Calendar readCalendar(String filePath) throws XMLStreamException, IOException {
        XMLInputFactory input = XMLInputFactory.newInstance();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found. Creating...");
            if (!file.createNewFile()) {
                throw new IOException("Could not create file.");
            }

            return new Calendar();
        }

        XMLEventReader reader = input.createXMLEventReader(new FileInputStream(file));

        Map<LocalDate, Set<Booking>> bookings = new HashMap<>();
        Set<LocalDate> holidaySet = new HashSet<>();
        LocalDate date = null;
        Booking booking = new Booking();
        while (reader.hasNext()) while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "booking" -> booking = new Booking();
                    case "name" -> {
                        nextEvent = reader.nextEvent();
                        booking.setName(nextEvent.asCharacters().getData());
                    }
                    case "note" -> {
                        nextEvent = reader.nextEvent();
                        booking.setNote(nextEvent.asCharacters().getData());
                    }
                    case "startTime" -> {
                        nextEvent = reader.nextEvent();
                        booking.setStartTime(LocalTime.parse(nextEvent.asCharacters().getData()));
                    }
                    case "endTime" -> {
                        nextEvent = reader.nextEvent();
                        booking.setEndTime(LocalTime.parse(nextEvent.asCharacters().getData()));
                    }
                    case "date" -> {
                        nextEvent = reader.nextEvent();
                        date = LocalDate.parse(nextEvent.asCharacters().getData());
                    }
                    case "holiday" -> {
                        nextEvent = reader.nextEvent();
                        holidaySet.add(LocalDate.parse(nextEvent.asCharacters().getData()));
                    }
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("booking")) {
                    Set<Booking> bookingSet;
                    if (bookings.containsKey(date)) {
                        bookingSet = new HashSet<>(bookings.get(date));
                        bookingSet.add(booking);
                    } else bookingSet = Set.of(booking);
                    bookings.put(date, bookingSet);
                    date = null;
                }
            }
        }

        reader.close();
        return new Calendar(holidaySet, bookings);
    }
}
