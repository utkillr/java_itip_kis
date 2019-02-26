package parser;

import model.FeedModel;
import util.Log;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * This class implements parsing for channel properties and items
 */
class RSSChannelParser {
    private static Log log = new Log(RSSChannelParser.class.getName(), System.out);


    /**
     * Iterate over XML and write channel properties until it's "item" tag.
     * Then call parser for RSSItem (in loop)
     * Finish on closing the channel tag
     *
     * Note: This parser is waiting for eventReader to be pointed after channel tag is opened
     *
     * @param eventReader XMLEventReader pointing right after channel tag opening
     * @return parsed FeedModel
     */
    FeedModel parse(XMLEventReader eventReader) {
        FeedModel model = new FeedModel();
        try {
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String prefix = event.asStartElement().getName().getPrefix();
                    if (prefix.equals("atom")) {
                        // ignore atom fields for now
                        continue;
                    }
                    String localPart = event.asStartElement().getName().getLocalPart();
                    // in case of it is <item>
                    if (localPart.equals(FeedModel.FEED_ITEM)) {
                        // Move inside of <item>
                        eventReader.nextEvent();
                        model.itemSources.add(new RSSItemParser().parse(eventReader));
                    } else {
                        model.metaSource.put(localPart.toLowerCase(), FeedModel.getCharacterData(eventReader));
                    }
                } else if (event.isEndElement()){
                    String localPart = event.asEndElement().getName().getLocalPart();
                    if (localPart.equals(FeedModel.FEED_CHANNEL)) {
                        break;
                    }
                }
            }
        } catch (XMLStreamException e) {
            log.error("Error occurred during parsing XML items and writing channel properties: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return model;
    }
}
