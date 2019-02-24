package parser;

import model.FeedModel;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.HashMap;
import java.util.Map;

public class RSSItemParser {
    public Map<String, String> parse(XMLEventReader eventReader) {
        Map<String, String> model = new HashMap<>();
        try {
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    model.put(localPart, FeedModel.getCharacterData(event, eventReader));
                } else if (event.isEndElement()) {
                    String localPart = event.asEndElement().getName().getLocalPart();
                    if (localPart.equals(FeedModel.FEED_ITEM)) {
                        break;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return model;
    }
}
