/**
 * 
 */
package cn.aposoft.tutorial.dateformat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

/**
 * UTC时间转换
 * 
 * @author Jann Liu
 *
 */
public class UtcDateFormatTest {

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Test
    public void formatUtcString() {
        Date date;
        try {
            date = formatter.parse("2016-04-06T09:46:06.000Z");
            System.out.println(formatter.format(date));
            Assert.assertEquals("2016-04-06T09:46:06.000Z", formatter.format(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 生成UTC 标准中国时
     */
    @Test
    public void formatUtcCSTString() {
        final DateFormat formatterUtc = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.US);
        Date d = new Date();
        System.out.println(formatterUtc.format(d));
        String str = "Wed, 06 Apr 2016 02:50:13 GMT";
        try {
            Date r = formatterUtc.parse(str);
            System.out.println(formatterUtc.format(r));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成 HTTP 协议使用的 UTC 的 GMT 标准时间
     */
    @Test
    public void formatUtcGMTString() {
        final DateFormat formatterUtc = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.US);
        formatterUtc.setTimeZone(TimeZone.getTimeZone("GMT"));
        @SuppressWarnings("deprecation")
        Date r = new Date(110, 0, 1, 8, 0, 0);
        Assert.assertEquals("Fri, 01 Jan 2010 00:00:00 GMT", formatterUtc.format(r));
    }

    /**
     * 生成基于ISO8601规范的日期字符串 GMT时间
     */
    @Test
    public void formatISO8601GMTString() {
        try {
            final DateFormat formatterGMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            formatterGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
            @SuppressWarnings("deprecation")
            Date r = new Date(110, 0, 1, 8, 0, 0);
            System.out.println(formatterGMT.format(r));
            Assert.assertEquals("2010-01-01T00:00:00.000Z", formatterGMT.format(r));
            Date d = formatterGMT.parse("2014-04-12T14:20:08.264Z");
            System.out.println(d);
            // formatterGMT.parse("2014-04-12T14:20:08.264Z");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成基于ISO8601规范的日期字符串 东8区时间
     */
    @Test
    public void formatISO8601ShanghaiString() {
        try {
            final DateFormat formatterGMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
            formatterGMT.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            @SuppressWarnings("deprecation")
            Date r = new Date(110, 0, 1, 8, 0, 0);
            System.out.println(formatterGMT.format(r));
            String result = formatterGMT.format(r);
            Assert.assertEquals("2010-01-01T08:00:00.000+0800", result);
            Assert.assertEquals("2010-01-01T08:00:00.000+08", result.substring(0, result.length() - 2));
            Date d = formatterGMT.parse("2014-04-12T14:20:08.264+0800");
            System.out.println(d);
            // formatterGMT.parse("2014-04-12T14:20:08.264Z");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示全部TimeZoneIDs
     */
    @Test
    public void listTimeZoneIDs() {

        // 通过假设停止后面的输出，采用注释取代假设
        // Assume.assumeTrue(false);
        String[] timeZoneIds = TimeZone.getAvailableIDs();
        Arrays.sort(timeZoneIds);
        System.out.println(Arrays.toString(timeZoneIds));
    }

}
