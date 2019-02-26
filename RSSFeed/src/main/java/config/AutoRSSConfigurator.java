package config;

import util.Log;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import static java.nio.file.StandardOpenOption.APPEND;

public class AutoRSSConfigurator {

    private static String file = "rss.cfg";

    private static Log log = new Log(AutoRSSConfigurator.class.getName(), System.out);

    public static void saveRSSConfiguration() {
        File f = new File(file);
        try {
            f.delete();
            f.createNewFile();
        } catch (IOException e) {
            log.error("Error occurred during creating file: " + e.getMessage());
            log.error("Configuration won't be saved");
            return;
        }

        synchronized (RSSConfiguration.getInstance()) {
            RSSConfiguration configuration = RSSConfiguration.getInstance();
            try {
                Files.write(f.toPath(), (getChannelFields(configuration) + "\n").getBytes(), APPEND);
                Files.write(f.toPath(), (getItemFields(configuration) + "\n").getBytes(), APPEND);
                Files.write(f.toPath(), (getTimeToPoll(configuration) + "\n").getBytes(), APPEND);
                Files.write(f.toPath(), (getRSSFeedsFullInfo(configuration) + "\n").getBytes(), APPEND);
            } catch (IOException e) {
                log.error("Not all the configuration can be written.");
            }
        }
    }

    public static void loadRSSConfiguration() {
        File f = new File(file);
        if (!f.canRead()) {
            log.error("Can't read config file. Configuration won't be loaded");
            return;
        }

        RSSConfiguration configuration = RSSConfiguration.getInstance();
        try {
            List<String> configList = Files.readAllLines(f.toPath());
            if (configList.size() < 3) {
                log.error("Config is invalid");
            } else {
                configuration.reconfig(
                        parseFields(configList.get(0)),
                        parseFields(configList.get(1))
                );
                configuration.setTimeToPoll(Long.valueOf(configList.get(2)));
                if (configList.size() > 3) {
                    for (String line : configList.subList(3, configList.size())) {
                        if (line.isEmpty()) continue;
                        // Gonna be List of 4 elems: feed, link, status and pubdate
                        List<String> parsed = parseFields(line);
                        if (parsed.size() != 4) {
                            log.warn("RSS Feed Configuration can't be read");
                            continue;
                        }
                        // Set feed and link
                        configuration.addRSSFeed(parsed.get(0), parsed.get(1));
                        // Set status
                        if (parsed.get(2).equals("0")) {
                            configuration.turnOffRSSFeed(parsed.get(0));
                        } else {
                            configuration.turnOnRSSFeed(parsed.get(0));
                        }
                        // Set pub date
                        String lastPubDate = parsed.get(3);
                        if (!lastPubDate.equals("null")) {
                            configuration.notifyFeedRead(parsed.get(0), new Date(Long.valueOf(lastPubDate)));
                        } else {
                            configuration.notifyFeedRead(parsed.get(0), null);
                        }
                    }
                }
            }

        } catch (IOException e) {
            log.error("Not all the configuration can be written.");
        }
    }

    private static String getChannelFields(RSSConfiguration configuration) {
        StringJoiner joiner = new StringJoiner(";");
        for (String field : configuration.getChannelFields()) {
            joiner.add(field);
        }
        return joiner.toString();
    }

    private static String getItemFields(RSSConfiguration configuration) {
        StringJoiner joiner = new StringJoiner(";");
        for (String item : configuration.getItemFields()) {
            joiner.add(item);
        }
        return joiner.toString();
    }

    private static String getTimeToPoll(RSSConfiguration configuration) {
        return configuration.getTimeToPoll().toString();
    }

    private static String getRSSFeedsFullInfo(RSSConfiguration configuration) {
        StringJoiner linesJoiner = new StringJoiner("\n");
        configuration.getRSSFeeds().forEach((feed, link) -> {
            StringBuilder builder = new StringBuilder();
            builder
                    .append(feed).append(";")
                    .append(link).append(";")
                    .append(configuration.isRSSFeedOn(feed) ? "1" : "0").append(";");
            Date lastPubDate = configuration.getRSSFeedLastPubDate(feed);
            if (lastPubDate != null) {
                builder.append(Long.toString(lastPubDate.getTime()));
            } else {
                builder.append("null");
            }
            linesJoiner.add(builder.toString());
        });
        return linesJoiner.toString();
    }


    private static List<String> parseFields(String line) {
        return Arrays.asList(line.split(";"));
    }
}
