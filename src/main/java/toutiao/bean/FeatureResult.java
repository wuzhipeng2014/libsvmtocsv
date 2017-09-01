package toutiao.bean;

/**
 * Created by zhipengwu on 17-8-24.
 */
public class FeatureResult {
    public FeatureResult() {
    }

    public String keyid;
    public String Lable;
    public String shiftCityTotalNum; //到达过的城市个数
    public String avgShiftCityNum;
    public String maxShiftCityNum;
    public String shiftCityTotal; //移动城市次数
    public String maxDayActiveRadius;
    public String avgDayActiveRadius;
    public String DayActiveRadiusRatio;
    public String phoneLevel;
    // public String modelHeat;
    public String age;
    public String toutiaoActiveDayNum;
    public String shiftAreaToalNum;
    public String avgShiftAreaNum;
    public String gender;
    public String platform;
    public String shiftCityname;
    public String residentCity;
    // ===================20170829============================
    // 工作日跨市移动次数
    public String workdayShiftCityCount;
    // 周末跨市移动次数
    public String weekendShiftCityCount;
    // 工作日跨市移动次数/周末跨市移动次数
    public String weekendShiftCityCountRatio;
    // 移动城市热度之和
    public String shfitCityTotalHeat;
    // 常住地城市等级
    public String residentCityLevel;

    // 节假日平均移动半径
    public String avgWeekendActiveRadius;
    // 工作日平均移动半径
    public String avgWorkdayActiveRadius;
    // 节假日|工作日平均移动半径比值
    public String avgActiveRadiusRatio;

    //工作日移动区域个数
    public String workdayShiftAreaNum;
    public String avgWorkdayShiftAreaNum;
    //节假日移动区域个数
    public String weekendShiftAreaNum;
    public String avgWeekendShiftAreaNum;
    //节假日|工作日 移动区域个数比值
    public String shiftAreaNumRatio;


    public static String header = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
            "Label", "I1", "I2", "I3", "I4", "I5", "I6", "I7", "I8", "I9", "I10", "I11", "I12", "I13", "I14", "I15",
            "I16", "I17", "I18", "I19", "I20", "I21", "I22","I23","I24","I25","I26","I27", "I28","I29","C1", "C2");

    public static String header2 = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", "keyid", "Label",
            "shiftAreaToalNum", "avgShiftCityNum", "maxShiftCityNum", "shiftCityTotal", "maxDayActiveRadius",
            "avgDayActiveRadius", "DayActiveRadiusRatio", "phoneLevel", "age", "toutiaoActiveDayNum",
            "shiftAreaToalNum", "avgShiftAreaNum", "gender", "platform", "residentCityLevel", "shiftCityname",
            "residentCity");

    public String toString() {
        String format = String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                keyid, Lable, phoneLevel, age,gender, platform,toutiaoActiveDayNum,
                shiftCityTotalNum, shiftCityTotal, maxShiftCityNum, avgShiftCityNum, workdayShiftCityCount,
                weekendShiftCityCount, weekendShiftCityCountRatio, shfitCityTotalHeat, residentCityLevel,
                maxDayActiveRadius, avgDayActiveRadius, DayActiveRadiusRatio,  avgWeekendActiveRadius, avgWorkdayActiveRadius, avgActiveRadiusRatio,
                shiftAreaToalNum, avgShiftAreaNum,workdayShiftAreaNum,weekendShiftAreaNum,shiftAreaNumRatio,avgWorkdayShiftAreaNum,avgWeekendShiftAreaNum,
                shiftCityname, residentCity);
        return format;
    }


    public String printStringByLabel(String lb){
        if (lb.equalsIgnoreCase(Lable)) {
            String format = String.format(
                    "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                    Lable, phoneLevel, age, gender, platform, toutiaoActiveDayNum,
                    shiftCityTotalNum, shiftCityTotal, maxShiftCityNum, avgShiftCityNum, workdayShiftCityCount,
                    weekendShiftCityCount, weekendShiftCityCountRatio, shfitCityTotalHeat, residentCityLevel,
                    maxDayActiveRadius, avgDayActiveRadius, DayActiveRadiusRatio, avgWeekendActiveRadius, avgWorkdayActiveRadius, avgActiveRadiusRatio,
                    shiftAreaToalNum, avgShiftAreaNum, workdayShiftAreaNum, weekendShiftAreaNum, shiftAreaNumRatio, avgWorkdayShiftAreaNum, avgWeekendShiftAreaNum,
                    shiftCityname, residentCity);

            return format;
        }
        return null;
    }
}
