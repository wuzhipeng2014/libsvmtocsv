package toutiao.Feature.Bean;

import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.List;

import static toutiao.util.FeatureUtil.segmentFeature;

/**
 * Created by zhipengwu on 17-10-11.
 */
public class BasicFeature {
    public String keyId;//用户标识
    public int label;//正负例标签
    /*基础特征*/
    public int activeDays;//活跃天数
    private double activeWeeks;//活跃周数
    public int activeDaysOfTopCity;//最活跃城市的活跃天数
    public int cityNum;//活跃城市个数
    public int areaNum; //活跃区域个数
    /*区域移动类特征*/
    public int shiftAreas; //区域移动次数
    public int weekMaxshiftAreas; //周最大区域移动次数
    public int monthMaxshiftAreas; //月最大区域移动次数
    public double averageWeekshiftAreas; //平均周区域移动次数
    public double averageMonthshiftAreas; //平均月区域移动次数
    public double shiftAreaNumRatio;//周末平均移动区域个数/工作日平均移动区域个数 z
    public double midShiftAreaNum;//单天移动区域个数中位数 z
    /*城市移动类特征*/
    private int shiftCities; //总城市移动次数
    private double diffWorkAndWeekendCity;//周末不在工作日常住城市的比例 m
    private int weekMaxshiftCities; //周最大城市移动次数
    private int monthMaxshiftCites; //月最大城市移动次数
    private double averageWeekshiftCities; //平均周城市移动次数
    private double averageMonthshiftCities; //平均月城市移动次数
    /*活动半径类特征*/
    private double avgWeekActiveRadiusOnWorkingDay;//周平均,非节假日活动范围半径
    private double avgmonthActiveRadiusOnWorkingDay;//月平均,非节假日活动范围半径
    /*附加特征*/
    public double phoneLevel;//手机等级(手机价格/500) z
    public int residentCityLevel;//常住地城市等级 z


    //todo
    public int shiftCitysOnWorkingDay0;//非节假日跨市移动频次(周平均)
    public int shiftCitysOnWorkingDay1;
    public int arrivedCityTotalHeat;//到达过的城市总热度 z


    @Override
    public String toString() {
        return super.toString();

    }

    public static BasicFeature convertToBasicFeature(String line){
        if (Strings.isNullOrEmpty(line)){
            return null;
        }

        String[] split = line.split("\t");
        BasicFeature basicFeature=new BasicFeature();
        basicFeature.keyId=split[0];
        basicFeature.label= Integer.valueOf(split[1]);
        basicFeature.activeDays=Integer.valueOf(split[2]);
        basicFeature.activeWeeks=Integer.valueOf(split[3]);
        basicFeature.activeDaysOfTopCity=Integer.valueOf(split[4]);
        basicFeature.cityNum=Integer.valueOf(split[5]);
        basicFeature.areaNum=Integer.valueOf(split[6]);
        basicFeature.shiftAreas=Integer.valueOf(split[7]);
        basicFeature.weekMaxshiftAreas=Integer.valueOf(split[8]);
        basicFeature.monthMaxshiftAreas=Integer.valueOf(split[9]);
        basicFeature.averageWeekshiftAreas=Double.valueOf(split[10]);
        basicFeature.averageMonthshiftAreas=Double.valueOf(split[11]);
        basicFeature.shiftAreaNumRatio=Double.valueOf(split[12]);
        basicFeature.midShiftAreaNum=Double.valueOf(split[13]);
        basicFeature.shiftCities=Integer.valueOf(split[14]);
        basicFeature.diffWorkAndWeekendCity=Double.valueOf(split[15]);
        basicFeature.weekMaxshiftCities=Integer.valueOf(split[16]);
        basicFeature.monthMaxshiftCites=Integer.valueOf(split[17]);
        basicFeature.averageWeekshiftCities=Double.valueOf(split[18]);
        basicFeature.averageMonthshiftCities=Double.valueOf(split[19]);
        basicFeature.avgWeekActiveRadiusOnWorkingDay=Double.valueOf(split[20]);
        basicFeature.avgmonthActiveRadiusOnWorkingDay=Double.valueOf(split[21]);
        basicFeature.phoneLevel=Double.valueOf(split[22]);
        basicFeature.residentCityLevel=Integer.valueOf(split[23]);

        // 3. activeWeeks
       List<Double> borderList = Arrays.asList(0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5,
                17.5, 19.5, 21.5, 23.5, 27.5,28.5,29.5,30.5,31.5);
        basicFeature.activeWeeks =Double.valueOf(segmentFeature(String.valueOf(basicFeature.activeWeeks), borderList));



        return basicFeature;
    }




    public String toCSVFormateRow(){
        String format = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", label, activeDays, activeWeeks,activeDaysOfTopCity, cityNum, areaNum, shiftAreas, weekMaxshiftAreas, monthMaxshiftAreas, averageWeekshiftAreas, averageMonthshiftAreas, shiftAreaNumRatio, midShiftAreaNum,shiftCities,diffWorkAndWeekendCity,weekMaxshiftCities,monthMaxshiftCites,averageWeekshiftCities,averageMonthshiftCities,avgWeekActiveRadiusOnWorkingDay,avgmonthActiveRadiusOnWorkingDay ,phoneLevel, residentCityLevel);
        return format;
    }
}
