package config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import poller.Poller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class RSSConfigurationTest {

    @Before
    public void setUp() {
        assertTrue(RSSConfiguration.getInstance().getRSSFeeds().isEmpty());
        RSSConfiguration.getInstance().addRSSFeed("dummy.rss", "dummy.txt");
    }

    @After
    public void tearDown() {
        RSSConfiguration.getInstance().delRSSFeed("dummy.rss");
        assertTrue(RSSConfiguration.getInstance().getRSSFeeds().isEmpty());
    }

    @Test
    @DisplayName("Test ability of adding new RSS Feed")
    public void addRemoveRSSFeedTest() {
        assertEquals("dummy.txt", RSSConfiguration.getInstance().getRSSFeeds().get("dummy.rss"));
        assertTrue(RSSConfiguration.getInstance().isRSSFeedOn("dummy.rss"));
    }

    @Test
    @DisplayName("Test ability of RSS Feed reconfiguration")
    public void reconfigTest() {
        List<String> validChannelParams = Arrays.asList("description", "copyright");
        List<String> validItemParams = Arrays.asList("description", "title");
        RSSConfiguration.getInstance().reconfig("dummy.rss", validItemParams, validChannelParams);
        assertTrue(RSSConfiguration.getInstance().getChannelFields("dummy.rss").containsAll(validChannelParams));
        assertTrue(validChannelParams.containsAll(RSSConfiguration.getInstance().getChannelFields("dummy.rss")));
        assertTrue(RSSConfiguration.getInstance().getItemFields("dummy.rss").containsAll(validItemParams));
        assertTrue(validItemParams.containsAll(RSSConfiguration.getInstance().getItemFields("dummy.rss")));

        List<String> notOnlyValidChannelParams = Arrays.asList("description", "copyright", "dummy");
        List<String> notOnlyValidItemParams = Arrays.asList("description", "title", "dummy");
        RSSConfiguration.getInstance().reconfig("dummy.rss", notOnlyValidItemParams, notOnlyValidChannelParams);
        assertFalse(RSSConfiguration.getInstance().getChannelFields("dummy.rss").containsAll(notOnlyValidChannelParams));
        assertTrue(RSSConfiguration.getInstance().getChannelFields("dummy.rss").containsAll(validChannelParams));
        assertTrue(notOnlyValidChannelParams.containsAll(RSSConfiguration.getInstance().getChannelFields("dummy.rss")));
        assertFalse(RSSConfiguration.getInstance().getItemFields("dummy.rss").containsAll(notOnlyValidItemParams));
        assertTrue(RSSConfiguration.getInstance().getItemFields("dummy.rss").containsAll(validItemParams));
        assertTrue(notOnlyValidItemParams.containsAll(RSSConfiguration.getInstance().getItemFields("dummy.rss")));

        List<String> invalidChannelParams = Arrays.asList("dummy");
        List<String> invalidItemParams = Arrays.asList("dummy");
        RSSConfiguration.getInstance().reconfig("dummy.rss", invalidItemParams, invalidChannelParams);
        assertFalse(RSSConfiguration.getInstance().getChannelFields("dummy.rss").containsAll(invalidChannelParams));
        assertFalse(invalidChannelParams.containsAll(RSSConfiguration.getInstance().getChannelFields("dummy.rss")));
        assertTrue(RSSConfiguration.getInstance().getChannelFields("dummy.rss").containsAll(validChannelParams));
        assertTrue(validChannelParams.containsAll(RSSConfiguration.getInstance().getChannelFields("dummy.rss")));
        assertFalse(RSSConfiguration.getInstance().getItemFields("dummy.rss").containsAll(invalidItemParams));
        assertFalse(invalidItemParams.containsAll(RSSConfiguration.getInstance().getItemFields("dummy.rss")));
        assertTrue(RSSConfiguration.getInstance().getItemFields("dummy.rss").containsAll(validItemParams));
        assertTrue(validItemParams.containsAll(RSSConfiguration.getInstance().getItemFields("dummy.rss")));

        RSSConfiguration.getInstance().reconfig("dummy.rss", null, null);
        assertTrue(RSSConfiguration.getInstance().getChannelFields("dummy.rss").containsAll(validChannelParams));
        assertTrue(validChannelParams.containsAll(RSSConfiguration.getInstance().getChannelFields("dummy.rss")));
        assertTrue(RSSConfiguration.getInstance().getItemFields("dummy.rss").containsAll(validItemParams));
        assertTrue(validItemParams.containsAll(RSSConfiguration.getInstance().getItemFields("dummy.rss")));
    }

    @Test
    @DisplayName("Test ability of turning RSS Feed On and Off")
    public void turnRSSFeedOnOffTest() {
        RSSConfiguration.getInstance().turnOnRSSFeed("dummy.rss");
        assertTrue(RSSConfiguration.getInstance().isRSSFeedOn("dummy.rss"));
        RSSConfiguration.getInstance().turnOffRSSFeed("dummy.rss");
        assertFalse(RSSConfiguration.getInstance().isRSSFeedOn("dummy.rss"));
        RSSConfiguration.getInstance().turnOffRSSFeed("dummy.rss");
        assertFalse(RSSConfiguration.getInstance().isRSSFeedOn("dummy.rss"));
        RSSConfiguration.getInstance().turnOnRSSFeed("dummy.rss");
        assertTrue(RSSConfiguration.getInstance().isRSSFeedOn("dummy.rss"));
    }

    @Test
    @DisplayName("Test ability of changing latest PubDate")
    public void changePubDateTest() {
        RSSConfiguration.getInstance().notifyFeedRead("dummy.rss", null);
        assertNull(RSSConfiguration.getInstance().getRSSFeedLastPubDate("dummy.rss"));
        Date dummyPubDate = new Date(1000000L);
        RSSConfiguration.getInstance().notifyFeedRead("dummy.rss", dummyPubDate);
        assertEquals(dummyPubDate, RSSConfiguration.getInstance().getRSSFeedLastPubDate("dummy.rss"));
    }

    @Test
    @DisplayName("Test ability of changing polling time")
    public void changePollTimeTest() {
        RSSConfiguration.getInstance().setTimeToPoll(100L);
        assertEquals((Long)100L, RSSConfiguration.getInstance().getTimeToPoll());
        RSSConfiguration.getInstance().setTimeToPoll(1L);
        assertEquals((Long)Poller.timeCheckThreshold, RSSConfiguration.getInstance().getTimeToPoll());
        RSSConfiguration.getInstance().setTimeToPoll(100L);
        assertEquals((Long)100L, RSSConfiguration.getInstance().getTimeToPoll());}
}
