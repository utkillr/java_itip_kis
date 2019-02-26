package util;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import parser.ParserTest;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.*;

public class XMLEventCharactersReaderTest {

    @Test
    @DisplayName("Test reading of single event")
    public void singleEventReadTest() throws XMLStreamException {
        String file = "util" + File.separator + "singleEvent.xml";
        InputStream inputStream = ParserTest.class.getClassLoader().getResourceAsStream(file);

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
        // Skip first <xml> event
        eventReader.nextEvent();

        eventReader.nextEvent();
        assertEquals("NASA", XMLEventCharactersReader.getCharacterData(eventReader));

        eventReader.close();
    }

    @Test
    @DisplayName("Test reading of single event")
    public void openingEventReadTest() throws XMLStreamException {
        String file = "util" + File.separator + "singleEvent.xml";
        InputStream inputStream = ParserTest.class.getClassLoader().getResourceAsStream(file);

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
        // Skip first <xml> event
        eventReader.nextEvent();
        assertEquals("", XMLEventCharactersReader.getCharacterData(eventReader));

        eventReader.close();
    }

    @Test
    @DisplayName("Test reading of single event")
    public void closingEventReadTest() throws XMLStreamException {
        String file = "util" + File.separator + "singleEvent.xml";
        InputStream inputStream = ParserTest.class.getClassLoader().getResourceAsStream(file);

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
        // Skip first <xml> event
        eventReader.nextEvent();

        eventReader.nextEvent();
        eventReader.nextEvent();
        assertEquals("", XMLEventCharactersReader.getCharacterData(eventReader));

        eventReader.close();
    }
}