package model;

import config.RSSConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class RSSChannel {
    /**
     * This class represents RSS Channel (not Atom one).
     * Includes default fields which are unable to be removed
     */

    private RSSConfiguration configuration;

    private Map<String, String> metaBody;
    private List<RSSItem> items;
    private Date latestPubDate;

    public Map<String, String> getMetaBody() {
        return metaBody;
    }

    public List<RSSItem> getItems() {
        return items;
    }

    public Date getLatestPubDate() {
        return latestPubDate;
    }

    public RSSChannel(RSSConfiguration configuration, FeedModel model, final Date latestPubDate) {
        this.metaBody = new HashMap<>();
        this.configuration = configuration;
        model.metaSource.forEach((key, value) -> {
            if (configuration.getChannelFields().contains(key)) {
                metaBody.put(key, value);
            }
        });
        this.items = new ArrayList<>();
        for (Map<String, String> item : model.itemSources) {
            items.add(new RSSItem(configuration, item));
            if (latestPubDate != null) {
                items = items
                        .stream()
                        .filter(i ->
                                i.getLatestPubDate() == null || i.getLatestPubDate().compareTo(latestPubDate) > 0
                        )
                        .collect(Collectors.toList());
            }
        }

        this.latestPubDate = items
                .stream()
                .map(RSSItem::getLatestPubDate)
                .filter(Objects::nonNull)
                .min((d1, d2) -> -d1.compareTo(d2))
                .orElse(null);
    }
}
