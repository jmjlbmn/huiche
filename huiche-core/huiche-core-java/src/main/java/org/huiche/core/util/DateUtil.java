package org.huiche.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间工具类
 *
 * @author Maning
 * @version 2017/6/27
 */
public class DateUtil {
    public static String nowTime() {
        return now();
    }

    public static String nowMilli() {
        return now("yyyy-MM-dd HH:mm:ss:SSS");
    }

    public static String now() {
        return now("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 建议使用LocalDate/LocalDateTime,而不是Date
     */
    public static Date from(String time) {
        return from(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 建议使用LocalDate/LocalDateTime,而不是Date
     */
    public static Date from(String time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String nowDate() {
        return now("yyyy-MM-dd");
    }

    public static String now(String format) {
        return to((LocalDateTime) null, format);
    }

    public static String to(Date date) {
        return to(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String to(Date date, String format) {
        return to(date2LocalDateTime(date), format);
    }

    public static String to(LocalDateTime time, String format) {
        time = null == time ? LocalDateTime.now() : time;
        return time.format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime from(CharSequence time, String format) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format));
    }

    public static int getDayOfWeek() {
        return LocalDate.now().getDayOfWeek().getValue();
    }

    public static Date local2Date(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date local2Date(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        date = null == date ? new Date() : date;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate date2LocalDate(Date date) {
        date = null == date ? new Date() : date;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String parse(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String parse(String format, Long time) {
        return parse(format, new Date(time));
    }

    public static String getByDayOffset(String format, Integer offset) {
        long sep = 1000 * 60 * 60 * 24;
        format = StringUtil.isNotEmpty(format) ? format : "yyyy-MM-dd";
        offset = null == offset ? 0 : offset;
        return parse(format, System.currentTimeMillis() + sep * offset);
    }

    public static String getByDayOffset(Integer offset) {
        return getByDayOffset(null, offset);
    }

    public static String getDayMonthAgo() {
        return getByDayOffset(-30);
    }

    public static String getDayWeekAgo() {
        return getByDayOffset(-7);
    }
}
