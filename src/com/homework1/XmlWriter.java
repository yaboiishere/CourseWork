package com.homework1;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

abstract class XmlWriter {
    private XmlWriter() {
    }

    static void writeCalendar(Calendar calendar, String filePath) throws XMLStreamException, IOException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found. Creating...");
            file.createNewFile();
        }
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileOutputStream(file));

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("calendar");
        writer.writeStartElement("bookings");
        calendar.getBookings().forEach((date, bookings) -> {
            bookings.forEach(booking -> {
                try {
                    writer.writeStartElement("booking");

                    writer.writeStartElement("date");
                    writer.writeCharacters(date.toString());
                    writer.writeEndElement();

                    writer.writeStartElement("name");
                    writer.writeCharacters(booking.getName());
                    writer.writeEndElement();

                    writer.writeStartElement("note");
                    writer.writeCharacters(booking.getNote());
                    writer.writeEndElement();

                    writer.writeStartElement("startTime");
                    writer.writeCharacters(booking.getStartTime().toString());
                    writer.writeEndElement();

                    writer.writeStartElement("endTime");
                    writer.writeCharacters(booking.getEndTime().toString());
                    writer.writeEndElement();

                    writer.writeEndElement();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            });
        });
        writer.writeEndElement();
        writer.writeStartElement("holidays");
        calendar.getHolidaySet().forEach(holiday -> {
            try {
                writer.writeStartElement("holiday");
                writer.writeCharacters(holiday.toString());
                writer.writeEndElement();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        });
        writer.writeEndElement();
        writer.writeEndElement();

        writer.writeEndDocument();
        writer.close();


    }
}
