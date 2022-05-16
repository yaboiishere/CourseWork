package com.homework1;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class XmlReader {
    private XMLEventReader reader;

    public XmlReader(String filePath) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        reader = xmlInputFactory.createXMLEventReader(new FileInputStream(filePath));
    }

    public XMLEventReader getReader() {
        return reader;
    }

    public void close() throws XMLStreamException {
        reader.close();
        reader = null;
    }

    public Map<LocalDate, Set<Booking>> readFile() throws XMLStreamException {
        Map<LocalDate, Set<Booking>> bookings = new HashMap<>();
        while(reader.hasNext()) {
            while (reader.hasNext()) {
                Booking booking = new Booking();
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
                            booking.setStartTime(LocalDateTime.parse(nextEvent.asCharacters().getData()));
                        }
                        case "endTime" -> {
                            nextEvent = reader.nextEvent();
                            booking.setEndTime(LocalDateTime.parse(nextEvent.asCharacters().getData()));
                        }
                    }
                }
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("booking")) {
                        if(bookings.containsKey(booking.getStartTime().toLocalDate())) {
                            bookings.get(booking.getStartTime().toLocalDate()).add(booking);
                        } else {
                            Set<Booking> bookingSet = Set.of(booking);
                            bookings.put(booking.getStartTime().toLocalDate(), bookingSet);
                        }
                    }
                }
            }
        }
        return bookings;
    }
}
