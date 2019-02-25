package model;

import config.RSSConfiguration;
import util.PubDateParser;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RSSItem {
    /**
     * This class represents Item of RSS feed (not Atom one).
     * Includes default fields which are unable to be removed
     */

    private RSSConfiguration configuration;

    private Map<String, String> body;
    private Date latestPubDate;

    public Map<String, String> getBody() {
        return body;
    }

    public Date getLatestPubDate() {
        return latestPubDate;
    }

    public RSSItem(RSSConfiguration configuration, Map<String, String> source) {
        this.body = new HashMap<>();
        this.configuration = configuration;
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
