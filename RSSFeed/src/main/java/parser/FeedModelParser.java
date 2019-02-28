package parser;

import lombok.extern.slf4j.Slf4j;
import model.FeedModel;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

/**
 * This class implements global parsing to throw away all the data besides "channel" tag
 */
@Slf4j
public class FeedModelParser {
    /**
     * Iterate over XML until it's "channel" tag. Then call parser for RSSChannel
     *
     * @param in InputStream with XML
     * @return model which is returned from RSSChannelParser
     */
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
                        model = new RSSChannelParser().parse(eventReader);
                    }
                }
            }
        } catch (XMLStreamException e) {
            log.error("[ERROR] Error occurred during parsing rss: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return model;
    }
}
