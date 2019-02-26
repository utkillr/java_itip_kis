package cli;

import config.RSSConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Implementation of CLI
 */
@Slf4j
public class CommandLineManager {

    /**
     * Initial manager setup
     */
    public void init() {
        RSSConfiguration.getInstance().reconfig(
                Arrays.asList("title", "description", "link"),
                Arrays.asList("title", "description", "link")
        );
    }

    /**
     * Print information about possible commands to the console
     */
    public void printHelp() {
        final PrintWriter writer = new PrintWriter(System.out);
        final String helpMessage =
                "Available options:\n\t" +
                    "-ip (Item Properties):\n\t\t" +
                        "(w/o params): Get list of used Item Properties\n\t\t" +
                        "<property_1> ... <property_n>: Set list of Item Properties to use\n\t" +
                    "-cp (Channel Properties):\n\t\t" +
                        "(w/o params): Get list of used Channel Properties\n\t\t" +
                        "<property_1> ... <property_n>: Set list of Channel Properties to use\n\t" +
                    "-rss:\n\t\t" +
                        "(w/o params): Get list of RSS Feeds and associated files\n\t\t" +
                        "add <rss link> <file>: Link RSS Feed to the file.\n\t\t\t" +
                            "If file exists or is a directory, error is raised\n\t\t" +
                        "del <rss link>: Delete RSS Feed and associated file\n\t\t" +
                        "on <rss link>: Turn RSS Feed ON\n\t\t" +
                        "off <rss link>: turn RSS Feed OFF\n\t" +
                    "-time:\n\t\t" +
                        "(w/o params): Get current time to poll in seconds\n\t\t" +
                        "<time>: Set time to poll in seconds\n\t" +
                    "-h:\n\t\t" +
                        "Print this Help message\n\t" +
                    "-exit:\n\t\t" +
                        "Exit from application";
        writer.println(helpMessage);
        writer.flush(); // вывод
    }

    /**
     * Create file if it does not exist yet
     *
     * @param file file name
     */
    public void createFileIfNotExists(String file) {
        File f = new File(file);
        if (!f.exists() && !f.isDirectory()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                log.error("[ERROR] Error occurred during creating file: " + e.getMessage());
            }
        }
    }

    /**
     * Associate RSS with file
     *
     * @param link rss feed link
     * @param file file name
     */
    public void associateRssToFile(String link, String file) {
        RSSConfiguration.getInstance().addRSSFeed(link, file);
    }

    /**
     * Disassociate RSS with file
     *
     * @param link rss feed link
     */
    public void diassociateRss(String link) {
        RSSConfiguration.getInstance().delRSSFeed(link);
    }

    /**
     * Turn RSS feed On
     *
     * @param link rss feed link
     */
    public void turnRSSOn(String link) {
        RSSConfiguration.getInstance().turnOnRSSFeed(link);
    }

    /**
     * Turn RSS feed Off
     *
     * @param link rss feed link
     */
    public void turnRSSOff(String link) {
        RSSConfiguration.getInstance().turnOffRSSFeed(link);
    }

    /**
     * Get Rss status (On or Off)
     *
     * @param link rss feed link
     */
    public String getRssStatus(String link) {
        return RSSConfiguration.getInstance().isRSSFeedOn(link) ? "ON" : "OFF";
    }

    /**
     * Print all the RSS feeds with associated files
     */
    public void printRss() {
        Map<String, String> rssFeeds = RSSConfiguration.getInstance().getRSSFeeds();
        final PrintWriter writer = new PrintWriter(System.out);
        if (rssFeeds.size() > 0) {
            rssFeeds.forEach((rss, file) ->
                    writer.println(
                            String.format(
                                    "%s (%s): %s",
                                    rss,
                                    getRssStatus(rss),
                                    file
                            )
                    )
            );
        } else {
            log.warn("[WARN] No RSS available");
        }
        writer.flush(); // вывод
    }

    /**
     * Get list of used item params
     */
    public void printItemParams() {
        System.out.println(RSSConfiguration.getAvailableItemFields());
    }

    /**
     * Set list of used item params
     *
     * @param params params to print
     */
    public void setItemParams(List<String> params) {
        RSSConfiguration.getInstance().reconfig(params, Collections.emptyList());
    }

    /**
     * Get list of used channel params
     */
    public void printChannelParams() {
        System.out.println(RSSConfiguration.getAvailableChannelFields());
    }

    /**
     * Set list of used channel params
     *
     * @param params params to print
     */
    public void setChannelParams(List<String> params) {
        RSSConfiguration.getInstance().reconfig(Collections.emptyList(), params);
    }

    /**
     * Set time to poll RSS Feeds
     *
     * @param time time in seconds
     */
    public void setTimeToPoll(Long time) {
        RSSConfiguration.getInstance().setTimeToPoll(time);
    }

    /**
     * Print time to poll RSS Feeds
     */
    public void printTimeToPoll() {
        long timeToPoll = RSSConfiguration.getInstance().getTimeToPoll();
        final PrintWriter writer = new PrintWriter(System.out);
        writer.println(timeToPoll);
        writer.flush(); // вывод
    }

    public void prettyPrint(String... messages) {
        StringBuilder builder = new StringBuilder();
        builder.append("[RESPONSE]:\n");
        for (String message : messages) {
            builder.append("\t").append(message).append("\n");
        }
        final PrintWriter writer = new PrintWriter(System.out);
        writer.println(builder.toString());
        writer.flush(); // вывод
    }
}
