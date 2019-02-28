package model;

import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * This class implements our model of RSS Feed which is actually
 * Channel description source (metaSource) + item description sources (itemSources)
 */
@EqualsAndHashCode
public class FeedModel {

    public static String FEED_ITEM = "item";
    public static String FEED_CHANNEL ="channel";

    public Map<String, String> metaSource;
    public List<Map<String, String>> itemSources;

    public FeedModel() {
        metaSource = new HashMap<>();
        itemSources = new ArrayList<>();
    }
}
