package toutiao.Feature.Bean;

import com.google.common.base.Strings;

/**
 * Created by zhipengwu on 17-10-11.
 */
public class BasicFeature {
    public String keyId;//用户标识
    public int label;//正负例标签
    /*基础特征*/
    public int activeDays;//活跃天数
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
    public double diffWorkAndWeekendCity;//周末不在工作日常住城市的比例 m

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
        basicFeature.activeDaysOfTopCity=Integer.valueOf(split[3]);
        basicFeature.cityNum=Integer.valueOf(split[4]);
        basicFeature.areaNum=Integer.valueOf(split[5]);
        basicFeature.shiftAreas=Integer.valueOf(split[6]);
        basicFeature.weekMaxshiftAreas=Integer.valueOf(split[7]);
        basicFeature.monthMaxshiftAreas=Integer.valueOf(split[8]);
        basicFeature.averageWeekshiftAreas=Double.valueOf(split[9]);
        basicFeature.averageMonthshiftAreas=Double.valueOf(split[10]);
        basicFeature.shiftAreaNumRatio=Double.valueOf(split[11]);
        basicFeature.midShiftAreaNum=Double.valueOf(split[12]);
        basicFeature.phoneLevel=Double.valueOf(split[13]);
        basicFeature.residentCityLevel=Integer.valueOf(split[14]);

        return basicFeature;
    }




    public String toCSVFormateRow(){
        String format = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", label, activeDays, activeDaysOfTopCity, cityNum, areaNum, shiftAreas, weekMaxshiftAreas, monthMaxshiftAreas, averageWeekshiftAreas, averageMonthshiftAreas, shiftAreaNumRatio, midShiftAreaNum, phoneLevel, residentCityLevel);
        return format;
    }
}
