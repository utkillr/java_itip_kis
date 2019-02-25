package cli;

import config.RSSConfiguration;
import model.FeedModel;
import model.RSSChanel;
import model.RSSItem;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import parser.FeedModelParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;

public class CommandLineManagerImpl implements CommandLineManager {

    @Override
    public void init() {
        RSSConfiguration.getInstance().reconfig(
                Arrays.asList("title", "description", "link"),
                Arrays.asList("title", "description", "link")
        );
    }

    @Override
    public void printHelp(final Options options) {
        final String commandLineSyntax = "java rss.jar";
        final PrintWriter writer = new PrintWriter(System.out);
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(
                writer,
                80,
                commandLineSyntax,
                "Options",
                options,
                3,
                5,
                "-- HELP --",
                true);
        writer.flush(); // вывод
    }

    @Override
    public void createFileIfNotExists(String file) {
        File f = new File(file);
        if (!f.exists() && !f.isDirectory()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addRssToFile(String link, String file) {
        try {
            URL url = new URL(link);
            InputStream in = url.openStream();
            FeedModel model = new FeedModelParser().parse(in);
            Path path = Paths.get(file);
            RSSChanel feed = new RSSChanel(RSSConfiguration.getInstance(), model);

            final String feedString = getStringFromMap(feed.getMetaBody(), RSSConfiguration.getInstance().getFeedFields());
            Files.write(path, feedString.getBytes(), APPEND);

            for (RSSItem item : feed.getItems()) {
                final String itemString = getStringFromMap(item.getBody(), RSSConfiguration.getInstance().getItemFields());
                Files.write(path, itemString.getBytes(), APPEND);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStringFromMap(Map<String, String> map, List<String> availableKeys) {
        return map.entrySet()
                .stream()
                .filter((keyValue) -> availableKeys.contains(keyValue.getKey()))
                .map(keyValue -> String.format("%s: %s\n", keyValue.getKey(), keyValue.getValue()))
                .collect(Collectors.joining()) + "\n";
    }

    @Override
    public void getItemParams() {
        System.out.println(RSSConfiguration.getAvailableItemFields());
    }

    @Override
    public void setItemParams(List<String> params) {
        RSSConfiguration.getInstance().reconfig(params, Collections.emptyList());
    }

    @Override
    public void getFeedParams() {
        System.out.println(RSSConfiguration.getAvailableFeedFields());
    }

    @Override
    public void setFeedParams(List<String> params) {
        RSSConfiguration.getInstance().reconfig(Collections.emptyList(), params);
    }
}
