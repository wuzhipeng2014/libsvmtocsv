package toutiao.bean;

/**
 * Created by zhipengwu on 17-8-24.
 */
public class FeatureResult {
    public FeatureResult (){}
    public String keyid;
    public String Lable;
    public String shiftCityTotalNum;
    public String avgShiftCityNum;
    public String maxShiftCityNum;
    public String shiftCityTotal;
    public String maxDayActiveRadius;
    public String avgDayActiveRadius;
    public String DayActiveRadiusRatio;
    public String phoneLevel;
//    public String modelHeat;
    public String age;
//    public String shfitCityTotalHeat;
    public String toutiaoActiveDayNum;
    public String shiftAreaToalNum;
    public String avgShitAreaNum;
    public String gender;
    public String platform;
    public String shiftCityname;
    public String residentCity;
    public static String header=String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s","keyid","Label","I1","I2","I3","I4","I5","I6","I7","I8","I9","I10","I11","I12","I13","I14","C1","C2");

    public static String header2=String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s","keyid","Label","shiftAreaToalNum","avgShiftCityNum","maxShiftCityNum","shiftCityTotal","maxDayActiveRadius","avgDayActiveRadius","DayActiveRadiusRatio","phoneLevel","age","toutiaoActiveDayNum","shiftAreaToalNum","avgShitAreaNum","gender","platform","shiftCityname","residentCity");


    public String toString(){
        String format = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", keyid,Lable, shiftAreaToalNum, avgShiftCityNum, maxShiftCityNum, shiftCityTotal, maxDayActiveRadius, avgDayActiveRadius, DayActiveRadiusRatio, phoneLevel, age,toutiaoActiveDayNum, shiftAreaToalNum, avgShitAreaNum, gender, platform, shiftCityname, residentCity);
        return format;
    }
}
