package parser;

import model.FeedModel;
import util.Log;
import util.XMLEventCharactersReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements parsing for item properties
 */
class RSSItemParser {
    private static Log log = new Log(RSSItemParser.class.getName(), System.out);

    /**
     * Iterate over XML and write item properties.
     * Finish on closing the item tag
     *
     * Note: This parser is waiting for eventReader to be pointed after item tag is opened
     *
     * @param eventReader XMLEventReader pointing right after item tag opening
     * @return map: ItemProperty -> value
     */
    Map<String, String> parse(XMLEventReader eventReader) throws IllegalAccessException {
        Map<String, String> model = new HashMap<>();
        boolean leaf = false;
        try {
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    if (leaf) {
                        throw new IllegalAccessException("Can't parse XML inside of RSS Item");
                    }
                    leaf = true;
                    String prefix = event.asStartElement().getName().getPrefix();
                    if (prefix.equals("atom")) {
                        // ignore atom fields for now
                        continue;
                    }
                    String localPart = event.asStartElement().getName().getLocalPart();
                    model.put(localPart.toLowerCase(), XMLEventCharactersReader.getCharacterData(eventReader));
                } else if (event.isEndElement()) {
                    leaf = false;
                    String localPart = event.asEndElement().getName().getLocalPart();
                    if (localPart.equals(FeedModel.FEED_ITEM)) {
                        break;
                    }
                }
            }
        } catch (XMLStreamException e) {
            log.error("Error occurred during parsing XML items and writing item properties: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return model;
    }
}
