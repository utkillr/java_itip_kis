package util;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public class XMLEventCharactersReader {
    /**
     * Gets character data inside of xml tag via event reader
     * Waits for XMLEvent pointing on opening tag
     *
     * @param eventReader active XMLEventReader pointing on on event to be read
     * @return String representation of tag insides
     * @throws XMLStreamException in case of issues with event reading
     */
    public static String getCharacterData(XMLEventReader eventReader) throws XMLStreamException {
        String result = "";
        XMLEvent event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }
}
