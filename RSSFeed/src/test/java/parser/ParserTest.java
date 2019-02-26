package parser;

import model.FeedModel;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    @DisplayName("Test of FeedModel parser")
    public void feedModelParserTest() {
        InputStream inputStream = ParserTest.class.getClassLoader().getResourceAsStream("news.rss");

        FeedModel model = new FeedModel();
        model.metaSource.put("description", "A RSS news feed containing the latest NASA news articles and press releases.");
        model.metaSource.put("title", "NASA Breaking News");

        FeedModelParser parser = new FeedModelParser();
        FeedModel modelFromFile = parser.parse(inputStream);
        assertEquals(model, modelFromFile);
    }
}
