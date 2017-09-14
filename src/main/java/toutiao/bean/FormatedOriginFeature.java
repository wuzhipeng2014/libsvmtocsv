package toutiao.bean;

import com.google.common.base.Joiner;
import toutiao.util.FeatureUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhipengwu on 17-9-8.
 */
public class FormatedOriginFeature {

    public String  keyid;
    public String  label;
    public String  activeDays ;
    public String  activeWeeks ;
    public String  activeDaysOfTopCity;
    public String  activeDaysOfEndCity;
    public String  gender;
    public String  age;
    public String  platform;
    public List<String> citeNumVector;
    public List<String>   areaNumVector;
    public String  shiftCitys0;
    public String  shiftCitys1;
    public String  shiftCitysOnWorkingDay0;
    public String  shiftCitysOnWorkingDay1;
    public String  shiftCitysOnWeekend0;
    public String  shiftCitysOnWeekend1;
    public String  avgActiveRadius0;
    public String  avgActiveRadius1;
    public String  maxActiveRadius0;
    public String  maxActiveRadius1;
    public String  avgActiveRadiusOnWorkingDay0;
    public String  avgActiveRadiusOnWorkingDay1;
    public String  maxActiveRadiusOnWorkingDay0;
    public String  maxActiveRadiusOnWorkingDay1;
    public String  avgActiveRadiusOnWeekend0;
    public String  avgActiveRadiusOnWeekend1;
    public String  maxActiveRadiusOnWeekend0;
    public String  maxActiveRadiusOnWeekend1;
    public String  model;
    public String  allCity;




    public void convertFeature(){
        List<Double> borderList = Arrays.asList(0.28, 0.55);
        FeatureUtil.convertoVector(avgActiveRadius0,borderList);
    }


    public void smoothFeature(){
        if (Double.valueOf(avgActiveRadiusOnWorkingDay0)>1000){

        }



    }


    public  String toCsv(){
        String format = String.format("%s,%s,%s,%s,%s, %s,%s,%s,%s,%s, %s,%s,%s,%s,%s, %s,%s,%s,%s,%s, %s,%s,%s,%s,%s, %s,%s,%s,%s,%s, %s",
                keyid, label, activeDays, activeWeeks, activeDaysOfTopCity,
                activeDaysOfEndCity, gender, age, platform, Joiner.on(",").skipNulls().join(citeNumVector),
                Joiner.on(",").skipNulls().join(areaNumVector), shiftCitys0, shiftCitys1, shiftCitysOnWorkingDay0, shiftCitysOnWorkingDay1,
                shiftCitysOnWeekend0, shiftCitysOnWeekend1, avgActiveRadius0, avgActiveRadius1, maxActiveRadius0,
                maxActiveRadius1, avgActiveRadiusOnWorkingDay0, avgActiveRadiusOnWorkingDay1, maxActiveRadiusOnWorkingDay0, maxActiveRadiusOnWorkingDay1, avgActiveRadiusOnWeekend0, avgActiveRadiusOnWeekend1, maxActiveRadiusOnWeekend0, maxActiveRadiusOnWeekend1,
                model, allCity);

        return format;
    }

}
