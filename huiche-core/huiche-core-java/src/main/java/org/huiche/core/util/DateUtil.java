package org.huiche.core.util;

import lombok.experimental.UtilityClass;

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
 */
@UtilityClass
public class DateUtil {
    /**
     * 获取当前时间,到秒
     *
     * @return 时间字符串
     */
    public static String nowTime() {
        return now("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前时间到毫秒
     *
     * @return 时间字符串
     */
    public static String nowMilli() {
        return now("yyyy-MM-dd HH:mm:ss:SSS");
    }

    /**
     * 建议使用LocalDate/LocalDateTime,而不是Date
     *
     * @param time 时间字符串
     * @return Date
     */
    public static Date from(String time) {
        return from(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 建议使用LocalDate/LocalDateTime,而不是Date
     *
     * @param time   时间字符串
     * @param format 格式化
     * @return Date
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

    /**
     * 获取当前日期
     *
     * @return 日期字符串
     */
    public static String nowDate() {
        return now("yyyy-MM-dd");
    }

    /**
     * 按格式化条件获取当前时间
     *
     * @param format 格式化
     * @return 时间字符串
     */
    public static String now(String format) {
        return to((LocalDateTime) null, format);
    }

    /**
     * 日期转日期字符串
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String to(Date date) {
        return to(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期按格式化转字符串
     *
     * @param date   日期
     * @param format 格式化
     * @return 日期字符串
     */
    public static String to(Date date, String format) {
        return to(date2LocalDateTime(date), format);
    }

    /**
     * 日期时间按格式化转字符串
     *
     * @param dateTime 日期时间
     * @param format   格式化
     * @return 日期时间字符串
     */
    public static String to(LocalDateTime dateTime, String format) {
        dateTime = null == dateTime ? LocalDateTime.now() : dateTime;
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 日期时间字符串转日期时间
     *
     * @param time   时间字符串
     * @param format 格式化
     * @return 日期时间
     */
    public static LocalDateTime from(CharSequence time, String format) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取星期几(int)
     *
     * @return 星期几
     */
    public static int getDayOfWeek() {
        return LocalDate.now().getDayOfWeek().getValue();
    }

    /**
     * 本地日期时间转日期
     *
     * @param time 日期时间
     * @return 日期
     */
    public static Date local2Date(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 本地日期转日期
     *
     * @param date 本地日期
     * @return 日期
     */
    public static Date local2Date(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 日期转日期时间
     *
     * @param date 日期
     * @return 日期时间
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        date = null == date ? new Date() : date;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 日期转本地日期
     *
     * @param date 日期
     * @return 本地日期
     */
    public static LocalDate date2LocalDate(Date date) {
        date = null == date ? new Date() : date;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 解析日期未字符串
     *
     * @param format 格式化
     * @param date   日期
     * @return 日期字符串
     */
    public static String parse(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 解析long未日期字符串
     *
     * @param format 格式化
     * @param time   时间
     * @return 日期字符串
     */
    public static String parse(String format, Long time) {
        return parse(format, new Date(time));
    }

    /**
     * 获取偏移多少天的日期
     *
     * @param format 格式化
     * @param offset 偏移天数
     * @return 日期字符串
     */
    public static String getByDayOffset(String format, Integer offset) {
        long sep = 1000 * 60 * 60 * 24;
        format = StringUtil.isNotEmpty(format) ? format : "yyyy-MM-dd";
        offset = null == offset ? 0 : offset;
        return parse(format, System.currentTimeMillis() + sep * offset);
    }

    /**
     * 获取偏移多少天的日期
     *
     * @param offset 偏移
     * @return 日期字符串
     */
    public static String getByDayOffset(Integer offset) {
        return getByDayOffset(null, offset);
    }

    /**
     * 获取30天前的日期
     *
     * @return 日期字符串
     */
    public static String getDayMonthAgo() {
        return getByDayOffset(-30);
    }

    /**
     * 获取7天前的日期
     *
     * @return 日期字符串
     */
    public static String getDayWeekAgo() {
        return getByDayOffset(-7);
    }
}
