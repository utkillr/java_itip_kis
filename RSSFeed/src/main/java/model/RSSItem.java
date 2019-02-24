package model;

import config.RSSConfiguration;

import java.util.HashMap;
import java.util.Map;

public class RSSItem {
    /**
     * This class represents Item of RSS feed (not Atom one).
     * Includes default fields which are unable to be removed
     */

    private RSSConfiguration configuration;

    private Map<String, String> body;

    public Map<String, String> getBody() {
        return body;
    }

    public RSSItem(RSSConfiguration configuration, Map<String, String> source) {
        this.body = new HashMap<>();
        this.configuration = configuration;
        source.forEach((key, value) -> {
            if (configuration.getItemFields().contains(key)) {
                body.put(key, value);
            }
        });
    }
}
