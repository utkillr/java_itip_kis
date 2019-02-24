package model;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedModel {

    public static String FEED_ITEM = "item";
    public static String FEED_CHANNEL ="channel";

    public Map<String, String> metaSource;
    public List<Map<String, String>> itemSources;

    public FeedModel() {
        metaSource = new HashMap<>();
        itemSources = new ArrayList<>();
    }

    public static String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }
}
