package seleksi.labpro.owca.utils;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatUtil {

    public static String formatDuration(int totalSeconds) {
        Duration duration = Duration.ofSeconds(totalSeconds);
        LocalTime time = LocalTime.ofSecondOfDay(duration.getSeconds());
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}