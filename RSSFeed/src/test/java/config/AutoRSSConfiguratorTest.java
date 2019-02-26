package config;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class AutoRSSConfiguratorTest {

    private String file = "test.cfg";

    public void cleanup() {
        List<String> feeds = new ArrayList<>(RSSConfiguration.getInstance().getRSSFeeds().keySet());
        feeds.forEach(RSSConfiguration.getInstance()::delRSSFeed);
        assertTrue(RSSConfiguration.getInstance().getRSSFeeds().isEmpty());
    }

    public void deleteFile() {
        File file = new File(this.file);
        file.delete();
    }

    @Test
    @DisplayName("Test of ability to save and load configurations")
    public void autoReconfig() {
        cleanup();
        deleteFile();

        AutoRSSConfigurator.setFile(file);

        RSSConfiguration.getInstance().setTimeToPoll(100L);

        List<String> dummyChannelFields = Arrays.asList("description", "title", "link", "copyright");
        List<String> dummyItemFields = Arrays.asList("description", "title", "link", "pubdate");
        RSSConfiguration.getInstance().addRSSFeed("dummy.rss", "dummy.txt");
        RSSConfiguration.getInstance().turnOffRSSFeed("dummy.rss");
        RSSConfiguration.getInstance().notifyFeedRead("dummy.rss", null);
        RSSConfiguration.getInstance().reconfig("dummy.rss", dummyItemFields, dummyChannelFields);

        List<String> newdummyChannelFields = Arrays.asList("description", "title");
        List<String> newdummyItemFields = Arrays.asList("title", "link");
        Date newdummyDate = new Date(1000000L);
        RSSConfiguration.getInstance().addRSSFeed("newdummy.rss", "newdummy.txt");
        RSSConfiguration.getInstance().turnOnRSSFeed("newdummy.rss");
        RSSConfiguration.getInstance().notifyFeedRead("newdummy.rss", newdummyDate);
        RSSConfiguration.getInstance().reconfig("newdummy.rss", newdummyItemFields, newdummyChannelFields);

        AutoRSSConfigurator.saveRSSConfiguration();

        cleanup();

        AutoRSSConfigurator.loadRSSConfiguration();

        assertEquals((Long)100L, RSSConfiguration.getInstance().getTimeToPoll());

        assertEquals("dummy.txt", RSSConfiguration.getInstance().getRSSFeeds().get("dummy.rss"));
        assertFalse(RSSConfiguration.getInstance().isRSSFeedOn("dummy.rss"));
        assertNull(RSSConfiguration.getInstance().getRSSFeedLastPubDate("dummy.rss"));
        assertTrue(RSSConfiguration.getInstance().getChannelFields("dummy.rss").containsAll(dummyChannelFields));
        assertTrue(dummyChannelFields.containsAll(RSSConfiguration.getInstance().getChannelFields("dummy.rss")));
        assertTrue(RSSConfiguration.getInstance().getItemFields("dummy.rss").containsAll(dummyItemFields));
        assertTrue(dummyItemFields.containsAll(RSSConfiguration.getInstance().getItemFields("dummy.rss")));

        assertEquals("newdummy.txt", RSSConfiguration.getInstance().getRSSFeeds().get("newdummy.rss"));
        assertTrue(RSSConfiguration.getInstance().isRSSFeedOn("newdummy.rss"));
        assertEquals(newdummyDate, RSSConfiguration.getInstance().getRSSFeedLastPubDate("newdummy.rss"));
        assertTrue(RSSConfiguration.getInstance().getChannelFields("newdummy.rss").containsAll(newdummyChannelFields));
        assertTrue(newdummyChannelFields.containsAll(RSSConfiguration.getInstance().getChannelFields("newdummy.rss")));
        assertTrue(RSSConfiguration.getInstance().getItemFields("newdummy.rss").containsAll(newdummyItemFields));
        assertTrue(newdummyItemFields.containsAll(RSSConfiguration.getInstance().getItemFields("newdummy.rss")));

        cleanup();
        deleteFile();
    }

}
