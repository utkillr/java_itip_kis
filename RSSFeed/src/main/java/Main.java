import config.RSSConfiguration;
import model.FeedModel;
import model.RSSFeed;
import model.RSSItem;
import parser.FeedModelParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Main {
    // Work with Malformed XML only, no Atom fields
    public static void main(String[] args) throws InterruptedException {
        try {
            URL url = new URL("https://www.nasa.gov/rss/dyn/breaking_news.rss");
            InputStream in = url.openStream();
            FeedModel model = new FeedModelParser().parse(in);
            RSSConfiguration.getInstance().reconfig(
                    Arrays.asList("title", "description", "link"),
                    Arrays.asList("title", "description", "link")
            );
            RSSFeed feed = new RSSFeed(RSSConfiguration.getInstance(), model);
            System.out.println(feed.getMetaBody().get("title"));
            for (RSSItem item : feed.getItems()) {
                System.out.println(item.getBody().get("title"));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
