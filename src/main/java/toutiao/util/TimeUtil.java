package toutiao.util;

import com.google.common.base.Strings;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhipengwu on 16-8-29.
 */
public class TimeUtil {

  private static Logger logger= LoggerFactory.getLogger(TimeUtil.class);


    /**
     * 格式化日期格式,默认返回昨天的日期
     * @param inputTime yyyyMMdd类型或yyyy-MM-dd
     * @return yyyy-MM-dd类型日期格式
     */
    public static String formateTime(String inputTime){
        SimpleDateFormat df_in = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        SimpleDateFormat df_out = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String formatedTime="";
        if (null==inputTime||inputTime.isEmpty()){
            Date date=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            date = calendar.getTime();
            return df_out.format(date);
        }
        if (inputTime.contains("-")){
            Date inputDate = null;
            try {
                inputDate = df_out.parse(inputTime);
            } catch (ParseException e) {
                logger.error("日期格式转换失败,{}",e);
            }
            formatedTime=df_out.format(inputDate);
        }else {

            try {
                Date inputDate = df_in.parse(inputTime);
                formatedTime = df_out.format(inputDate);
            } catch (Exception e) {
                logger.error("日期格式转换失败,{}",e);

            }
        }

        return formatedTime;
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss:SSS 形式的时间转换为jodatime的 Datetime类型
     * @param time
     * @return
     */
    public static DateTime getDateTime(String time){
        if (Strings.isNullOrEmpty(time)){
            return null;
        }
        DateTimeFormatter fmt= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss:SSS");
        return fmt.parseDateTime(time);
    }

    /**
     * 将时间字符串转换为jodatime的Datetime类型
     * time: 表示时间的字符串
     * format: time 的时间格式,如: yyyy-MM-dd HH:mm:ss:SSS ; MM/dd/yyyy hh:mm:ss.SSS; yyyyMMdd HH:mm:ss:SSS
     *
     * 将yyyy-MM-dd HH:mm:ss:SSS 形式的时间转换为jodatime的 Datetime类型
     * @param time
     * @return
     */
    public static DateTime getDateTime(String time,String format){
        if (Strings.isNullOrEmpty(time)){
            return null;
        }
        DateTimeFormatter fmt= DateTimeFormat.forPattern(format);
        return fmt.parseDateTime(time);
    }


    public static void main(String[] args) {
//        System.out.println(formateTime("2016081ff9"));
//        System.out.println(formateTime("dfhhfgtghhg"));
//        System.out.println(formateTime(null));
//        System.out.println(formateTime(""));

        String actionTime="2017-01-15 07:12:50:232";
//         actionTime="20170115 07:12:50:232";

//        DateTime dateTime1 = getDateTime(actionTime, "yyyyMMdd HH:mm:ss:SSS");
        //Datatime类型时间格式化输出
//        System.out.println(dateTime1.toString("yyyyMMdd HH:mm:ss:SSS"));



        try {
            Date date = DateUtils.parseDate(actionTime,"yyyy-MM-dd HH:mm:ss:SSS");
            System.out.println(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
