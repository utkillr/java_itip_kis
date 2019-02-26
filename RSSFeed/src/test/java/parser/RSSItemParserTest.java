package parser;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import util.XMLEventCharactersReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RSSItemParserTest {

    @Test(expected = IllegalAccessException.class)
    @DisplayName("Test for regular RSSItem parsing with wrong entry point")
    public void parseValidRSSItemTestWrongEntryPoint() throws XMLStreamException, IllegalAccessException {
        String file = "parser" + File.separator + "regularItem.xml";
        InputStream inputStream = ParserTest.class.getClassLoader().getResourceAsStream(file);

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
        // Skip first <xml> event
        eventReader.nextEvent();

        new RSSItemParser().parse(eventReader);
        eventReader.close();
    }

    @Test
    @DisplayName("Test for regular RSSItem parsing")
    public void parseValidRSSItemTest() throws XMLStreamException, IllegalAccessException {
        String file = "parser" + File.separator + "regularItem.xml";
        InputStream inputStream = ParserTest.class.getClassLoader().getResourceAsStream(file);

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
        // Skip first <xml> event
        eventReader.nextEvent();
        eventReader.nextEvent();

        Map<String, String> expectedModel = new HashMap<>();
        expectedModel.put("title", "NAME");
        expectedModel.put("description", "DESCRIPTION");
        expectedModel.put("pubdate", "DATE");
        Map<String, String> model = new RSSItemParser().parse(eventReader);

        assertEquals(expectedModel.size(), model.size());
        for (String key : expectedModel.keySet()) {
            assertEquals(expectedModel.get(key), model.get(key));
        }

        eventReader.close();
    }

    @Test(expected = IllegalAccessException.class)
    @DisplayName("Test for Invalid RSSItem parsing")
    public void parseInvalidRSSItemTest() throws XMLStreamException, IllegalAccessException {
        String file = "parser" + File.separator + "invalidItem.xml";
        InputStream inputStream = ParserTest.class.getClassLoader().getResourceAsStream(file);

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
        // Skip first <xml> event
        eventReader.nextEvent();

        new RSSItemParser().parse(eventReader);
        eventReader.close();
    }

    @Test
    @DisplayName("Test for Empty RSSItem parsing")
    public void parseEmptyRSSItemTest() throws XMLStreamException, IllegalAccessException {
        String file = "parser" + File.separator + "emptyItem.xml";
        InputStream inputStream = ParserTest.class.getClassLoader().getResourceAsStream(file);

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
        // Skip first <xml> event
        eventReader.nextEvent();

        eventReader.nextEvent();

        Map<String, String> model = new RSSItemParser().parse(eventReader);
        assertTrue(model.isEmpty());
        eventReader.close();
    }
}
