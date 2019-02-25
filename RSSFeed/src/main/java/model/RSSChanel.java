package model;

import config.RSSConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RSSChanel {
    /**
     * This class represents RSS feed (not Atom one).
     * Includes default fields which are unable to be removed
     */

    private RSSConfiguration configuration;

    private Map<String, String> metaBody;
    private List<RSSItem> items;

    public Map<String, String> getMetaBody() {
        return metaBody;
    }

    public List<RSSItem> getItems() {
        return items;
    }

    public RSSChanel(RSSConfiguration configuration, FeedModel model) {
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
}
