package org.huiche.core.util;

import lombok.experimental.UtilityClass;
import org.huiche.core.exception.HuiCheException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类,主要提供String和Date类型的转换
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
    @Nonnull
    public static String nowTime() {
        return now("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前时间到毫秒
     *
     * @return 时间字符串
     */
    @Nonnull
    public static String nowMilli() {
        return now("yyyy-MM-dd HH:mm:ss:SSS");
    }

    /**
     * 字符串转Date
     *
     * @param time 时间字符串
     * @return Date
     */
    @Nonnull
    public static Date from(@Nonnull String time) {
        return from(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 字符串转Date
     *
     * @param time   时间字符串
     * @param format 格式化
     * @return Date
     */
    @Nonnull
    public static Date from(@Nonnull String time, @Nonnull String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            throw new HuiCheException("日期解析失败: " + time + " , format: " + format);
        }
    }

    /**
     * 获取当前日期
     *
     * @return 日期字符串
     */
    @Nonnull
    public static String nowDate() {
        return now("yyyy-MM-dd");
    }

    /**
     * 按格式化条件获取当前时间
     *
     * @param format 格式化
     * @return 时间字符串
     */
    @Nonnull
    public static String now(@Nonnull String format) {
        return to((LocalDateTime) null, format);
    }

    /**
     * 日期转日期字符串
     *
     * @param date 日期
     * @return 日期字符串
     */
    @Nonnull
    public static String to(@Nonnull Date date) {
        return to(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期按格式化转字符串
     *
     * @param date   日期
     * @param format 格式化
     * @return 日期字符串
     */
    @Nonnull
    public static String to(@Nonnull Date date, @Nonnull String format) {
        return to(date2LocalDateTime(date), format);
    }

    /**
     * 日期时间按格式化转字符串
     *
     * @param dateTime 日期时间
     * @param format   格式化
     * @return 日期时间字符串
     */
    @Nonnull
    public static String to(@Nullable LocalDateTime dateTime, @Nonnull String format) {
        dateTime = null == dateTime ? LocalDateTime.now() : dateTime;
        return dateTime.format(DateTimeFormatter.ofPattern(format));
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
    @Nonnull
    public static Date local2Date(@Nonnull LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 本地日期转日期
     *
     * @param date 本地日期
     * @return 日期
     */
    @Nonnull
    public static Date local2Date(@Nonnull LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 日期转日期时间
     *
     * @param date 日期
     * @return 日期时间
     */
    @Nonnull
    public static LocalDateTime date2LocalDateTime(@Nullable Date date) {
        date = null == date ? new Date() : date;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 日期转本地日期
     *
     * @param date 日期
     * @return 本地日期
     */
    @Nonnull
    public static LocalDate date2LocalDate(@Nullable Date date) {
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
    @Nonnull
    public static String parse(@Nonnull String format, @Nonnull Date date) {
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
    @Nonnull
    public static String parse(@Nonnull String format, long time) {
        return parse(format, new Date(time));
    }

    /**
     * 获取偏移多少天的日期
     *
     * @param format 格式化
     * @param offset 偏移天数
     * @return 日期字符串
     */
    @Nonnull
    public static String getByDayOffset(@Nonnull String format, int offset) {
        long sep = 1000 * 60 * 60 * 24;
        return parse(format, System.currentTimeMillis() + sep * offset);
    }

    /**
     * 获取偏移多少天的日期
     *
     * @param offset 偏移
     * @return 日期字符串
     */
    @Nonnull
    public static String getByDayOffset(int offset) {
        return getByDayOffset("yyyy-MM-dd", offset);
    }

    /**
     * 获取30天前的日期
     *
     * @return 日期字符串
     */
    @Nonnull
    public static String getDayMonthAgo() {
        return getByDayOffset(-30);
    }

    /**
     * 获取7天前的日期
     *
     * @return 日期字符串
     */
    @Nonnull
    public static String getDayWeekAgo() {
        return getByDayOffset(-7);
    }

    /**
     * 获取某个月份的天数
     *
     * @param year  年
     * @param month 月份
     * @return 天数
     */
    public static int getMonthDays(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取当前月份的天数
     *
     * @return 天数
     */
    public static int getNowMonthDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }
}
