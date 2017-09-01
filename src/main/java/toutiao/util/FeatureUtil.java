package toutiao.util;

import com.google.common.collect.Lists;
import toutiao.bean.FeatureResult;
import toutiao.bean.FormatedFeatureResult;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhipengwu on 17-8-31.
 */
public class FeatureUtil {

    /**
     * 将value根据给定边界值转换为向量
     * @param value
     * @param borderList
     * @return
     */
    public static  List<String> convertoVector(String value,List<Double> borderList){
        List<String> vector= Lists.newArrayList();
        int size = borderList.size();
        for (int i = 0; i < size+1; i++) {
            vector.add("0");
        }
        boolean isDone=false;
        for (int i = 0; i < size; i++) {
            if (Double.valueOf(value)<borderList.get(i)){
                vector.set(i,"1");
                isDone=true;
               break;
            }
        }
        if (!isDone){
            vector.set(size,"1");
        }else {
            vector.set(size,"0");
        }

        return vector;
    }

    /**
     * 改变FeatureResult中的某些特征的异常大的值
     * @param featureResult
     */
    public static void ConverFreatureResult(FeatureResult featureResult){
        //移动区域个数比值
//        featureResult.shiftAreaNumRatio=Double.valueOf(featureResult.shiftAreaNumRatio)>1.5?"1.5":featureResult.shiftAreaNumRatio;

        // 单天最大移动城市个数
        featureResult.maxShiftCityNum=Double.valueOf(featureResult.maxShiftCityNum)>15?"15":featureResult.maxShiftCityNum;

        //工作日平均移动区域个数
        featureResult.avgWorkdayShiftAreaNum=Double.valueOf(featureResult.avgWorkdayShiftAreaNum)>7?"7":featureResult.avgWorkdayShiftAreaNum;

        //节假日移动城市次数比值
        featureResult.weekendShiftCityCountRatio=Double.valueOf(featureResult.weekendShiftCityCountRatio)>35?"35":featureResult.weekendShiftCityCountRatio;

        //平均移动半径比值
        featureResult.avgActiveRadiusRatio=Double.valueOf(featureResult.avgActiveRadiusRatio)>144?"144":featureResult.avgActiveRadiusRatio;

        //移动城市热度之和
        featureResult.shfitCityTotalHeat=Double.valueOf(featureResult.shfitCityTotalHeat)>6?"6":featureResult.shfitCityTotalHeat;

        //移动区域总数
        featureResult.shiftAreaToalNum=Double.valueOf(featureResult.shiftAreaToalNum)>68?"68":featureResult.shiftAreaToalNum;

        //平均移动区域总数
        featureResult.avgShiftAreaNum=Double.valueOf(featureResult.avgShiftAreaNum)>27?"27":featureResult.avgShiftAreaNum;

        //移动城市个数
        featureResult.shiftCityTotalNum=Double.valueOf(featureResult.shiftCityTotalNum)>25?"25":featureResult.shiftCityTotalNum;

        //跨市移动次数
        featureResult.shiftCityTotal=Double.valueOf(featureResult.shiftCityTotal)>908.9?"908.9":featureResult.shiftCityTotal;

        //工作日跨市移动次数
        featureResult.workdayShiftCityCount=Double.valueOf(featureResult.workdayShiftCityCount)>656.6?"656.6":featureResult.workdayShiftCityCount;

        //节假日平均移动区域个数
        featureResult.avgWeekendShiftAreaNum=Double.valueOf(featureResult.avgWeekendShiftAreaNum)>10?"10":featureResult.avgWeekendShiftAreaNum;

        //工作日移动区域个数
        featureResult.workdayShiftAreaNum=Double.valueOf(featureResult.workdayShiftAreaNum)>860.8?"860.8":featureResult.workdayShiftAreaNum;

        //平均移动城市个数
        featureResult.avgShiftCityNum=Double.valueOf(featureResult.avgShiftCityNum)>13?"13":featureResult.avgShiftCityNum;

        //节假日移动区域个数
        featureResult.weekendShiftAreaNum=Double.valueOf(featureResult.weekendShiftAreaNum)>328.7?"328.7":featureResult.weekendShiftAreaNum;

        //节假日移动城市次数
        featureResult.weekendShiftCityCount=Double.valueOf(featureResult.weekendShiftCityCount)>252.7?"252.7":featureResult.weekendShiftCityCount;

        //单天活跃半径比值
//        featureResult.DayActiveRadiusRatio=Double.valueOf(featureResult.DayActiveRadiusRatio)>250?"250":featureResult.DayActiveRadiusRatio;

    }

    /**
     * 将正负例中分布差异较大的特征转换为向量
     * @param featureResult
     */
    public static void convertmajorFeaturetoVector(FeatureResult featureResult, FormatedFeatureResult formatedFeatureResult){

        //单天平均活动半径
        List<Double> radiusBorderList= Arrays.asList(6.5, 8.15, 9.82,11.45,13.09);
        List<String> avgDayActiveRadiusVector = convertoVector(featureResult.avgDayActiveRadius, radiusBorderList);
        formatedFeatureResult.avgDayActiveRadiusVector=avgDayActiveRadiusVector;

        //城市间移动次数
        radiusBorderList= Arrays.asList(1.9,2.6);
        List<String> maxShiftCityNumVector = convertoVector(featureResult.maxShiftCityNum, radiusBorderList);
        formatedFeatureResult.maxShiftCityNumVector=maxShiftCityNumVector;

        //节假日平均活动半径
        radiusBorderList= Arrays.asList(6.27,7.84,9.41,10.98,12.54);
        List<String> avgWeekendActiveRadiusVector = convertoVector(featureResult.avgWeekendActiveRadius, radiusBorderList);
        formatedFeatureResult.avgWeekendActiveRadiusVector=avgWeekendActiveRadiusVector;

        //工作日平均移动区域个数
        radiusBorderList= Arrays.asList(1.02);
        List<String> avgWorkdayShiftAreaNumVector = convertoVector(featureResult.avgWorkdayShiftAreaNum, radiusBorderList);
        formatedFeatureResult.avgWorkdayShiftAreaNumVector=avgWorkdayShiftAreaNumVector;

        //常住地城市等级
        radiusBorderList= Arrays.asList(2.2,3.0,4.0,5.0,5.5);
        List<String> residentCityLevelVector = convertoVector(featureResult.residentCityLevel, radiusBorderList);
        formatedFeatureResult.residentCityLevelVector=residentCityLevelVector;

        //单天最大活跃半径
        radiusBorderList= Arrays.asList(6.52,8.16,9.79,11.47,13.11);
        List<String> maxDayActiveRadiusVector = convertoVector(featureResult.maxDayActiveRadius, radiusBorderList);
        formatedFeatureResult.maxDayActiveRadiusVector=maxDayActiveRadiusVector;

        //移动的城市热度之和
        radiusBorderList= Arrays.asList(0.6,1.2,1.8);
        List<String> shfitCityTotalHeatVector = convertoVector(featureResult.shfitCityTotalHeat, radiusBorderList);
        formatedFeatureResult.shfitCityTotalHeatVector=shfitCityTotalHeatVector;

        //移动区域总个数
        radiusBorderList= Arrays.asList(6.9,12.8);
        List<String> shiftAreaToalNumVector = convertoVector(featureResult.shiftAreaToalNum, radiusBorderList);
        formatedFeatureResult.shiftAreaToalNumVector=shiftAreaToalNumVector;

        //工作日平均移动半径
        radiusBorderList= Arrays.asList(6.25,9.79,11.42,13.05);
        List<String> avgWorkdayActiveRadiusVector = convertoVector(featureResult.avgWorkdayActiveRadius, radiusBorderList);
        formatedFeatureResult.avgWorkdayActiveRadiusVector=avgWorkdayActiveRadiusVector;

        //手机等级
        radiusBorderList= Arrays.asList(3.0,5.0,7.0,9.0);
        List<String> phoneLevelVector = convertoVector(featureResult.phoneLevel, radiusBorderList);
        formatedFeatureResult.phoneLevelVector=phoneLevelVector;

        //头条活跃天数

        //出现过的城市总个数
        radiusBorderList= Arrays.asList(3.4,5.8);
        List<String> shiftCityTotalNumVector = convertoVector(featureResult.shiftCityTotalNum, radiusBorderList);
        formatedFeatureResult.shiftCityTotalNumVector=shiftCityTotalNumVector;
        //城市间移动总次数
        radiusBorderList= Arrays.asList(3.0,6.0,9.0);
        List<String> shiftCityTotalVector = convertoVector(featureResult.shiftCityTotal, radiusBorderList);
        formatedFeatureResult.shiftCityTotalVector=shiftCityTotalVector;

        //工作日城市间移动次数
        radiusBorderList= Arrays.asList(3.0,6.0,9.0);
        List<String> workdayShiftCityCountVector = convertoVector(featureResult.workdayShiftCityCount, radiusBorderList);
        formatedFeatureResult.workdayShiftCityCountVector=workdayShiftCityCountVector;

        //节假日平均移动区域个数
        radiusBorderList= Arrays.asList(1.06,1.2);
        List<String> avgWeekendShiftAreaNumVector = convertoVector(featureResult.avgWeekendShiftAreaNum, radiusBorderList);
        formatedFeatureResult.avgWeekendShiftAreaNumVector=avgWeekendShiftAreaNumVector;

        //节假日城市间移动次数
        radiusBorderList= Arrays.asList(1.0,2.0,3.0,4.0);
        List<String> weekendShiftCityCountVector = convertoVector(featureResult.weekendShiftCityCount, radiusBorderList);
        formatedFeatureResult.weekendShiftCityCountVector=weekendShiftCityCountVector;

        //单天移动半径比值
        radiusBorderList= Arrays.asList(234.85);
        List<String> DayActiveRadiusRatioVector = convertoVector(featureResult.DayActiveRadiusRatio, radiusBorderList);
        formatedFeatureResult.DayActiveRadiusRatioVector=DayActiveRadiusRatioVector;



    }

    public static void cloneFeaturetoFormatedFeatureResult(FeatureResult featureResult, FormatedFeatureResult formatedFeatureResult){
        formatedFeatureResult.keyid=featureResult.keyid;
        formatedFeatureResult.Lable=featureResult.Lable;
        formatedFeatureResult.avgShiftCityNum=featureResult.avgShiftCityNum;
        formatedFeatureResult.avgShiftAreaNum=featureResult.avgShiftAreaNum;
        formatedFeatureResult.gender=featureResult.gender;
        formatedFeatureResult.platform=featureResult.platform;
        formatedFeatureResult.shiftCityname=featureResult.shiftCityname;
        formatedFeatureResult.residentCity=featureResult.residentCity;
        formatedFeatureResult.age=featureResult.age;
        formatedFeatureResult.toutiaoActiveDayNum=featureResult.toutiaoActiveDayNum;
        formatedFeatureResult.weekendShiftCityCountRatio=featureResult.weekendShiftCityCountRatio;
        formatedFeatureResult.avgActiveRadiusRatio=featureResult.avgActiveRadiusRatio;
        formatedFeatureResult.shiftAreaNumRatio=featureResult.shiftAreaNumRatio;
        formatedFeatureResult.weekendShiftAreaNum=featureResult.weekendShiftAreaNum;
        formatedFeatureResult.workdayShiftAreaNum=featureResult.workdayShiftAreaNum;
    }




    public static void main(String[] args) {
        String value="1024";
        List<Double> borderList=Arrays.asList(16.0,32.0,512.0,1024.0);
        List<String> strings = convertoVector(value, borderList);
        System.out.println(strings.toString());

    }

}
