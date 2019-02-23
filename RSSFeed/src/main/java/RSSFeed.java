import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RSSFeed {
    /**
     * This class represents RSS feed (not Atom one).
     * Includes default fields which are unable to be removed
     */

    private RSSConfiguration configuration;

    Map<String, String> metaBody;
    List<RSSItem> items;

    public RSSFeed(RSSConfiguration configuration, FeedModel model) {
        this.metaBody = new HashMap<>();
        this.configuration = configuration;
        model.metaSource.forEach((key, value) -> {
            if (configuration.getItemFields().contains(key)) {
                metaBody.put(key, value);
            }
        });
        this.items = new ArrayList<>();
        for (Map<String, String> item : model.itemSources) {
            items.add(new RSSItem(configuration, item));
        }
    }

    public static FeedModel parse(InputStream in) {
        FeedModel model = new FeedModel();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    if (localPart.equals(FeedModel.FEED_ITEM)) {
                        StringBuilder builder = new StringBuilder();
                        event = eventReader.nextEvent();
                        while (! (event.isEndElement() &&
                                  event.asEndElement().getName().getLocalPart().equals(FeedModel.FEED_ITEM)
                        )) {
                            builder.append(event.toString());
                            event = eventReader.nextEvent();
                        }
                        model.itemSources.add(RSSItem.parse(
                                new ByteArrayInputStream(
                                        builder.toString().getBytes(StandardCharsets.UTF_8)
                                )
                        ));
                    }
                    // localPart - key. getCharacterData(event, eventReader) - data.
                    // model.metaSource.put(localPart, FeedModel.getCharacterData(event, eventReader));
                }
                else if (event.isEndElement()) {
                    eventReader.nextEvent();
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return model;
    }
}
