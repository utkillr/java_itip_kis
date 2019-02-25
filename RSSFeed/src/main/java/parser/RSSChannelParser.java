package parser;

import model.FeedModel;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

class RSSChannelParser {
    // Waiting for <channel> ... </channel> XML
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
            throw new RuntimeException(e);
        }
        return model;
    }
}
