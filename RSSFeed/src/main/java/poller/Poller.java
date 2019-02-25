package poller;

import config.RSSConfiguration;
import model.FeedModel;
import model.RSSChannel;
import model.RSSItem;
import parser.FeedModelParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;

public class Poller implements Runnable {

    private static boolean running = false;
    private static long timeCheckThreshold = 5;

    public static void poll(RSSConfiguration configuration) {
        configuration.getRSSFeeds().forEach((feed, file) -> {
            if (configuration.isRSSFeedOn(feed)) {
                Date newPubDate = printRSSFeedToFile(feed, file, configuration.getRSSFeedLastPubDate(feed));
                configuration.notifyFeedRead(feed, newPubDate);
            }
        });
    }

    /**
     * Print rss to the provided file
     *
     * @param link rss feed link
     * @param file file name
     */
    public static Date printRSSFeedToFile(String link, String file, Date fromPubDate) {
        try {
            URL url = new URL(link);
            InputStream in = url.openStream();
            FeedModel model = new FeedModelParser().parse(in);
            Path path = Paths.get(file);
            RSSChannel channel = new RSSChannel(RSSConfiguration.getInstance(), model, fromPubDate);

            // we do not want to append empty channel description
            if (channel.getItems().size() > 0) {

                System.out.println("Writing new data :)");

                final String feedString = getStringFromMap(
                        channel.getMetaBody(), RSSConfiguration.getInstance().getChannelFields(), 0
                );
                Files.write(path, feedString.getBytes(), APPEND);

                for (RSSItem item : channel.getItems()) {
                    final String itemString = getStringFromMap(
                            item.getBody(), RSSConfiguration.getInstance().getItemFields(), 1
                    );
                    Files.write(path, itemString.getBytes(), APPEND);
                }
            }

            return channel.getLatestPubDate();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getStringFromMap(Map<String, String> map, List<String> availableKeys, int initialIndent) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < initialIndent; i++) {
            indentation.append("\t");
        }
        String indentationStr = indentation.toString();
        return map.entrySet()
                .stream()
                .filter((keyValue) -> availableKeys.contains(keyValue.getKey()))
                .map(keyValue -> String.format("%s%s:\n%s\t%s\n", indentationStr, keyValue.getKey(),
                        indentationStr, keyValue.getValue()))
                .collect(Collectors.joining()) + "\n";
    }

    @Override
    public void run() {
        running = true;
        RSSConfiguration configuration = RSSConfiguration.getInstance();
        while (running) {
            poll(configuration);
            try {
                sleep(configuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }

    private void sleep(RSSConfiguration configuration) throws InterruptedException {
        long currentTimeToPoll = configuration.getTimeToPoll();
        long leftTimeToPoll = currentTimeToPoll;
        while (leftTimeToPoll > timeCheckThreshold) {
            long newTimeToPoll = configuration.getTimeToPoll();
            // If somebody changed poll time, apply it and start sleeping from scratch
            if (newTimeToPoll != currentTimeToPoll) {
                System.out.println("Time changed from " + currentTimeToPoll + " to " + newTimeToPoll);
                currentTimeToPoll = newTimeToPoll;
                leftTimeToPoll = currentTimeToPoll;
            }
            if (running) {
                TimeUnit.SECONDS.sleep(timeCheckThreshold);
            }
            leftTimeToPoll -= timeCheckThreshold;
        }
        if (running) {
            TimeUnit.SECONDS.sleep(leftTimeToPoll);
        }
    }
}
