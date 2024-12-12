package kr.teamo2.utils;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    public static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    public static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");

    private static final DateTimeFormatter STANDARD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final DateTimeFormatter STANDARD_SPACE_SPLIT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter STANDARD_ONLY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final DateTimeFormatter STANDARD_HH_MM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private static final DateTimeFormatter STANDARD_ZONED_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final DateTimeFormatter HH_MM_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private static final DateTimeFormatter H_M_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("H:m")
            .toFormatter();

    private static final DateTimeFormatter YYYY_MM_DD_MINUS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter YYYY_MM_DD_SLASH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private static final DateTimeFormatter[] YYYY_MM_DD_FORMATTERS = new DateTimeFormatter[]{YYYY_MM_DD_MINUS_FORMATTER, YYYY_MM_DD_SLASH_FORMATTER};

    public static final DateTimeFormatter YYYY_MM_DD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final DateTimeFormatter ZONED_DATETIME_STANDARD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    public static LocalTime toLocalTime(Integer hour, Integer minutes) {
        if (hour == null || minutes == null) {
            return null;
        }
        try {
            return LocalTime.of(hour, minutes);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static LocalTime toLocalTime(String localTime) {
        if (localTime == null) {
            return null;
        }
        try {
            return LocalTime.parse(localTime, H_M_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDate toLocalDate(Integer month, Integer day) {
        if (month == null || day == null) {
            return null;
        }
        try {
            return LocalDate.of(LocalDate.now()
                    .getYear(), month, day);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static LocalDate toLocalDate(Integer year, Integer month, Integer day) {
        if (month == null || day == null) {
            return null;
        }
        try {
            return LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static LocalDate toLocalDate(String localDate) {
        if (localDate == null) {
            return null;
        }
        return Arrays
                .stream(YYYY_MM_DD_FORMATTERS)
                .map(formatter -> {
                    try {
                        return LocalDate.parse(localDate, formatter);
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    public static LocalDateTime toLocalDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        return List.of(STANDARD_FORMATTER, STANDARD_HH_MM_FORMATTER, STANDARD_SPACE_SPLIT_FORMATTER, STANDARD_ZONED_DATE_TIME_FORMATTER)
                .stream()
                .map(formatter -> {
                    try {
                        return LocalDateTime.parse(dateTime, formatter);
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    public static String toString(ZonedDateTime zonedDateTime) {
        try {
            if (zonedDateTime.getZone()
                    .equals(ZoneOffset.UTC)) {
                return zonedDateTime.format(UTC_FORMATTER);
            } else {
                return zonedDateTime.format(ZONED_DATETIME_STANDARD_FORMATTER);
            }
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static String toString(LocalDateTime localDateTime) {
        try {
            return localDateTime.format(STANDARD_FORMATTER);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static String toString(LocalTime time) {
        if (time == null) {
            return null;
        }
        try {
            return time.format(HH_MM_FORMATTER);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static String toSpaceSplitString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        try {
            return dateTime.format(STANDARD_SPACE_SPLIT_FORMATTER);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static ZonedDateTime downScaleMinutes(ZonedDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        int minute = dateTime.getMinute();
        minute = (minute / 10) * 10;
        return dateTime.withMinute(minute).truncatedTo(ChronoUnit.MINUTES);
    }

    public static Integer calculateAge(String birthDay) {
        if (birthDay == null || (birthDay.length() != 6 && birthDay.length() != 8)) {
            return null;
        }
        LocalDate currentDate = LocalDate.now(ZoneOffset.UTC);
        boolean isYYYYMMDDBirthDay = birthDay.length() == 8;
        try {
            if (isYYYYMMDDBirthDay) {
                LocalDate birthDate = LocalDate.parse(birthDay, YYYY_MM_DD_FORMATTER);
                Period period = Period.between(birthDate, currentDate);
                return period.getYears();
            }
            String prefix = Integer.parseInt(birthDay.substring(0, 2)) >= 24 ? "19" : "20";
            String yyyyMMddBirthDay = prefix + birthDay;
            LocalDate birthDate = LocalDate.parse(yyyyMMddBirthDay, YYYY_MM_DD_FORMATTER);
            Period period = Period.between(birthDate, currentDate);
            return period.getYears();
        } catch (Exception e) {
            return null;
        }
    }

    public static Set<LocalDate> extractDatesByDayOfWeek(LocalDate startDate, LocalDate endDate, DayOfWeek[] dayOfWeeks) {
        if (startDate == null || endDate == null || (dayOfWeeks == null || dayOfWeeks.length == 0)) {
            return Set.of();
        }
        return startDate.datesUntil(endDate.plusDays(1))
                .filter(date -> Arrays.stream(dayOfWeeks).anyMatch(dayOfWeek -> dayOfWeek == date.getDayOfWeek()))
                .collect(Collectors.toSet());
    }

    public static boolean isDateWithInRange(LocalDate date, LocalDate startDateInclusive, LocalDate endDateInclusive) {
        if (startDateInclusive == null || endDateInclusive == null || date == null) {
            return false;
        }
        return date.isAfter(startDateInclusive.minusDays(1)) && date.isBefore(endDateInclusive.plusDays(1));
    }
}
