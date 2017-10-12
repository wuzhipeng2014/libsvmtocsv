package toutiao.Feature.Bean;

/**
 * Created by zhipengwu on 17-10-12.
 */
public class ProcessedFeature {
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


    public static ProcessedFeature convertoProcessedFeature(BasicFeature basicFeature){
        if (basicFeature==null){
            return null;
        }
        ProcessedFeature processedFeature=new ProcessedFeature();
        processedFeature.keyId=basicFeature.keyId;
        processedFeature.label=basicFeature.label;
        processedFeature.activeDays=basicFeature.activeDays;
        processedFeature.activeDaysOfTopCity=basicFeature.activeDaysOfTopCity;
        processedFeature.cityNum=basicFeature.cityNum;
        processedFeature.areaNum=basicFeature.areaNum;
        processedFeature.shiftAreas=basicFeature.shiftAreas;
        processedFeature.weekMaxshiftAreas=basicFeature.weekMaxshiftAreas;
        processedFeature.monthMaxshiftAreas=basicFeature.monthMaxshiftAreas;
        processedFeature.averageWeekshiftAreas=basicFeature.averageWeekshiftAreas;
        processedFeature.averageMonthshiftAreas=basicFeature.averageMonthshiftAreas;
        processedFeature.shiftAreaNumRatio=basicFeature.shiftAreaNumRatio;
        processedFeature.midShiftAreaNum=basicFeature.midShiftAreaNum;
        processedFeature.diffWorkAndWeekendCity=basicFeature.diffWorkAndWeekendCity;
        processedFeature.phoneLevel=basicFeature.phoneLevel;
        processedFeature.residentCityLevel=basicFeature.residentCityLevel;


//        processedFeature.label=basicFeature.label;
//        processedFeature.label=basicFeature.label;

        return processedFeature;

    }


    public String toCSVFormateRow(){
        String format = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", label, activeDays, activeDaysOfTopCity, cityNum, areaNum, shiftAreas, weekMaxshiftAreas, monthMaxshiftAreas, averageWeekshiftAreas, averageMonthshiftAreas, shiftAreaNumRatio, midShiftAreaNum, phoneLevel, residentCityLevel);
        return format;
    }

}
