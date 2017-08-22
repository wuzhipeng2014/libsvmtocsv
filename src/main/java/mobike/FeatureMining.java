package mobike;

import com.google.common.base.Strings;
import mobike.util.GeoHash;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by zhipengwu on 17-8-18.
 * mobike竞赛数据特征挖掘
 */
public class FeatureMining {

    /**
     * 将yyyy-MM-dd HH:mm:ss:SSS 形式的时间转换为jodatime的 Datetime类型
     * 
     * @param time
     * @return
     */
    public static DateTime getDateTime(String time) {
        if (Strings.isNullOrEmpty(time)) {
            return null;
        }
        //测试数据格式化时间
//        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
        //训练数据格式化时间
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return fmt.parseDateTime(time);
    }

    public static void main(String[] args) {
        String inputFile = "src/main/resources/mobike/train.csv";
        String outputFile = "src/main/resources/mobike/fomat_train_lng.csv";
//
//        String inputFile = "src/main/resources/mobike/test.csv";
//        String outputFile = "src/main/resources/mobike/fomat_test.csv";

        try {
            FileWriter fw = new FileWriter(outputFile);
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
            String header = "userid,bikeid,biketype,year,month,day,hour,minute,second,start_lat_number,start_lat_decimal,start_lng_number,start_lng_decimal,end_lat_number,end_lat_decimal,end_lng_number,end_lng_decimal";
            fw.append(header+"\n");
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                String[] split = line.split(",");
                // orderid,userid,bikeid,biketype,starttime,geohashed_start_loc,geohashed_end_loc
                if (split.length == 7) {
                    String starttime = split[4];
                    if (starttime.contains(":")) {
                        // 分别得到时间的年 月 日 时 分 秒
                        DateTime dateTime = getDateTime(starttime);
                        int year = dateTime.getYear();
                        int monthOfYear = dateTime.getMonthOfYear();
                        int dayOfMonth = dateTime.getDayOfMonth();
                        int hourOfDay = dateTime.getHourOfDay();
                        int minuteOfHour = dateTime.getMinuteOfHour();
                        int secondOfMinute = dateTime.getSecondOfMinute();

                        String startLoc = split[5];
                        double[] decodeStart = GeoHash.decode(startLoc);
                        Double start_lat = decodeStart[0];
                        DoubleBean start_lat_double_bean = splitDouble(start_lat);
                        Double start_lng = decodeStart[1];
                        DoubleBean start_lng_double_bean = splitDouble(start_lng);

                        /*训练数据特征挖掘及格式化*/
                        String endLoc = split[6];
                        double[] decodeEnd = GeoHash.decode(endLoc);
                        double end_lat = decodeEnd[0];
                        DoubleBean end_lat_double_bean = splitDouble(end_lat);
                        double end_lng = decodeEnd[1];
                        DoubleBean end_lng_double_bean = splitDouble(end_lng);
//                        String format = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", split[1],
//                                split[2], split[3], year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour,
//                                secondOfMinute, start_lat_double_bean.number, start_lat_double_bean.decimal,
//                                start_lng_double_bean.number, start_lng_double_bean.decimal, end_lat_double_bean.number,
//                                end_lat_double_bean.decimal, end_lng_double_bean.number, end_lng_double_bean.decimal);
                        //生成训练数据经度训练特征
                        String format = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", split[1],
                                split[2], split[3], year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour,
                                secondOfMinute, start_lat_double_bean.number, start_lat_double_bean.decimal,
                                start_lng_double_bean.number, start_lng_double_bean.decimal,end_lng_double_bean.decimal);

                        //生成训练数据维度训练特征

                        /*测试数据特征挖掘及格式化*/
//                        String format = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", split[1],
//                                split[2], split[3], year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour,
//                                secondOfMinute, start_lat_double_bean.number, start_lat_double_bean.decimal,
//                                start_lng_double_bean.number, start_lng_double_bean.decimal);

                        fw.append(format);

//                        if (Math.abs( start_lat-end_lat) >=1||Math.abs(start_lng-end_lng)>=1){
//                            System.out.println(format);
//                        }

                    }

                }
            }
            fw.flush();
            fw.close();
            lineIterator.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DoubleBean splitDouble(double d) {
        DoubleBean doubleBean = new DoubleBean();
        String value = String.valueOf(d);
        String[] split = value.split("\\.");

        if (split.length == 2) {
            doubleBean.number = split[0];
            doubleBean.decimal = String.format("0.%s",split[1]);
        }
        return doubleBean;
    }

    public static void  writeTrainLat(){

    }

    public static void writeTrainLng(){

    }
}
