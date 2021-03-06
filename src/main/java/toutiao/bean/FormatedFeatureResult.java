package toutiao.bean;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import toutiao.util.AnalysisFeature;

import java.util.List;

/**
 * Created by zhipengwu on 17-8-31.
 */
public class FormatedFeatureResult {

    public FormatedFeatureResult() {
    }

    public String keyid;
    public String Lable;
    public String gender;
    public String platform;

    public String age;
    public List<String> avgShiftCityNumVector;

    // 平均移动区域个数
    public List<String> avgShiftAreaNumVector;

    public String toutiaoActiveDayNum;
    // 工作日跨市移动次数/周末跨市移动次数
    public String weekendShiftCityCountRatio;
    // 节假日|工作日平均移动半径比值
    public String avgActiveRadiusRatio;

    // 工作日移动区域个数
    public String workdayShiftAreaNum;

    // 节假日移动区域个数
    public String weekendShiftAreaNum;

    public String shiftCityname;
    public String residentCity;
    public String avgGDP;
    public String spendingPower;


    // 节假日|工作日 移动区域个数比值
    public List<String> shiftAreaNumRatioVector;
    public List<String> shiftCityTotalNumVector; // 到达过的城市个数
    public List<String> maxShiftCityNumVector;
    public List<String> shiftCityTotalVector; // 移动城市次数
    public List<String> maxDayActiveRadiusVector;
    public List<String> avgDayActiveRadiusVector;
    public List<String> DayActiveRadiusRatioVector;
    public List<String> phoneLevelVector;
    // public String modelHeat;

    public List<String> shiftAreaToalNumVector;

    // ===================20170829============================
    // 工作日跨市移动次数
    public List<String> workdayShiftCityCountVector;
    // 周末跨市移动次数
    public List<String> weekendShiftCityCountVector;

    // 移动城市热度之和
    public List<String> shfitCityTotalHeatVector;
    // 常住地城市等级
    public List<String> residentCityLevelVector;
    // 节假日平均移动半径
    public List<String> avgWeekendActiveRadiusVector;
    // 工作日平均移动半径
    public List<String> avgWorkdayActiveRadiusVector;

    public List<String> avgWorkdayShiftAreaNumVector;

    public List<String> avgWeekendShiftAreaNumVector;


    //预测的丢失的城市移动数目
    public String predictMissShiftCityNum;

    @Override
    public String toString() {
        List<String> result = Lists.newArrayList();
        result.add(keyid);
        result.add(Lable);
        result.add(gender);
        result.add(platform);
        result.add(age);
        result.addAll(avgShiftCityNumVector);
        result.addAll(avgShiftAreaNumVector);
        result.add(toutiaoActiveDayNum);
        result.add(weekendShiftCityCountRatio);
        result.add(avgActiveRadiusRatio);
        result.add(workdayShiftAreaNum);
        result.add(weekendShiftAreaNum);

        result.addAll(shiftAreaNumRatioVector);
        result.addAll(shiftCityTotalNumVector);
        result.addAll(maxShiftCityNumVector);
        result.addAll(shiftCityTotalVector);
        result.addAll(maxDayActiveRadiusVector);
        result.addAll(avgDayActiveRadiusVector);
        try {
            result.addAll(DayActiveRadiusRatioVector);
        } catch (Exception e) {
            System.out.println();
        }
        result.addAll(phoneLevelVector);
        result.addAll(shiftAreaToalNumVector);
        result.addAll(workdayShiftCityCountVector);
        result.addAll(weekendShiftCityCountVector);
        result.addAll(shfitCityTotalHeatVector);
        result.addAll(residentCityLevelVector);
        result.addAll(avgWeekendActiveRadiusVector);
        result.addAll(avgWorkdayActiveRadiusVector);
        result.addAll(avgWorkdayShiftAreaNumVector);
        result.addAll(avgWeekendShiftAreaNumVector);

        result.add(shiftCityname);
        result.add(residentCity);

        return Joiner.on(",").skipNulls().join(result);
    }

    /**
     * 将添加的新特征转换为csv格式
     * 
     * @return
     */
    public String newFeatureToString() {
        List<String> result = Lists.newArrayList();

        //todo 新标签
        String newLabel = AnalysisFeature.newLableMap.get(keyid);

        //todo 过滤掉新标签中不是正例的用户
        if (newLabel.equalsIgnoreCase("0")&&Lable.equalsIgnoreCase("1")){
            return null;
        }

        //todo 如果是正例用户,并且其出现过的城市个数为1,将其加1
//        try {
//            if (newLabel.equalsIgnoreCase("1") && shiftCityTotalNumVector.get(0).equals("1")) {
//                shiftCityTotalNumVector = Arrays.asList("2");
//            }
//        }catch (Exception e){
//            System.out.println();
//        }


        result.add(keyid);
        result.add(newLabel);
        result.addAll(phoneLevelVector);
        result.addAll(residentCityLevelVector);
        result.addAll(shfitCityTotalHeatVector);


        // 单天移动区域个数比值
        result.addAll(shiftAreaNumRatioVector);

        //单天移动区域个数 中值
        result.addAll(avgShiftAreaNumVector);


        //出现过的城市个数
        result.addAll(shiftCityTotalNumVector);

        //常住地城市人均GDP
//        result.add(avgGDP);

        //常住地之外城市消费能力
//        result.add(spendingPower);

        //预测的丢失城市值
//        result.add(predictMissShiftCityNum);


        // 2. 工作日/节假日跨市移动次数最大比值

        // 3. 最大活动半径/平均活动半径 比值

        return Joiner.on("\t").skipNulls().join(result);

    }

    public void posNegFeatureCmp(){
        String pos_file="/home/zhipengwu/secureCRT/pos_feature_result_file_20170918.txt";
        String neg_file="/home/zhipengwu/secureCRT/neg_feature_result_file_20170918.txt";



    }
}
