package config;

import model.RSSChannel;
import util.PubDateParser;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class RSSConfiguration {
    /**
     * This class provides configuration for RSS feed and its items
     */

    private static final RSSConfiguration configuration = new RSSConfiguration();

    private static String ITEM_TITLE = "title";
    private static String ITEM_DESCRIPTION = "description";
    private static String ITEM_LINK = "link";
    private static String ITEM_AUTHOR = "author";
    private static String ITEM_CATEGORY = "category";
    private static String ITEM_COMMENTS = "comments";
    private static String ITEM_ENCLOSURE = "enclosure";
    private static String ITEM_GUID = "guid";
    private static String ITEM_PUB_DATE = "pubDate";
    private static String ITEM_SOURCE = "source";
    private static List<String> availableItemFields = Arrays.asList(
            ITEM_TITLE, ITEM_DESCRIPTION, ITEM_LINK, ITEM_AUTHOR, ITEM_CATEGORY, ITEM_COMMENTS, ITEM_ENCLOSURE,
            ITEM_GUID, ITEM_PUB_DATE, ITEM_SOURCE

    );
    private static List<String> rawAvailableItemFields = availableItemFields
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toList());


    private static String FEED_TITLE = "title";
    private static String FEED_LINK = "link";
    private static String FEED_DESCRIPTION = "description";
    private static String FEED_LANGUAGE = "language";
    private static String FEED_COPYRIGHT = "copyright";
    private static String FEED_MANAGING_EDITOR = "managingEditor";
    private static String FEED_WEB_MASTER = "webMaster";
    private static String FEED_PUB_DATE = "pubDate";
    private static String FEED_LAST_BUILD_DATE = "lastBuildDate";
    private static String FEED_CATEGORY = "category";
    private static String FEED_GENERATOR = "generator";
    private static String FEED_DOCS = "docs";
    private static String FEED_CLOUD = "cloud";
    private static String FEED_TTL = "ttl";
    private static String FEED_RATING = "rating";
    private static String FEED_TEXT_INPUT = "textInput";
    private static String FEED_SKIP_HOURS = "skipHours";
    private static String FEED_SKIP_DAYS = "skipDays";
    private static List<String> availableChannelFields = Arrays.asList(
            FEED_TITLE, FEED_LINK, FEED_DESCRIPTION, FEED_LANGUAGE, FEED_COPYRIGHT, FEED_MANAGING_EDITOR,
            FEED_WEB_MASTER, FEED_PUB_DATE, FEED_LAST_BUILD_DATE, FEED_CATEGORY, FEED_GENERATOR, FEED_DOCS, FEED_CLOUD,
            FEED_TTL, FEED_RATING, FEED_TEXT_INPUT, FEED_SKIP_HOURS, FEED_SKIP_DAYS
    );
    private static List<String> rawAvailableChannelFields = availableChannelFields
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toList());

    // There goes actual class definition
    private List<String> itemFields;
    private List<String> channelFields;
    private long timeToPoll;
    private Map<String, String> RSSFeeds;
    private Map<String, LocalFeedInfo> RSSFeedStatus;

    private RSSConfiguration() {
        itemFields = new ArrayList<>();
        channelFields = new ArrayList<>();
        timeToPoll = 10L;
        RSSFeeds = new HashMap<>();
        RSSFeedStatus = new HashMap<>();
    }

    public static List<String> getAvailableChannelFields() {
        return availableChannelFields;
    }

    public static List<String> getAvailableItemFields() {
        return availableItemFields;
    }

    public List<String> getItemFields() {
        // Call new to avoid external modification
        return new ArrayList<>(itemFields);
    }

    public List<String> getChannelFields() {
        // Call new to avoid external modification
        return new ArrayList<>(channelFields);
    }

    public static RSSConfiguration getInstance() {
        return configuration;
    }

    public void reconfig(List<String> itemFields, List<String> channelFields) {

        if(itemFields.size() > 0){
            this.itemFields = itemFields
                    .stream()
                    .map(String::toLowerCase)
                    .filter(rawAvailableItemFields::contains)
                    .collect(Collectors.toList());
        }

        if(channelFields.size() > 0){
            this.channelFields = channelFields
                    .stream()
                    .map(String::toLowerCase)
                    .filter(rawAvailableChannelFields::contains)
                    .collect(Collectors.toList());
        }
    }

    public void setTimeToPoll(Long time) {
        this.timeToPoll = time;
    }

    public Long getTimeToPoll() {
        return timeToPoll;
    }

    public void addRSSFeed(String feed, String file) {
        if (!RSSFeeds.containsKey(feed)) {
            RSSFeeds.put(feed, file);
            RSSFeedStatus.put(feed, new LocalFeedInfo());
        } else {
            throw new InvalidParameterException("Feed " + feed + " is already added");
        }
    }

    public void turnOnRSSFeed(String feed) {
        if (RSSFeeds.containsKey(feed)) {
            RSSFeedStatus.get(feed).status = FeedStatus.ON;
        } else {
            throw new InvalidParameterException("Feed " + feed + " is not added");
        }
    }

    public void turnOffRSSFeed(String feed) {
        if (RSSFeeds.containsKey(feed)) {
            RSSFeedStatus.get(feed).status = FeedStatus.OFF;
        } else {
            throw new InvalidParameterException("Feed " + feed + " is not added");
        }
    }

    public boolean isRSSFeedOn(String feed) {
        if (RSSFeeds.containsKey(feed)) {
            return RSSFeedStatus.get(feed).status == FeedStatus.ON;
        } else {
            throw new InvalidParameterException("Feed " + feed + " is not added");
        }
    }

    public Date getRSSFeedLastPubDate(String feed) {
        if (RSSFeeds.containsKey(feed)) {
            return RSSFeedStatus.get(feed).lastPubDate;
        } else {
            throw new InvalidParameterException("Feed " + feed + " is not added");
        }
    }

    public void notifyFeedRead(String feed, Date lastPubDate) {
        if (lastPubDate != null) {
            RSSFeedStatus.get(feed).lastPubDate = lastPubDate;
        }
    }

    public void delRSSFeed(String feed) {
        RSSFeeds.remove(feed);
    }

    public Map<String, String> getRSSFeeds() {
        return new HashMap<>(RSSFeeds);
    }
}
