package mobike;

import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhipengwu on 17-8-18.
 */
public class FeatureMining {


    /**
     * 将yyyy-MM-dd HH:mm:ss:SSS 形式的时间转换为jodatime的 Datetime类型
     * @param time
     * @return
     */
    public static DateTime getDateTime(String time){
        if (Strings.isNullOrEmpty(time)){
            return null;
        }
        DateTimeFormatter fmt= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return fmt.parseDateTime(time);
    }

    public static void main(String[] args) {
        String inputFile="/home/zhipengwu/data/machineLearning/kaggle/mobike/MOBIKE_CUP_2017/train.csv";
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
            while (lineIterator.hasNext()){
                String line = lineIterator.nextLine();
                String[] split = line.split(",");
                //orderid,userid,bikeid,biketype,starttime,geohashed_start_loc,geohashed_end_loc
                if (split.length==7){
                    String starttime=split[4];
                    if (starttime.contains(":")) {
                        //分别得到时间的年 月 日 时 分 秒
                        DateTime dateTime = getDateTime(starttime);
                        int year = dateTime.getYear();
                        int monthOfYear = dateTime.getMonthOfYear();
                        int dayOfMonth = dateTime.getDayOfMonth();
                        int hourOfDay = dateTime.getHourOfDay();
                        int minuteOfHour = dateTime.getMinuteOfHour();
                        int secondOfMinute = dateTime.getSecondOfMinute();
                        System.out.println();
                    }
                    String startLoc= split[5];
                    String endLoc=split[6];

                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
