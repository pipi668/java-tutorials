/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * 生成 HTTP1.1标准时间格式
 * 
 * @author LiuJian
 * @date 2017年5月18日
 * 
 */
public class GMTDateFormat {

    public static void main(String[] args) throws ParseException {
        Calendar cal = Calendar.getInstance();

        // Locale.US用于将日期区域格式设为美国（英国也可以）。缺省改参数的话默认为机器设置，如中文系统星期将显示为汉子“星期六”
        SimpleDateFormat localDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);
        SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        // 时区设为格林尼治
        greenwichDate.setTimeZone(TimeZone.getTimeZone("GMT"));

        System.out.println("当前时间：" + localDate.format(cal.getTime()));
        System.out.println("格林尼治时间：" + greenwichDate.format(cal.getTime()));

        // 解析
        Date date1 = greenwichDate.parse("Wed, 17 May 2017 21:59:22 GMT");
        Date date2 = greenwichDate.parse("Fri, 06 Nov 2026 11:35:17 GMT");
        System.out.println(date1);
        System.out.println(date2);

    }
}
