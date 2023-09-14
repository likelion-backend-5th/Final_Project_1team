package mutsa.api.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterUtil {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(KOREA_ZONE);

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return FORMATTER.format(localDateTime);
    }
}
