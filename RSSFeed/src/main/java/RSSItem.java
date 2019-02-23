import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RSSItem {
    /**
     * This class represents Item of RSS feed (not Atom one).
     * Includes default fields which are unable to be removed
     */

    private RSSConfiguration configuration;

    Map<String, String> body;

    public RSSItem(RSSConfiguration configuration, Map<String, String> source) {
        this.body = new HashMap<>();
        this.configuration = configuration;
        source.forEach((key, value) -> {
            if (configuration.getItemFields().contains(key)) {
                body.put(key, value);
            }
        });
    }

    public static Map<String, String> parse(InputStream in) {
        Map<String, String> model = new HashMap<>();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    model.put(localPart, FeedModel.getCharacterData(event, eventReader));
                }
                else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equals(FeedModel.FEED_ITEM)) {
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
