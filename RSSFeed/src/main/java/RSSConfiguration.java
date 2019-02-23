import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private static String FEED_TITLE = "title";
    private static String FEED_LINK = "link";
    private static String FEED_DESCRIPTION = "description";
    private static String FEED_LANGUAGE = "language";
    private static String FEED_COPYRIGHT = "language";
    private static String FEED_MANAGING_EDITOR = "language";
    private static String FEED_WEB_MASTER = "language";
    private static String FEED_PUB_DATE = "language";
    private static String FEED_LAST_BUILD_DATE = "language";
    private static String FEED_CATEGORY = "language";
    private static String FEED_GENERATOR = "language";
    private static String FEED_DOCS = "language";
    private static String FEED_CLOUD = "language";
    private static String FEED_TTL = "language";
    private static String FEED_RATING = "language";
    private static String FEED_TEXT_INPUT = "language";
    private static String FEED_SKIP_HOURS = "language";
    private static String FEED_SKIP_DAYS = "language";

    private static List<String> availableFeedFields = Arrays.asList(
            FEED_TITLE, FEED_LINK, FEED_DESCRIPTION, FEED_LANGUAGE, FEED_COPYRIGHT, FEED_MANAGING_EDITOR,
            FEED_WEB_MASTER, FEED_PUB_DATE, FEED_LAST_BUILD_DATE, FEED_CATEGORY, FEED_GENERATOR, FEED_DOCS, FEED_CLOUD,
            FEED_TTL, FEED_RATING, FEED_TEXT_INPUT, FEED_SKIP_HOURS, FEED_SKIP_DAYS
    );

    private List<String> itemFields;
    private List<String> feedFields;
    private long secsToPoll = 60;

    private RSSConfiguration() {
        itemFields = new ArrayList<>();
        feedFields = new ArrayList<>();
    }

    public List<String> getItemFields() {
        // Call new to avoid external modification
        return new ArrayList<>(itemFields);
    }

    public List<String> getFeedFields() {
        // Call new to avoid external modification
        return new ArrayList<>(feedFields);
    }

    public static RSSConfiguration getInstance() {
        return configuration;
    }

    public void reconfig(List<String> itemFields, List<String> feedFields) {
        this.itemFields = itemFields.stream().filter(availableItemFields::contains).collect(Collectors.toList());
        this.feedFields = feedFields.stream().filter(availableFeedFields::contains).collect(Collectors.toList());
    }
}
