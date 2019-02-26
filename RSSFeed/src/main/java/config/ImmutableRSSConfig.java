package config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ImmutableRSSConfig {
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
    static List<String> availableItemFields = Arrays.asList(
            ITEM_TITLE, ITEM_DESCRIPTION, ITEM_LINK, ITEM_AUTHOR, ITEM_CATEGORY, ITEM_COMMENTS, ITEM_ENCLOSURE,
            ITEM_GUID, ITEM_PUB_DATE, ITEM_SOURCE

    );
    static List<String> rawAvailableItemFields = availableItemFields
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
    static List<String> availableChannelFields = Arrays.asList(
            FEED_TITLE, FEED_LINK, FEED_DESCRIPTION, FEED_LANGUAGE, FEED_COPYRIGHT, FEED_MANAGING_EDITOR,
            FEED_WEB_MASTER, FEED_PUB_DATE, FEED_LAST_BUILD_DATE, FEED_CATEGORY, FEED_GENERATOR, FEED_DOCS, FEED_CLOUD,
            FEED_TTL, FEED_RATING, FEED_TEXT_INPUT, FEED_SKIP_HOURS, FEED_SKIP_DAYS
    );
    static List<String> rawAvailableChannelFields = availableChannelFields
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toList());
}
