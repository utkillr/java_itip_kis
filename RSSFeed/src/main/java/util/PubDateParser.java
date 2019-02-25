package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PubDateParser {

    private static DateFormat[] formatters = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"),
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm zzz"),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy zzz"),
            new SimpleDateFormat("EEE MMM dd HH:mm yyyy zzz"),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"),
            new SimpleDateFormat("EEE MMM dd HH:mm zzz yyyy")
    };

    public static Date parse(String pubDate) {
        for (DateFormat formatter : formatters) {
            try {
                return formatter.parse(pubDate);
            } catch (ParseException ignored) {}
        }
        return null;
    }
}
