package model;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements our model of RSS Feed which is actually
 * Channel description source (metaSource) + item description sources (itemSources)
 */
public class FeedModel {

    public static String FEED_ITEM = "item";
    public static String FEED_CHANNEL ="channel";

    public Map<String, String> metaSource;
    public List<Map<String, String>> itemSources;

    public FeedModel() {
        metaSource = new HashMap<>();
        itemSources = new ArrayList<>();
    }

    /**
     * Gets character data inside of xml tag via event reader
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
