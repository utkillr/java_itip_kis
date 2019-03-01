package model;

import config.RSSConfiguration;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * This class implements our model of RSS Feed which is actually
 * Channel description source (metaSource) + item description sources (itemSources)
 */
@EqualsAndHashCode
public class FeedModel {

    public static String FEED_ITEM = "item";
    public static String ATOM_ITEM = "entry";
    public static String FEED_CHANNEL ="channel";
    public static String ATOM_CHANNEL ="feed";

    public Map<String, String> metaSource;
    public List<Map<String, String>> itemSources;

    public FeedModel() {
        metaSource = new HashMap<>();
        itemSources = new ArrayList<>();
    }

    public void atomToRSS() {
        Map<String, String> newMetaSource = new HashMap<>();
        metaSource.keySet().forEach(key -> newMetaSource.put(RSSConfiguration.atomToRSS(key), metaSource.get(key)));
        List<Map<String, String>> newItemSources = new ArrayList<>();
        for (Map<String, String> itemSource : itemSources) {
            Map<String, String> newItemSource = new HashMap<>();
            itemSource.keySet().forEach(key -> newItemSource.put(RSSConfiguration.atomToRSS(key), itemSource.get(key)));
            newItemSources.add(newItemSource);
        }
        this.metaSource = newMetaSource;
        this.itemSources = newItemSources;
    }

}
