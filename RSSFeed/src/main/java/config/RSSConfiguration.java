package config;

import poller.Poller;

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

    private long timeToPoll;
    private Map<String, String> RSSFeeds;
    private Map<String, LocalFeedInfo> RSSFeedStatus;
    private Map<String, List<String>> RSSFeedChannelFields;
    private Map<String, List<String>> RSSFeedItemFields;

    private RSSConfiguration() {
        RSSFeedChannelFields = new HashMap<>();
        RSSFeedItemFields = new HashMap<>();
        timeToPoll = 30L;
        RSSFeeds = new HashMap<>();
        RSSFeedStatus = new HashMap<>();
    }

    /**
     * Get all the mandatory channel fields
     *
     * @return unmodifiable list of mandatory channel fields
     */
    public static List<String> getMandatoryRawChannelFields() {
        return Collections.unmodifiableList(ImmutableRSSConfig.mandatoryRawChannelFields);
    }

    /**
     * Get all the mandatory item fields
     *
     * @return unmodifiable list of mandatory item fields
     */
    public static List<String> getMandatoryRawItemFields() {
        return Collections.unmodifiableList(ImmutableRSSConfig.mandatoryRawItemFields);
    }

    /**
     * Get all the available channel fields
     *
     * @return unmodifiable list of available channel fields
     */
    public static List<String> getAvailableChannelFields() {
        return Collections.unmodifiableList(ImmutableRSSConfig.availableChannelFields);
    }

    /**
     * Get all the available item fields
     *
     * @return unmodifiable list of available item fields
     */
    public static List<String> getAvailableItemFields() {
        return Collections.unmodifiableList(ImmutableRSSConfig.availableItemFields);
    }

    /**
     * Get configured item fields
     *
     * @param feed Feed to get configured item feeds
     * @return unmodifiable list of configured item fields
     */
    public List<String> getItemFields(String feed) {
        return Collections.unmodifiableList(RSSFeedItemFields.get((feed)));
    }

    /**
     * Get configured channel fields
     *
     * @param feed Feed to get configured channel feeds
     * @return unmodifiable list of configured channel fields
     */
    public List<String> getChannelFields(String feed) {
        return Collections.unmodifiableList(RSSFeedChannelFields.get((feed)));
    }

    public void setRSSFeedFile(String feed, String file) {
        RSSFeeds.replace(feed, file);
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
     * @param feed Feed to reconfig
     * @param itemFields item fields to be configured
     * @param channelFields channel fields to be configured
     */
    public void reconfig(String feed, List<String> itemFields, List<String> channelFields) {

        if(itemFields != null && itemFields.size() > 0){
            List<String> newFields = itemFields
                    .stream()
                    .map(String::toLowerCase)
                    .filter(ImmutableRSSConfig.rawAvailableItemFields::contains)
                    .collect(Collectors.toList());
            if (! newFields.isEmpty()) {
                this.RSSFeedItemFields.replace(feed, newFields);
            }
        }

        if(channelFields != null && channelFields.size() > 0){
            List<String> newFields = channelFields
                    .stream()
                    .map(String::toLowerCase)
                    .filter(ImmutableRSSConfig.rawAvailableChannelFields::contains)
                    .collect(Collectors.toList());
            if (! newFields.isEmpty()) {
                this.RSSFeedChannelFields.replace(feed, newFields);
            }
        }
    }

    /**
     * Set time to poll. It can't be more than polling threshold
     *
     * @param time new time to poll
     */
    public void setTimeToPoll(Long time) {
        this.timeToPoll = time > Poller.timeCheckThreshold ? time : Poller.timeCheckThreshold;
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
            RSSFeedChannelFields.put(feed, new ArrayList<>(ImmutableRSSConfig.defaultRawAvailableChannelFields));
            RSSFeedItemFields.put(feed, new ArrayList<>(ImmutableRSSConfig.defaultRawAvailableItemFields));
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
        RSSFeedStatus.remove(feed);
        RSSFeedChannelFields.remove(feed);
        RSSFeedItemFields.remove(feed);
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
