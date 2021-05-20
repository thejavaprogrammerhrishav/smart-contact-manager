package com.smart.contact.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateDto {
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter LONG_FORMAT = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");

    private final LocalDate start;
    private final LocalDate end;

    public DateDto(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public static DateDto getWeek(String date, DateTimeFormatter formatter) {
        LocalDate now = LocalDate.parse(date, formatter);
        return new DateDto(now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)),
                now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)));
    }

    public static DateDto getMonth(String date, DateTimeFormatter formatter) {
        LocalDate now = LocalDate.parse(date, formatter);
        return new DateDto(now.with(TemporalAdjusters.firstDayOfMonth()),
                now.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static String getDateNormal(LocalDate date) {
        return date.format(FORMAT);
    }

    public static String getDateLong(LocalDate date) {
        return date.format(LONG_FORMAT);
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}
