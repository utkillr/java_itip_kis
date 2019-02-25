package config;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Configuration singleton for RSS Feed, Items and whole application
 */
public class RSSConfiguration {

    /**
     * Singleton field
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

    /**
     * Get all the available channel fields
     *
     * @return unmodifiable list of available channel fields
     */
    public static List<String> getAvailableChannelFields() {
        return Collections.unmodifiableList(availableChannelFields);
    }

    /**
     * Get all the available item fields
     *
     * @return unmodifiable list of available item fields
     */
    public static List<String> getAvailableItemFields() {
        return Collections.unmodifiableList(availableItemFields);
    }

    /**
     * Get configured item fields
     *
     * @return unmodifiable list of configured item fields
     */
    public List<String> getItemFields() {
        return Collections.unmodifiableList(itemFields);
    }

    /**
     * Get configured channel fields
     *
     * @return unmodifiable list of configured channel fields
     */
    public List<String> getChannelFields() {
        return Collections.unmodifiableList(channelFields);
    }

    /**
     * Get singleton configuration instance
     *
     * @return configuration instance
     */
    public static RSSConfiguration getInstance() {
        return configuration;
    }

    /**
     * Reconfig fields of items and channel.
     * Waits for lists which are not handled in case of they are empty
     * Unavailable fields are ignored
     *
     * @param itemFields item fields to be configured
     * @param channelFields channel fields to be configured
     */
    public void reconfig(List<String> itemFields, List<String> channelFields) {

        if(itemFields != null && itemFields.size() > 0){
            this.itemFields = itemFields
                    .stream()
                    .map(String::toLowerCase)
                    .filter(rawAvailableItemFields::contains)
                    .collect(Collectors.toList());
        }

        if(channelFields != null && channelFields.size() > 0){
            this.channelFields = channelFields
                    .stream()
                    .map(String::toLowerCase)
                    .filter(rawAvailableChannelFields::contains)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Set time to poll
     *
     * @param time new time to poll
     */
    public void setTimeToPoll(Long time) {
        this.timeToPoll = time;
    }

    /**
     * Get time to poll
     *
     * @return current time to poll
     */
    public Long getTimeToPoll() {
        return timeToPoll;
    }

    /**
     * Add new RSS Feed to application
     * If it already in, Exception is raised
     *
     * @param feed link to RSS Feed
     * @param file full path to file to associate with feed
     */
    public void addRSSFeed(String feed, String file) {
        if (!RSSFeeds.containsKey(feed)) {
            RSSFeeds.put(feed, file);
            RSSFeedStatus.put(feed, new LocalFeedInfo());
        } else {
            throw new InvalidParameterException("Feed " + feed + " is already added");
        }
    }

    /**
     * Remove RSS Feed from application
     *
     * @param feed link to RSS Feed
     */
    public void delRSSFeed(String feed) {
        RSSFeeds.remove(feed);
    }

    /**
     * Turn RSS Feed on
     * If Feed is not in, Exception is raised
     *
     * @param feed link to RSS Feed
     */
    public void turnOnRSSFeed(String feed) {
        if (RSSFeeds.containsKey(feed)) {
            RSSFeedStatus.get(feed).status = FeedStatus.ON;
        } else {
            throw new InvalidParameterException("Feed " + feed + " is not added");
        }
    }

    /**
     * Turn RSS Feed off without removal
     * If Feed is not in, Exception is raised
     *
     * @param feed link to RSS Feed
     */
    public void turnOffRSSFeed(String feed) {
        if (RSSFeeds.containsKey(feed)) {
            RSSFeedStatus.get(feed).status = FeedStatus.OFF;
        } else {
            throw new InvalidParameterException("Feed " + feed + " is not added");
        }
    }

    /**
     * Check if RSS Feed is on
     * If Feed is not in, Exception is raised
     *
     * @param feed link to RSS Feed
     * @return true if Feed is on, else false
     */
    public boolean isRSSFeedOn(String feed) {
        if (RSSFeeds.containsKey(feed)) {
            return RSSFeedStatus.get(feed).status == FeedStatus.ON;
        } else {
            throw new InvalidParameterException("Feed " + feed + " is not added");
        }
    }

    /**
     * Get last read pubDate of RSS Feed
     * If Feed is not in, Exception is raised
     *
     * @param feed link to RSS Feed
     * @return latest pubDate of feed
     */
    public Date getRSSFeedLastPubDate(String feed) {
        if (RSSFeeds.containsKey(feed)) {
            return RSSFeedStatus.get(feed).lastPubDate;
        } else {
            throw new InvalidParameterException("Feed " + feed + " is not added");
        }
    }

    /**
     * Set RSS Feed last pubDate to new one
     * If Feed is not in, Exception is raised
     *
     * @param feed link to RSS Feed
     * @param lastPubDate new pubDate
     */
    public void notifyFeedRead(String feed, Date lastPubDate) {
        if (RSSFeeds.containsKey(feed)) {
            if (lastPubDate != null) {
                RSSFeedStatus.get(feed).lastPubDate = lastPubDate;
            }
        } else {
            throw new InvalidParameterException("Feed " + feed + " is not added");
        }
    }

    /**
     * Get all the RSS Feeds with its associated files
     *
     * @return unmodifiable map: feed -> file
     */
    public Map<String, String> getRSSFeeds() {
        return Collections.unmodifiableMap(RSSFeeds);
    }
}
