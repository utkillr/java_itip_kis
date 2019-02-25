package config;

import java.util.Date;

class LocalFeedInfo {
    FeedStatus status;
    Date lastPubDate;

    LocalFeedInfo() {
        status = FeedStatus.ON;
        lastPubDate = null;
    }
}
