package model;

import config.RSSConfiguration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents RSS Channel (Atom syntax is ignored for now).
 */
public class RSSChannel {

    private Map<String, String> metaBody;
    private List<RSSItem> items;
    private Date latestPubDate;

    /**
     * Get properties of RSS Channel
     *
     * @return map: Configured property -> value
     */
    public Map<String, String> getMetaBody() {
        return metaBody;
    }

    /**
     * Get list of items inside of channel
     *
     * @return list of items
     */
    public List<RSSItem> getItems() {
        return items;
    }

    /**
     * Get latest pub date, which is assigned in constructor.
     * Actually, the latest pubDate of items
     *
     * @return latest pubDate
     */
    public Date getLatestPubDate() {
        return latestPubDate;
    }

    /**
     * Setup configured channel fields
     * Setup items which are newer than latestPubDate
     * Update latestPubDate
     *
     * @param configuration RSSConfiguration instance
     * @param model parsed FeedModel
     * @param latestPubDate can be null - needed for filtering old items
     */
    public RSSChannel(RSSConfiguration configuration, FeedModel model, final Date latestPubDate) {
        this.metaBody = new HashMap<>();
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
