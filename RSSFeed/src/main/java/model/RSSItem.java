package model;

import config.RSSConfiguration;
import util.PubDateParser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
     * @param source parsed Map from FeedModel
     */
    RSSItem(RSSConfiguration configuration, Map<String, String> source) {
        this.body = new HashMap<>();
        source.forEach((key, value) -> {
            if (configuration.getItemFields().contains(key)) {
                body.put(key, value);
            }
        });
        if (source.containsKey("pubDate".toLowerCase())) {
            latestPubDate = PubDateParser.parse(source.get("pubDate".toLowerCase()));
        } else {
            latestPubDate = null;
        }
    }
}
