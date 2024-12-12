package kr.teamo2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TimeUtilTest {

    @Test
    void toLocalTime_validInput_success() {
        LocalTime time = TimeUtil.toLocalTime(14, 30);
        assertNotNull(time);
        assertEquals(14, time.getHour());
        assertEquals(30, time.getMinute());
    }

    @Test
    void toLocalTime_invalidInput_returnsNull() {
        assertNull(TimeUtil.toLocalTime(25, 60));
    }

    @Test
    void toLocalDateTime_validInput_success() {
        LocalDateTime dateTime = TimeUtil.toLocalDateTime("2024-03-14T15:30:00");
        assertNotNull(dateTime);
        assertEquals(2024, dateTime.getYear());
        assertEquals(3, dateTime.getMonthValue());
        assertEquals(14, dateTime.getDayOfMonth());
        assertEquals(15, dateTime.getHour());
        assertEquals(30, dateTime.getMinute());
    }

    @Test
    void calculateAge_validInput_success() {
        Integer age = TimeUtil.calculateAge("20000101");
        assertNotNull(age);
        assertTrue(age > 20); // 실제 나이는 현재 날짜에 따라 다름
    }

    @Test
    void extractDatesByDayOfWeek_validInput_success() {
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 31);
        DayOfWeek[] dayOfWeeks = new DayOfWeek[]{DayOfWeek.MONDAY};

        Set<LocalDate> dates = TimeUtil.extractDatesByDayOfWeek(startDate, endDate, dayOfWeeks);
        assertFalse(dates.isEmpty());
        dates.forEach(date -> assertEquals(DayOfWeek.MONDAY, date.getDayOfWeek()));
    }

    @Test
    void isDateWithInRange_validInput_success() {
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 31);
        LocalDate testDate = LocalDate.of(2024, 3, 15);

        assertTrue(TimeUtil.isDateWithInRange(testDate, startDate, endDate));
    }
}
