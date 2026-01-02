package konnekt.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateRangeUtil {

    public static LocalDate nextSaturday(LocalDate start) {
        return start.with(
                TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)
        );
    }
}
