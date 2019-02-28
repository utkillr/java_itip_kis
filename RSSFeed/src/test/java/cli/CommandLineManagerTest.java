package cli;

import config.RSSConfiguration;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CommandLineManagerTest {
    private CommandLineManager manager = new CommandLineManager();

    @Test
    @DisplayName("Test of processing item params")
    public void itemParamsProcessTest() {
        List<String> itemParamsFirst = Arrays.asList("title", "link", "description", "pubdate");
        List<String> itemParamsSecond = Arrays.asList("title", "link", "description");

        manager.setItemParams(itemParamsFirst);
        List<String> itemFields = RSSConfiguration.getInstance().getItemFields();
        assertEquals(itemFields, itemParamsFirst);

        manager.setItemParams(itemParamsSecond);
        itemFields = RSSConfiguration.getInstance().getItemFields();
        assertEquals(itemFields, itemParamsSecond);
        assertNotEquals(itemFields, itemParamsFirst);
    }

    @Test
    @DisplayName("Test of processing channel params")
    public void channelParamsProcessTest() {
        List<String> channelParamsFirst = Arrays.asList("title", "link", "description", "pubdate");
        List<String> channelParamsSecond = Arrays.asList("title", "link", "description");

        manager.setChannelParams(channelParamsFirst);
        List<String> channelFields = RSSConfiguration.getInstance().getChannelFields();
        assertEquals(channelFields, channelParamsFirst);

        manager.setChannelParams(channelParamsSecond);
        channelFields = RSSConfiguration.getInstance().getChannelFields();
        assertEquals(channelFields, channelParamsSecond);
        assertNotEquals(channelFields, channelParamsFirst);
    }

    @Test
    @DisplayName("Test of creating file if it exists either not")
    public void creatingFileTest() {

        String fileName = "file.txt";
        File f = new File(fileName);
        assertFalse(f.exists());

        manager.createFileIfNotExists(fileName);
        assertTrue(f.exists());

        manager.createFileIfNotExists(fileName);
        assertTrue(f.exists());

        f.delete();
    }

    @Test
    @DisplayName("Test of processing time to poll")
    public void timeToPollProcessTest() {
        Long timeToPollFirst = 10L;
        Long timeToPollSecond = 20L;

        manager.setTimeToPoll(timeToPollFirst);
        assertEquals(timeToPollFirst, RSSConfiguration.getInstance().getTimeToPoll());

        manager.setTimeToPoll(timeToPollSecond);
        assertEquals(timeToPollSecond, RSSConfiguration.getInstance().getTimeToPoll());
        assertNotEquals(timeToPollFirst, RSSConfiguration.getInstance().getTimeToPoll());
    }

    @Test
    @DisplayName("Test of processing rss")
    public void rssProcessingTest() {
        String firstRss = "firstRss";
        String secondRss = "secondRss";

        String firstFile = "firstFile.txt";
        String secondFile = "secondFile.txt";

        manager.associateRssToFile(firstRss, firstFile);
        assertEquals(firstFile, RSSConfiguration.getInstance().getRSSFeeds().get(firstRss));
        assertNotEquals(firstFile, RSSConfiguration.getInstance().getRSSFeeds().get(secondRss));
        assertNotEquals(secondFile, RSSConfiguration.getInstance().getRSSFeeds().get(firstRss));

        manager.turnRSSOff(firstRss);
        assertEquals("OFF", manager.getRssStatus(firstRss));

        manager.turnRSSOn(firstRss);
        assertEquals("ON", manager.getRssStatus(firstRss));

        manager.diassociateRss(firstRss);
        assertNotEquals(firstFile, RSSConfiguration.getInstance().getRSSFeeds().get(firstRss));
    }
}
