package konnekt.utils;

import java.sql.Timestamp;
import java.time.Duration;

public class TimeAgo {

    public static String format(Timestamp t) {
        long sec = Duration.between(t.toInstant(),
                java.time.Instant.now()).getSeconds();

        if (sec < 60) return "• Just Now";
        if (sec < 3600) return "• " + sec / 60 + "m";
        if (sec < 86400) return "• " + sec / 3600 + "h";
        return "• " + sec / 86400 + "d";
    }
}
