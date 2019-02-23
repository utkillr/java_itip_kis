import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://www.vogella.com/article.rss");
            InputStream in = url.openStream();
            FeedModel model = RSSFeed.parse(in);
            RSSFeed feed = new RSSFeed(RSSConfiguration.getInstance(), model);
            System.out.println(feed.metaBody.get("title"));
            for (RSSItem item : feed.items) {
                System.out.println(item.body.get("title"));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
