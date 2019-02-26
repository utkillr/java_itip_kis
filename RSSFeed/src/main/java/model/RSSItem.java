package model;

import config.RSSConfiguration;
import util.PubDateParser;

import java.io.InvalidObjectException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents RSS Channel (Atom syntax is ignored for now).
 */
public class RSSItem {

    private Map<String, String> body;
    private Date latestPubDate;

    /**
     * Get properties of RSS Item
     *
     * @return map: Configured property -> value
     */
    public Map<String, String> getBody() {
        return body;
    }

    /**
     * Get parsed pubDate, which is assigned in constructor.
     * Field is called latestPubDate for confuse decreasing - the same name is used in RSSChannel
     *
     * @return pubDate
     */
    Date getLatestPubDate() {
        return latestPubDate;
    }

    /**
     * Setup configured item fields
     * Parse latestPubDate
     *
     * @param configuration RSSConfiguration instance
     * @param feed Link to RSS feed
     * @param source parsed Map from FeedModel
     */
    RSSItem(RSSConfiguration configuration, String feed, Map<String, String> source) throws InvalidObjectException {
        if (! source.keySet().containsAll(RSSConfiguration.getMandatoryRawItemFields())) {
            throw new InvalidObjectException("RSS Channel does not contains all the mandatory fields");
        }
        this.body = new HashMap<>();
        source.forEach((key, value) -> {
            if (configuration.getItemFields(feed).contains(key)) {
                body.put(key, value);
            }
        });
        latestPubDate = PubDateParser.parse(source.get("pubDate".toLowerCase()));
    }
}
