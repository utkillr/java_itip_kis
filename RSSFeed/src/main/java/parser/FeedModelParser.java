package parser;

import model.FeedModel;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

public class FeedModelParser {
    public FeedModel parse(InputStream in) {
        FeedModel model = null;
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    if (localPart.equals(FeedModel.FEED_CHANNEL)) {
                        // Move inside of <channel>
                        eventReader.nextEvent();
                        model = new RSSFeedParser().parse(eventReader);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return model;
    }
}
