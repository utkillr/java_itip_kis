package config;

import java.util.Date;

/**
 * Aggregator class for FeedStatus and last PubDate
 */
class LocalFeedInfo {
    FeedStatus status;
    Date lastPubDate;

    LocalFeedInfo() {
        status = FeedStatus.ON;
        lastPubDate = null;
    }
}
